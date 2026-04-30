package dev.yhiguchi.home_expense.presentation.api;

/** If-Matchヘッダーの解析。{@code @IfMatch} バリデーションで形式が保証されている前提。 */
public final class IfMatchParser {

  private IfMatchParser() {}

  public static long parse(String ifMatch) {
    return Long.parseLong(ifMatch.substring(1, ifMatch.length() - 1));
  }
}
