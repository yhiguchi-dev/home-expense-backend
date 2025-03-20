package dev.higuchi.jtd.json;

public interface JSONParser {

  <TYPE> TYPE parse(Object json, Class<TYPE> type);
}
