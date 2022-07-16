package com.diffbuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.tersesystems.echopraxia.api.Field;
import com.tersesystems.echopraxia.api.FieldBuilderResult;
import com.tersesystems.echopraxia.api.Value;

import static com.diffbuilder.DiffFieldBuilderConstants.mapper;

public interface DiffFieldBuilder {

  default FieldBuilderResult diff(String fieldName, Value<?> before, Value<?> after) {
    JsonNode beforeNode = mapper.valueToTree(before);
    JsonNode afterNode = mapper.valueToTree(after);

    JsonNode patch = JsonDiff.asJson(beforeNode, afterNode);
    // convert the patch json node back to fields and values :-)
    final Value<?> value = mapper.convertValue(patch, Value.class);
    return Field.keyValue(fieldName, value);
  }
}

class DiffFieldBuilderConstants {
  static ObjectMapper mapper = new ObjectMapper();
  static {
    // should register EchopraxiaModule here because of jackson dependency
    mapper.findAndRegisterModules();
  }
}