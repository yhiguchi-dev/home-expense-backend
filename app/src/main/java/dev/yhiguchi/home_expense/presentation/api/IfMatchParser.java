package dev.yhiguchi.home_expense.presentation.api;

import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;

/** If-Matchヘッダーの解析。 */
public final class IfMatchParser {

  private IfMatchParser() {}

  /**
   * {@code "<version>"} 形式の If-Match ヘッダー値からバージョン番号を取り出す。
   *
   * <p>{@code @IfMatch} バリデーションで形式と桁数が事前検証されている前提だが、検証を経ない呼び出しに備えて
   * 解析失敗時はバージョン不一致と等価と見なし {@link ConcurrentUpdateException} を送出する。
   */
  public static long parse(String ifMatch) {
    if (ifMatch == null || ifMatch.length() < 3) {
      throw new ConcurrentUpdateException();
    }
    try {
      return Long.parseLong(ifMatch.substring(1, ifMatch.length() - 1));
    } catch (NumberFormatException e) {
      throw new ConcurrentUpdateException();
    }
  }
}
