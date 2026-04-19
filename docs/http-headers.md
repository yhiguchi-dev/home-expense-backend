# HTTP ヘッダー調査と対応方針

## 目次

- [1. 背景と目的](#1-背景と目的)
- [2. 実測したレスポンスヘッダー](#2-実測したレスポンスヘッダー)
- [3. ヘッダーごとの設定値・目的・出典](#3-ヘッダーごとの設定値目的出典)
- [4. Quarkus における設定機構](#4-quarkus-における設定機構)
- [5. 追加検討事項と優先度](#5-追加検討事項と優先度)
- [6. 対応案](#6-対応案)
- [7. Envoy Gateway 運用時の配置方針](#7-envoy-gateway-運用時の配置方針)
- [8. テスト方針](#8-テスト方針)
- [9. 参考資料](#9-参考資料)

---

## 1. 背景と目的

Quarkus 3.34.3 上の本プロジェクトで、どのヘッダーが誰によって付与されているかを把握し、セキュリティ上必要なヘッダーが抜けなく適用されていることを保証する。あわせて、Quarkus のデフォルト挙動と設定オプションを明文化する。

---

## 2. 実測したレスポンスヘッダー

12種類のレスポンス（正常系・異常系・OPTIONS・health・未マッピングパス）を `@QuarkusTest` + RestAssured で観測した結果。

### 2.1 観測一覧

| 正規化キー (小文字) | 観測された表記 | 付与元 | 対象レスポンス |
|---|---|---|---|
| `x-frame-options` | `X-Frame-Options` | `quarkus.http.header.*` 設定 | 全レスポンス |
| `x-content-type-options` | `X-Content-Type-Options` | `quarkus.http.header.*` 設定 | 全レスポンス |
| `strict-transport-security` | `Strict-Transport-Security` | `quarkus.http.header.*` 設定 | 全レスポンス |
| `content-security-policy` | `Content-Security-Policy` | `quarkus.http.header.*` 設定 | 全レスポンス |
| `content-length` | `content-length`（常に小文字） | Vert.x / HTTPスタック | 全レスポンス（0 バイトでも付与） |
| `content-type` | `Content-Type` / `content-type` | JAX-RS / 各 extension | ボディあり時 |
| `content-encoding` | `content-encoding` | Quarkus compression | `gzip` / `identity` |
| `cache-control` | `cache-control` | SmallRye Health extension | `/q/health/*` のみ |
| `link` | `Link` | アプリコード | ページングGET |
| `allow` | `Allow` | JAX-RS | OPTIONS |

### 2.2 重要な所見

- **Quarkus/Vert.x がデフォルトで自動付与するセキュリティヘッダーは存在しない**。明示設定が唯一の手段。
- `Content-Length` は全レスポンスで自動付与される（Vert.x による）。
- HTTP ヘッダー名は case-insensitive（[RFC 9110 §5.1](https://www.rfc-editor.org/rfc/rfc9110#section-5.1)）。実運用では大小文字の差は影響しないが、RestAssured のアサーションも case-insensitive なので問題にならない。
- `Cache-Control: no-store` は SmallRye Health extension が `/q/health/*` に自動付与。
- Quarkus のデフォルト 404 フォールバックは `text/html`。REST 専用アプリでは違和感があるが、現状実害はない。
- `Date` ヘッダーは RestAssured の `Response.getHeaders()` に現れなかった。Vert.x 側で省略されている可能性。

---

## 3. ヘッダーごとの設定値・目的・出典

### 3.1 明示的に設定しているヘッダー（セキュリティ4種）

#### `X-Frame-Options: DENY`

- **目的**: クリックジャッキング対策。ブラウザが他サイトの `<frame>/<iframe>/<object>` 内でこのレスポンスを表示することを禁止。
- **値**: `DENY` = 同一オリジンであっても一切埋め込み不可。
- **出典**: [RFC 7034（Informational）](https://www.rfc-editor.org/rfc/rfc7034)
- **補足**: 現代の推奨は CSP `frame-ancestors` への移行。本プロジェクトは両方を設定済み（多重防御）。

#### `X-Content-Type-Options: nosniff`

- **目的**: ブラウザが `Content-Type` を無視して MIME sniffing することを禁止。JSON を誤って HTML や JavaScript として解釈されるのを防ぐ。
- **値**: `nosniff` が唯一定義された値。
- **出典**: [Fetch Standard §4.5](https://fetch.spec.whatwg.org/#x-content-type-options-header)（元は Microsoft 拡張、現在は標準化）。

#### `Strict-Transport-Security: max-age=31536000; includeSubDomains`

- **目的**: HTTPS 強制。一度このヘッダーを受け取ったブラウザは指定期間中、HTTP でのアクセスを自動的に HTTPS にアップグレードする。
- **値**:
  - `max-age=31536000` = 1 年間
  - `includeSubDomains` = サブドメインにも適用
- **出典**: [RFC 6797](https://www.rfc-editor.org/rfc/rfc6797)
- **注意**: HTTP（平文）で返しても無視される。HTTPS 化完了を確認の上で付与する。

#### `Content-Security-Policy: default-src 'none'; frame-ancestors 'none'`

- **目的**: リソース読み込み元とレンダリング文脈の制御。XSS 対策およびクリックジャッキング対策の強化版。
- **値**:
  - `default-src 'none'` = すべてのサブディレクティブの既定を「読み込み不可」に
  - `frame-ancestors 'none'` = `X-Frame-Options: DENY` の CSP 版（現代の標準）
- **出典**:
  - [CSP Level 3（W3C Working Draft）](https://www.w3.org/TR/CSP3/)
  - [CSP Level 2（W3C Recommendation）](https://www.w3.org/TR/CSP2/) — `frame-ancestors` はここから

### 3.2 HTTPスタック / extension が自動付与するヘッダー

#### `Content-Length: <byte数>`

- **目的**: レスポンスボディのバイト数通知。クライアントの受信終端判断に使用。
- **値**: 整数バイト数（空ボディでも `0`）。
- **出典**: [RFC 9110 §8.6](https://www.rfc-editor.org/rfc/rfc9110#section-8.6)
- **補足**: `Transfer-Encoding: chunked` のときは省略される。

#### `Content-Type: application/json;charset=UTF-8` など

- **目的**: ボディのメディアタイプ通知。クライアントがパース方法を決定するために使用。
- **値の選択基準**:
  - 通常の REST レスポンス: `application/json`
  - 例外マッパ経由のエラーレスポンス: `application/problem+json`
  - Quarkus デフォルト 404: `text/html`
- **出典**:
  - メディアタイプ一般: [RFC 9110 §8.3](https://www.rfc-editor.org/rfc/rfc9110#section-8.3)
  - `application/problem+json`: [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457)

#### `Content-Encoding: gzip` / `identity`

- **目的**: ボディの圧縮/エンコーディング通知。
- **出典**: [RFC 9110 §8.4](https://www.rfc-editor.org/rfc/rfc9110#section-8.4)
- **補足**: クライアントの `Accept-Encoding` に応じて Quarkus compression が自動適用（`quarkus.http.enable-compression=true`）。

#### `Cache-Control: no-store`（health エンドポイントのみ）

- **目的**: 中間キャッシュ（ブラウザ・プロキシ・CDN）にレスポンスを保存しないよう指示。
- **出典**: [RFC 9111 §5.2.2.5](https://www.rfc-editor.org/rfc/rfc9111#section-5.2.2.5)
- **補足**: SmallRye Health extension が自動付与。

#### `Link: <URI>; rel="<関係>", ...`

- **目的**: Web Linking。リソース間の関係を Hypermedia として通知。
- **本プロジェクトでの値**: `rel="current"` / `rel="first"` / `rel="last"` / `rel="next"` / `rel="prev"` でページング。
- **出典**:
  - フォーマット: [RFC 8288](https://www.rfc-editor.org/rfc/rfc8288)
  - ページング関係の rel 値: [IANA Link Relations](https://www.iana.org/assignments/link-relations/link-relations.xhtml)

#### `Allow: GET, POST, ...`

- **目的**: OPTIONS レスポンス時および 405 Method Not Allowed 時に、当該リソースで許可されているメソッドを通知。
- **出典**: [RFC 9110 §10.2.1](https://www.rfc-editor.org/rfc/rfc9110#section-10.2.1)

### 3.3 アプリレベルで使用する主要ヘッダー

#### `ETag: "<version>"`

- **目的**: リソースのバージョン識別子。楽観ロック（`If-Match` と組み合わせて競合検出）に使用。
- **本プロジェクトでの値**: Expense/Income の `version` フィールド。
- **出典**: [RFC 9110 §8.8.3](https://www.rfc-editor.org/rfc/rfc9110#section-8.8.3)

#### `If-Match: "<version>"`（リクエストヘッダー）

- **目的**: 条件付きリクエスト。指定した ETag と一致する場合のみ更新を許可。
- **出典**: [RFC 9110 §13.1.1](https://www.rfc-editor.org/rfc/rfc9110#section-13.1.1)
- **関連 status**:
  - 412 Precondition Failed（一致せず）
  - 428 Precondition Required（ヘッダー欠落）— [RFC 6585 §3](https://www.rfc-editor.org/rfc/rfc6585#section-3)

#### `Location: <URI>`

- **目的**: POST 成功時の 201 Created で新規リソースの URI を通知。
- **出典**: [RFC 9110 §10.2.2](https://www.rfc-editor.org/rfc/rfc9110#section-10.2.2)

### 3.4 CORS 関連

#### `Access-Control-Allow-Origin: <origin>`

- **目的**: クロスオリジンでの JavaScript からのアクセス許可。
- **本プロジェクトでの値**: `${CORS_ORIGINS}` 環境変数で制御。

#### `Access-Control-Allow-Methods: GET,POST,PUT,DELETE`

- **目的**: プリフライトで許可メソッドを通知。

#### `Access-Control-Allow-Headers: Content-Type,Accept,If-Match`

- **目的**: プリフライトで許可リクエストヘッダーを通知。

#### `Access-Control-Expose-Headers: ETag`

- **目的**: JavaScript からアクセス可能にするレスポンスヘッダーを明示。**`ETag` を読めないと楽観ロックが機能しないため必須**。

- **CORS一式の出典**:
  - [Fetch Standard §3](https://fetch.spec.whatwg.org/#http-cors-protocol)（最新の規範）
  - [W3C CORS Recommendation](https://www.w3.org/TR/cors/)（歴史的）

---

## 4. Quarkus における設定機構

### 4.1 結論：専用 extension は存在しない

Node.js の Helmet に相当する「依存追加だけで必須ヘッダーが自動付与される」extension は、本家 Quarkus / Quarkiverse のいずれにも存在しない。公式ガイド（[HTTP Reference](https://quarkus.io/guides/http-reference)）でも、設定プロパティによる宣言が標準アプローチとされている。

> There's no dedicated security headers extension mentioned—configuration properties are the standard approach

### 4.2 設定プロパティの構文

`quarkus-vertx-http`（core HTTP、`quarkus-rest-jackson` 経由で transitive 導入済み）が提供する。

```properties
quarkus.http.header.<HeaderName>.value    = <値>
quarkus.http.header.<HeaderName>.path     = <パスパターン>    # 任意、既定 /*
quarkus.http.header.<HeaderName>.methods  = <メソッド群>      # 任意、既定すべて
```

- パス / メソッドでの絞り込みが可能。
- 空値を設定するとヘッダー無効化も可能。

### 4.3 実装パターンの比較

| 手段 | 採否 | コメント |
|---|---|---|
| `quarkus.http.header.*` | ✅ 採用 | 公式推奨。明示的・レビュー可能。 |
| `ContainerResponseFilter` 実装 | ❌ 見送り | 動的ロジック（認証状態で切替など）がある時のみ必要。 |
| Quarkiverse の専用 extension | ❌ 存在しない | Helmet 相当なし。 |
| Quarkus にデフォルトで組み込む | ❌ 入っていない | 起動時のプラグイン等もなし。 |

---

## 5. 追加検討事項と優先度

| ヘッダー | 優先度 | 推奨値 | 適用範囲 | 理由 |
|---|---|---|---|---|
| `Cache-Control` | **高** | `no-store` | `/v1/*` | 家計データは私的情報。中間キャッシュ残存は情報漏洩リスク。 |
| `Referrer-Policy` | **中** | `no-referrer` | 全レスポンス | API エラー内容などが `Referer` 経由で外部サイトに漏れないように。 |
| CSP 強化 | **低** | `base-uri 'none'; form-action 'none'` 追加 | 全レスポンス | 既に `default-src 'none'` で防御済み。belt-and-suspenders レベル。 |
| `Permissions-Policy` | **低** | — | — | REST API はブラウザレンダリング文脈ではないため実効性薄。 |

---

## 6. 対応案

### 6.1 `application.properties` の追記

```properties
# キャッシュ抑止（ビジネスAPIのみ）
quarkus.http.header.Cache-Control.value = no-store
quarkus.http.header.Cache-Control.path  = /v1/*

# Referer 送信抑止（全域）
quarkus.http.header.Referrer-Policy.value = no-referrer

# （任意）CSP 強化
quarkus.http.header.Content-Security-Policy.value = default-src 'none'; frame-ancestors 'none'; base-uri 'none'; form-action 'none'
```

### 6.2 設計上のポイント

- `Cache-Control` は `/v1/*` に限定することで、SmallRye Health が既に付与している `/q/health/*` との **二重設定を避ける**。
- CSP は **1行にまとめる**（複数の `Content-Security-Policy` ヘッダーは AND ではなく OR 的に評価される仕様）。
- `Cache-Control: no-store` と `ETag` は両立可能。`no-store` は中間キャッシュを禁止するが、条件付きリクエスト（`If-Match`）の動作は阻害しない（[RFC 9111 §5.2.2.5](https://www.rfc-editor.org/rfc/rfc9111#section-5.2.2.5)）。
- `Referrer-Policy` は CORS 判定に影響しない（`Referer` と `Origin` は別ヘッダー）。

### 6.3 見送った項目

| 項目 | 見送り理由 |
|---|---|
| `Permissions-Policy` | REST API はブラウザ機能コンテキストでないため実効性薄 |
| `Content-Security-Policy-Report-Only` | 段階的ロールアウトが必要なほど複雑な CSP を持たない |
| `Server` / `X-Powered-By` 明示 | Quarkus デフォルト（無し）が望ましい |
| 専用 extension 導入 | 存在しないため選択肢外 |

---

## 7. Envoy Gateway 運用時の配置方針

本プロジェクトは前段に **Envoy Gateway（EG, Kubernetes Gateway API 実装）** を置く構成を予定している。TLS 終端・CORS・圧縮・汎用セキュリティヘッダー付与を edge で一元化し、Quarkus はアプリケーション関心事に集中させる方針を採る。

> **実装ステータス**: Quarkus 側の移管対応は実施済み（`application.properties` から移管対象ヘッダー / CORS / 圧縮設定を削除、proxy 透過設定を追加）。EG マニフェストの整備と本番切替は未完了。EG 稼働までは本アプリが返すセキュリティヘッダーは `Cache-Control: no-store`（`/v1/*`）のみとなる。

### 7.1 責務分担の原則

- **トランスポート / エッジ関心事は EG**: TLS、HSTS、CORS、圧縮、汎用セキュリティヘッダー、client IP 透過、`Server` ヘッダー抑止。
- **アプリケーション関心事は Quarkus**: `ETag`、`Location`、`Link`、`Allow`、`Content-Type`、業務パス固有の `Cache-Control`。
- **二重付与は避ける**: 同一ヘッダーを両側で設定すると、特に CSP は複数ヘッダーが AND 評価される仕様のため、意図せず緩くなる危険がある。一元化を原則とし、defense-in-depth が必要な場合も `ResponseHeaderModifier.set` で上書き前提にする。

### 7.2 ヘッダー別の配置表

| ヘッダー / 機能 | 配置先 | 具体的な EG CRD | 備考 |
|---|---|---|---|
| TLS 終端 | EG | `Gateway` listener + `ClientTrafficPolicy` | 証明書は Secret 参照。 |
| `Strict-Transport-Security` | EG | `HTTPRouteFilter` (`ResponseHeaderModifier`) | HTTPS listener 配下の Route のみに filter を適用。`preload` 運用は edge 一括管理。 |
| `X-Frame-Options` / `X-Content-Type-Options` / `Content-Security-Policy` / `Referrer-Policy` | EG | `HTTPRouteFilter` (`ResponseHeaderModifier.set`) | 共通セットは EG 独自 `kind: HTTPRouteFilter` に抽出して `extensionRef` で再利用。 |
| CORS 一式 | EG | `SecurityPolicy` | EG 1.1+ で GA。**`exposeHeaders: [ETag]` 必須**（楽観ロックに必要）。 |
| gzip / brotli 圧縮 | EG | `BackendTrafficPolicy.compression` | EG 1.2+。Quarkus 側は `enable-compression=false` に。 |
| Client IP 透過 | EG + Quarkus | `ClientTrafficPolicy.clientIPDetection.xForwardedFor.numTrustedHops` + `quarkus.http.proxy.*` | OTel / アクセスログの正確性に影響。 |
| `Server` ヘッダー抑止 | EG | `ClientTrafficPolicy.headers.serverHeader: Suppress` | 既定は `Overwrite`（`server: envoy`）。指紋最小化。 |
| `Cache-Control: no-store` (`/v1/*`) | **Quarkus 維持** | — | パス / 業務データの性質に紐づくため、アプリ側に残す。 |
| `ETag` / `Location` / `Link` / `Allow` / `Content-Type` / `Content-Length` | **Quarkus 維持** | — | アプリ / プロトコル由来。 |
| ヘルスチェックの `Cache-Control` | **Quarkus 維持** | — | SmallRye Health が `/q/health/*` に自動付与。EG 側は触らない。 |

### 7.3 EG 固有の注意点

1. **`ResponseHeaderModifier` は 3 動詞（`set` / `add` / `remove`）**。Quarkus と EG の両方で同名ヘッダーを出す可能性がある場合は必ず `set`（上書き）を使う。`add` は複数ヘッダー並列となり CSP 等では評価が緩くなる。
2. **HSTS の HTTPS 限定適用**は listener 分離で担保する。HTTP → HTTPS リダイレクト用の Gateway と業務 HTTPS Gateway を分け、後者にのみ HSTS filter を刺す。HTTP listener に HSTS を付けると無意味。
3. **`EnvoyPatchPolicy` は最終手段**。EG ネイティブで提供されない細部の Envoy 設定が必要な場合のみ使用。バージョン依存で壊れやすいため、まず Gateway API / SecurityPolicy / BackendTrafficPolicy で完結させる。
4. **CORS preflight は EG で完結**させ、backend に到達させない。Quarkus の `OPTIONS` 応答（`Allow` 付与）と混線しないよう `SecurityPolicy` の `targetRefs` を業務 Route に限定。
5. **二重圧縮事故は起きない**（EG は `Content-Encoding` 既存なら skip）が、Quarkus 側 CPU が無駄になるため `enable-compression=false` を明示。
6. **Quarkus 側の proxy 透過設定**を同時に入れる：`quarkus.http.proxy.proxy-address-forwarding`、`allow-forwarded`、`enable-forwarded-host`、`enable-forwarded-prefix`、`trusted-proxies`。`numTrustedHops` と hop 数が一致していること。

### 7.4 EG マニフェスト骨子

```yaml
# 共通セキュリティヘッダーをテンプレート化（EG 独自 CRD）
apiVersion: gateway.envoyproxy.io/v1alpha1
kind: HTTPRouteFilter
metadata: { name: security-headers }
spec:
  responseHeaderModifier:
    set:
      - { name: Strict-Transport-Security, value: "max-age=31536000; includeSubDomains" }
      - { name: X-Content-Type-Options,    value: "nosniff" }
      - { name: X-Frame-Options,           value: "DENY" }
      - { name: Content-Security-Policy,   value: "default-src 'none'; frame-ancestors 'none'; base-uri 'none'; form-action 'none'" }
      - { name: Referrer-Policy,           value: "no-referrer" }
---
apiVersion: gateway.envoyproxy.io/v1alpha1
kind: SecurityPolicy
metadata: { name: api-cors }
spec:
  targetRefs: [{ group: gateway.networking.k8s.io, kind: HTTPRoute, name: expense-api }]
  cors:
    allowOrigins: ["https://app.example.com"]
    allowMethods: [GET, POST, PUT, DELETE]
    allowHeaders: [Content-Type, Accept, If-Match]
    exposeHeaders: [ETag]
---
apiVersion: gateway.envoyproxy.io/v1alpha1
kind: BackendTrafficPolicy
metadata: { name: api-compression }
spec:
  targetRefs: [{ group: gateway.networking.k8s.io, kind: HTTPRoute, name: expense-api }]
  compression:
    - type: Gzip
---
apiVersion: gateway.envoyproxy.io/v1alpha1
kind: ClientTrafficPolicy
metadata: { name: gw-client }
spec:
  targetRefs: [{ group: gateway.networking.k8s.io, kind: Gateway, name: main-gw }]
  clientIPDetection:
    xForwardedFor: { numTrustedHops: 1 }
  headers:
    serverHeader: Suppress
```

### 7.5 Quarkus 側の最終状態（移管後）

実装済み。`application.properties` の該当部分は以下:

```properties
# HTTP cache directive for business API (edge-independent)
quarkus.http.header.Cache-Control.value = no-store
quarkus.http.header.Cache-Control.path = /v1/*

# Proxy transparency (Envoy Gateway fronts the service)
quarkus.http.proxy.proxy-address-forwarding = true
quarkus.http.proxy.allow-forwarded = true
quarkus.http.proxy.enable-forwarded-host = true
quarkus.http.proxy.enable-forwarded-prefix = true
quarkus.http.proxy.trusted-proxies = ${TRUSTED_PROXIES:127.0.0.1/32}
```

削除したもの:
- `quarkus.http.enable-compression`（EG の `BackendTrafficPolicy.compression` に移管）
- `quarkus.http.header.{X-Content-Type-Options,X-Frame-Options,Strict-Transport-Security,Content-Security-Policy,Referrer-Policy}.*`（EG の `HTTPRouteFilter.responseHeaderModifier` に移管）
- `quarkus.http.cors.*` および `%dev.quarkus.http.cors.origins`（EG の `SecurityPolicy.cors` に移管）

**`TRUSTED_PROXIES`** 環境変数は EG Pod の CIDR（K8s 環境に応じて）に設定する。未指定時は `127.0.0.1/32` にフォールバックするため、EG 非経由の dev / test では透過設定が事実上 no-op となり、既存挙動を壊さない。

### 7.6 段階的ロールアウト

| Phase | 内容 | 検証ポイント |
|---|---|---|
| **Phase 1**（EG 稼働前） | Quarkus に proxy 透過設定のみ先行投入 | アクセスログ / OTel の client IP が `X-Forwarded-For` で正しく解決される |
| **Phase 2**（EG 切替時） | CORS・圧縮・TLS/HSTS を EG に移管、Quarkus 側の該当設定削除。`X-*` / CSP / Referrer-Policy も EG に一元化 | `curl -I https://.../v1/expenses` で全ヘッダーが EG 由来で付与される |
| **Phase 3**（移管完了後） | `/v1/*` の `Cache-Control: no-store` 維持、`ETag` の CORS exposed-headers 維持を統合テストで保証 | 楽観ロック（`If-Match` 付き PUT）が EG 経由で破綻していない |

### 7.7 見送り / 保留

| 項目 | 判断 |
|---|---|
| EG 側への Rate limit 追加 | 将来検討。Global rate limit には Redis バックエンドが必要。 |
| EG 側への JWT / OIDC 認証 | 将来検討。`SecurityPolicy` の JWT / OIDC / API Key 機能で edge 認証を集中化できる。 |
| Quarkus 側での defense-in-depth | 原則一元化。CSP / HSTS は drift 防止のため EG 単独。固定値 1 種の `X-Content-Type-Options: nosniff` 等は両側出しても害は少ないが、運用コストを考慮して不採用。 |

---

## 8. テスト方針

### 8.1 Quarkus 側テスト（移管後の現状）

`app/src/test/java/dev/yhiguchi/home_expense/presentation/http/HttpSecurityHeadersIntegrationTest.java` では、Quarkus が責務を持ち続ける `Cache-Control` のみを検証する:

- `GET /v1/expenses` → `Cache-Control: no-store`（`quarkus.http.header.Cache-Control` 由来）
- `GET /q/health/live` → `Cache-Control: no-store`（SmallRye Health 由来）

移管した 5 種のセキュリティヘッダー（`X-Content-Type-Options` / `X-Frame-Options` / `Strict-Transport-Security` / `Content-Security-Policy` / `Referrer-Policy`）と CORS は Quarkus 単体では付与されないため、ここでは検証しない。

### 8.2 E2E / smoke テスト（EG 稼働後）

EG 経由の応答で以下を確認する（`curl -I` / Gateway API conformance / 別途 e2e スイートで実施する想定）:

- 5 セキュリティヘッダーが EG の `HTTPRouteFilter.responseHeaderModifier` 由来で付与されている
- CORS プリフライト（`OPTIONS`）が EG で完結し、backend に到達しない
- `Access-Control-Expose-Headers: ETag` が返り、楽観ロック（`If-Match` 付き PUT）が機能する
- `Content-Encoding: gzip` が EG 由来で付与される
- `Strict-Transport-Security` が HTTPS listener 配下でのみ付与される
- `X-Forwarded-For` 解釈で OTel / アクセスログの `client.address` が正しい値になる

---

## 9. 参考資料

### 9.1 最新の HTTP 仕様体系（2022年改訂）

| RFC | 内容 |
|---|---|
| [RFC 9110](https://www.rfc-editor.org/rfc/rfc9110) | HTTP Semantics |
| [RFC 9111](https://www.rfc-editor.org/rfc/rfc9111) | HTTP Caching |
| [RFC 9112](https://www.rfc-editor.org/rfc/rfc9112) | HTTP/1.1 |
| [RFC 9113](https://www.rfc-editor.org/rfc/rfc9113) | HTTP/2 |
| [RFC 9114](https://www.rfc-editor.org/rfc/rfc9114) | HTTP/3 |
| [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) | Problem Details for HTTP APIs |

9110 / 9111 / 9112 は従来の RFC 7230〜7235 を置き換えた最新版。

### 9.2 セキュリティヘッダー仕様

- [RFC 6797 - HTTP Strict Transport Security](https://www.rfc-editor.org/rfc/rfc6797)
- [RFC 7034 - X-Frame-Options](https://www.rfc-editor.org/rfc/rfc7034)
- [W3C Content Security Policy Level 3](https://www.w3.org/TR/CSP3/)
- [W3C Referrer Policy](https://www.w3.org/TR/referrer-policy/)
- [W3C Permissions Policy](https://www.w3.org/TR/permissions-policy/)
- [Fetch Standard - WHATWG](https://fetch.spec.whatwg.org/)

### 9.3 Quarkus 関連

- [Quarkus HTTP Reference](https://quarkus.io/guides/http-reference)
- [Quarkus CORS](https://quarkus.io/guides/security-cors)
- [All Quarkus extensions](https://quarkus.io/extensions/)

### 9.4 比較参考

- [Helmet.js](https://helmetjs.github.io/) — Node.js/Express 向けセキュリティヘッダー集約ミドルウェア

### 9.5 Envoy Gateway 関連

- [Envoy Gateway Documentation](https://gateway.envoyproxy.io/docs/)
- [Gateway API — HTTPRoute `ResponseHeaderModifier` filter](https://gateway-api.sigs.k8s.io/api-types/httproute/#filters-optional)
- [Envoy Gateway `SecurityPolicy` (CORS / JWT / OIDC / API Key / ExtAuth)](https://gateway.envoyproxy.io/docs/tasks/security/)
- [Envoy Gateway `ClientTrafficPolicy` / `BackendTrafficPolicy`](https://gateway.envoyproxy.io/docs/tasks/traffic/)
- [Envoy Gateway `HTTPRouteFilter` (custom filter CRD)](https://gateway.envoyproxy.io/docs/api/extension_types/#httproutefilter)
