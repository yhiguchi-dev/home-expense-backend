package dev.higuchi.jtd.core.form;

import dev.higuchi.jtd.json.JSONRepresentation;

public sealed interface Form permits Discriminator, Elements, Empty, Enum, Properties, Ref, Type, Values {
}
