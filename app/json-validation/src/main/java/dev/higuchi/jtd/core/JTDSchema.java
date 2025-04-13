package dev.higuchi.jtd.core;

import dev.higuchi.jtd.core.form.FormCreator;
import dev.higuchi.jtd.json.JSONRepresentation;

import javax.xml.validation.Schema;
import java.util.Map;
import java.util.Set;

public interface JTDSchema {

  Map<String, ? extends JTDSchema> definitions();

  Boolean nullable();

  JSONRepresentation metadata();

  String ref();

  String type();

  Set<String> enm();

  JTDSchema elements();

  Map<String, ? extends JTDSchema> properties();

  Map<String, ? extends JTDSchema> optionalProperties();

  Boolean additionalProperties();

  JTDSchema values();

  String discriminator();

  Map<String, ? extends JTDSchema> mapping();

  boolean hasDefinitions();

  boolean hasNullable();

  boolean hasMetadata();

  boolean hasRef();

  boolean hasType();

  boolean hasEnum();

  boolean hasElements();

  boolean hasProperties();

  boolean hasOptionalProperties();

  boolean hasAdditionalProperties();

  boolean hasValues();

  boolean hasDiscriminator();

  boolean hasMapping();
}
