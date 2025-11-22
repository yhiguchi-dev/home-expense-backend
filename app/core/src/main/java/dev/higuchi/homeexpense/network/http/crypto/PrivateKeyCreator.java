package dev.higuchi.homeexpense.network.http.crypto;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

interface PrivateKeyCreator {

  default PrivateKey create(String key, String algorithm) {
    try {
      String _key =
          key.replaceAll("-----BEGIN.*-----", "")
              .replaceAll("-----END.*-----", "")
              .replaceAll("\n", "");
      byte[] bytes = Base64.getDecoder().decode(_key);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
      KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
      return keyFactory.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
