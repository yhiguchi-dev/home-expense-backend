package dev.higuchi.homeexpense.network.http.crypto;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

public class KeyStoreBuilder implements PrivateKeyCreator, CertificateCreator {

  KeyStore keyStore;

  private KeyStoreBuilder(KeyStore keyStore) {
    this.keyStore = keyStore;
  }

  public static KeyStoreBuilder create() {
    try {
      return new KeyStoreBuilder(KeyStore.getInstance(KeyStore.getDefaultType()));
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  public KeyStoreBuilder addCertificateEntry(String alias, String certificate) {
    Certificate _certificate = create(certificate);
    try {
      keyStore.setCertificateEntry(alias, _certificate);
      return this;
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  public KeyStoreBuilder addKeyEntry(
      String alias,
      String privateKey,
      String privateKeyAlgorithm,
      String password,
      List<String> certificateChain) {
    PrivateKey _privateKey = create(privateKey, privateKeyAlgorithm);
    Certificate[] chain = certificateChain.stream().map(this::create).toArray(Certificate[]::new);
    char[] _password = password.toCharArray();
    try {
      keyStore.setKeyEntry(alias, _privateKey, _password, chain);
      return this;
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  public KeyStore build() {
    return keyStore;
  }
}
