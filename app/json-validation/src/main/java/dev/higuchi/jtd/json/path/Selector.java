package dev.higuchi.jtd.json.path;

public sealed interface Selector permits NameSelector, IndexSelector, WildcardSelector {}
