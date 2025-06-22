package dev.higuchi.jtd.core.form;

public sealed interface Form
    permits Discriminator, Elements, Empty, Enum, Properties, Ref, Type, Values {}
