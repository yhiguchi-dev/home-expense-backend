package dev.higuchi.jtd.core.form;

import dev.higuchi.jtd.core.JTDSchema;
import java.util.Arrays;
import java.util.Optional;

public enum FormPattern {
  EMPTY(new boolean[] {false, false, false, false, false, false, false, false, false, false}),
  REF(new boolean[] {true, false, false, false, false, false, false, false, false, false}),
  TYPE(new boolean[] {false, true, false, false, false, false, false, false, false, false}),
  ENUM(new boolean[] {false, false, true, false, false, false, false, false, false, false}),
  ELEMENTS(new boolean[] {false, false, false, true, false, false, false, false, false, false}),
  PROPERTIES(new boolean[] {false, false, false, false, true, false, false, false, false, false}),
  OPTIONAL_PROPERTIES(
      new boolean[] {false, false, false, false, false, true, false, false, false, false}),
  PROPERTIES_AND_OPTIONAL_PROPERTIES(
      new boolean[] {false, false, false, false, true, true, false, false, false, false}),
  PROPERTIES_AND_ADDITIONAL_PROPERTIES(
      new boolean[] {false, false, false, false, true, false, true, false, false, false}),
  OPTIONAL_PROPERTIES_AND_ADDITIONAL_PROPERTIES(
      new boolean[] {false, false, false, false, false, true, true, false, false, false}),
  PROPERTIES_AND_OPTIONAL_PROPERTIES_AND_ADDITIONAL_PROPERTIES(
      new boolean[] {false, false, false, false, true, true, true, false, false, false}),
  VALUES(new boolean[] {false, false, false, false, false, false, false, true, false, false}),
  DISCRIMINATOR(new boolean[] {false, false, false, false, false, false, false, false, true, true}),
  ;
  final boolean[] value;

  FormPattern(boolean[] value) {
    this.value = value;
  }

  public static Optional<FormPattern> from(JTDSchema schema) {
    boolean[] forms = {
      schema.hasRef(),
      schema.hasType(),
      schema.hasEnum(),
      schema.hasElements(),
      schema.hasProperties(),
      schema.hasOptionalProperties(),
      schema.hasAdditionalProperties(),
      schema.hasValues(),
      schema.hasDiscriminator(),
      schema.hasMapping()
    };
    for (FormPattern pattern : values()) {
      if (Arrays.equals(pattern.value(), forms)) {
        return Optional.of(pattern);
      }
    }
    return Optional.empty();
  }

  public boolean[] value() {
    return value;
  }
}
