package dev.higuchi.homeexpense.network.http.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;

public class JSONBodyHandler<TYPE> implements HttpResponse.BodyHandler<TYPE> {
  Class<TYPE> clazz;
  JSONParser parser;

  public JSONBodyHandler(Class<TYPE> clazz, JSONParser parser) {
    this.clazz = clazz;
    this.parser = parser;
  }

  @Override
  public HttpResponse.BodySubscriber<TYPE> apply(HttpResponse.ResponseInfo responseInfo) {
    return HttpResponse.BodySubscribers.mapping(
        HttpResponse.BodySubscribers.ofInputStream(),
        (InputStream inputStream) -> {
          try (InputStream stream = inputStream) {
            return parser.parse(stream, clazz);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        });
  }
}
