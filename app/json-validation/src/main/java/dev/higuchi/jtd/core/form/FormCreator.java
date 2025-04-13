package dev.higuchi.jtd.core.form;

import dev.higuchi.jtd.core.JTDSchema;

public interface FormCreator {
    default Form create(JTDSchema schema) {
        FormPattern pattern = FormPattern.from(schema).get();
        return switch (pattern) {
            case EMPTY -> new Empty();
            case REF -> new Ref();
            case TYPE -> new Type();
            case ENUM -> new Enum();
            case ELEMENTS -> new Elements();
            case PROPERTIES, OPTIONAL_PROPERTIES,
                 PROPERTIES_AND_OPTIONAL_PROPERTIES, PROPERTIES_AND_ADDITIONAL_PROPERTIES,
                 OPTIONAL_PROPERTIES_AND_ADDITIONAL_PROPERTIES, PROPERTIES_AND_OPTIONAL_PROPERTIES_AND_ADDITIONAL_PROPERTIES -> new Properties();
            case VALUES -> new Values();
            case DISCRIMINATOR -> new Discriminator();
        };
    }
}
