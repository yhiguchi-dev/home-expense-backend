package dev.higuchi.homeexpense.json;

public interface JSONParser {

  <TYPE> TYPE parse(Object json, Class<TYPE> type);
}
