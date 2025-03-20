package dev.higuchi.homeexpense.network.http;

public record MtlsConfig(
    String certificate, String privateKey, String privateKeyAlgorithm, String caCertificate) {}
