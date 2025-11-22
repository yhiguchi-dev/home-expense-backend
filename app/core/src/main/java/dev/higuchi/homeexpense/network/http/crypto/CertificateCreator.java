package dev.higuchi.homeexpense.network.http.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

interface CertificateCreator {

  default Certificate create(String certificate) {
    try (InputStream inputStream =
        new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8))) {
      return CertificateFactory.getInstance("X509").generateCertificate(inputStream);
    } catch (IOException | CertificateException e) {
      throw new RuntimeException(e);
    }
  }
}
