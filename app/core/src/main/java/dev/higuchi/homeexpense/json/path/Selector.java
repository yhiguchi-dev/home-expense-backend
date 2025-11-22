package dev.higuchi.homeexpense.json.path;

public sealed interface Selector permits NameSelector, IndexSelector, WildcardSelector {}
