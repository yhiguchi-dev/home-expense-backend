package dev.higuchi.homeexpense.network.http.json;

import java.io.InputStream;

public interface JSONParser {

  <TYPE> TYPE parse(InputStream inputStream, Class<TYPE> clazz);
}
