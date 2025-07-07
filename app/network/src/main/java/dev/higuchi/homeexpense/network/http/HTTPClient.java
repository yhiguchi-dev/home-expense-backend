package dev.higuchi.homeexpense.network.http;

public class HTTPClient {

  //  HttpClient.Builder builder;
  //  JSONParser parser;
  //
  //  HTTPClient(Executor executor, Duration connectTimeout, JSONParser parser) {
  //    this.builder = HttpClient.newBuilder().executor(executor).connectTimeout(connectTimeout);
  //    this.parser = parser;
  //  }
  //
  //  public <TYPE> HttpResponse<TYPE> get(
  //      String url, Map<String, List<String>> query, Class<TYPE> clazz) {
  //    HttpRequest httpRequest =
  //        HttpRequest.newBuilder(createURI(url, query))
  //            .header("content-type", "application/json")
  //            .timeout(Duration.ofSeconds(10))
  //            .GET()
  //            .build();
  //    return send(httpRequest, clazz);
  //  }
  //
  //  URI createURI(String url, Map<String, List<String>> query) {
  //    List<String> list = new ArrayList<>();
  //    query.forEach(
  //        (s, strings) -> {
  //          strings.forEach(
  //              s1 -> {
  //                list.add(String.format("%s=%s", s, s1));
  //              });
  //        });
  //    return URI.create(String.format("%s?%s", url, String.join("&", list)));
  //  }
  //
  //  public <TYPE> HttpResponse<TYPE> send(HttpRequest httpRequest, Class<TYPE> clazz) {
  //    try (HttpClient httpClient = builder.build()) {
  //      try {
  //        return httpClient.send(httpRequest, new JSONBodyHandler<>(clazz, parser));
  //      } catch (IOException | InterruptedException e) {
  //        throw new RuntimeException(e);
  //      }
  //    }
  //  }
}
