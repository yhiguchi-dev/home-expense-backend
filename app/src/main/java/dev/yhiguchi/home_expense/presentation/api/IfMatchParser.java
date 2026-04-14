package dev.yhiguchi.home_expense.presentation.api;

/** If-Matchヘッダーの解析 */
public class IfMatchParser {

  private IfMatchParser() {}

  /**
   * If-Matchヘッダー値からバージョンを取得する。
   *
   * @throws PreconditionRequiredException If-Matchヘッダーが未指定の場合
   * @throws InvalidIfMatchException If-Matchヘッダーの形式が不正な場合
   */
  public static long parse(String ifMatch) {
    if (ifMatch == null || ifMatch.isBlank()) {
      throw new PreconditionRequiredException();
    }
    try {
      String value = ifMatch.strip();
      if (value.startsWith("\"") && value.endsWith("\"")) {
        value = value.substring(1, value.length() - 1);
      }
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new InvalidIfMatchException();
    }
  }
}
