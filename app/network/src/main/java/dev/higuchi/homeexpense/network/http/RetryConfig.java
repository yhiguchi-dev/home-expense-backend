package dev.higuchi.homeexpense.network.http;

public record RetryConfig(int retries, int timeout, int delay) {}
