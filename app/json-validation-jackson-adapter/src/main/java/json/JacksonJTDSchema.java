package json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.higuchi.jtd.core.JTDSchema;
import dev.higuchi.jtd.json.JSONRepresentation;
import java.util.Map;
import java.util.Set;

public class JacksonJTDSchema implements JTDSchema {
  @JsonProperty("definitions")
  Map<String, JacksonJTDSchema> definitions;

  @JsonProperty("nullable")
  Boolean nullable;

  @JsonProperty("metadata")
  JsonNode metadata;

  @JsonProperty("ref")
  String ref;

  @JsonProperty("type")
  String type;

  @JsonProperty("enum")
  Set<String> enm;

  @JsonProperty("elements")
  JacksonJTDSchema elements;

  @JsonProperty("properties")
  Map<String, JacksonJTDSchema> properties;

  @JsonProperty("optionalProperties")
  Map<String, JacksonJTDSchema> optionalProperties;

  @JsonProperty("additionalProperties")
  Boolean additionalProperties;

  @JsonProperty("values")
  JacksonJTDSchema values;

  @JsonProperty("discriminator")
  String discriminator;

  @JsonProperty("mapping")
  Map<String, JacksonJTDSchema> mapping;

  @Override
  public Map<String, ? extends JTDSchema> definitions() {
    return definitions;
  }

  @Override
  public Boolean nullable() {
    return nullable;
  }

  @Override
  public JSONRepresentation metadata() {
    return JacksonJSON.createRoot(metadata);
  }

  @Override
  public String ref() {
    return ref;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public Set<String> enm() {
    return enm;
  }

  @Override
  public JTDSchema elements() {
    return elements;
  }

  @Override
  public Map<String, ? extends JTDSchema> properties() {
    return properties;
  }

  @Override
  public Map<String, ? extends JTDSchema> optionalProperties() {
    return optionalProperties;
  }

  @Override
  public Boolean additionalProperties() {
    return additionalProperties;
  }

  @Override
  public JTDSchema values() {
    return values;
  }

  @Override
  public String discriminator() {
    return discriminator;
  }

  @Override
  public Map<String, ? extends JTDSchema> mapping() {
    return mapping;
  }

  @Override
  public boolean hasDefinitions() {
    return false;
  }

  @Override
  public boolean hasNullable() {
    return false;
  }

  @Override
  public boolean hasMetadata() {
    return false;
  }

  @Override
  public boolean hasRef() {
    return false;
  }

  @Override
  public boolean hasType() {
    return false;
  }

  @Override
  public boolean hasEnum() {
    return false;
  }

  @Override
  public boolean hasElements() {
    return false;
  }

  @Override
  public boolean hasProperties() {
    return false;
  }

  @Override
  public boolean hasOptionalProperties() {
    return false;
  }

  @Override
  public boolean hasAdditionalProperties() {
    return false;
  }

  @Override
  public boolean hasValues() {
    return false;
  }

  @Override
  public boolean hasDiscriminator() {
    return false;
  }

  @Override
  public boolean hasMapping() {
    return false;
  }
}
