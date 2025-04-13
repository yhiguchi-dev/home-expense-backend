package dev.higuchi.jtd.core;

import dev.higuchi.jtd.core.form.Form;
import dev.higuchi.jtd.json.path.JSONPath;

public class Validation {

  Form form;

  JSONPath jsonPath;

  public Validation(Form form, JSONPath jsonPath) {
    this.form = form;
    this.jsonPath = jsonPath;
  }
}
