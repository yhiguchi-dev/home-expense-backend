package dev.higuchi.homeexpense.network.http.crypto;

import java.security.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLContextCreator {

  public static SSLContext create(KeyStore keyStore) {
    try {
      KeyManagerFactory keyManagerFactory =
          KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore, null);

      TrustManagerFactory trustManagerFactory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(
          keyManagerFactory.getKeyManagers(),
          trustManagerFactory.getTrustManagers(),
          SecureRandom.getInstanceStrong());
      return sslContext;
    } catch (NoSuchAlgorithmException
        | KeyStoreException
        | UnrecoverableKeyException
        | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }
}
