package com.andoverrobotics.core.config;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class Configuration {

  private final Map<String, String> map;

  private Configuration(Map<String, String> map) {
    this.map = map;
  }

  public static Configuration from(Map<String, String> map) {
    return new Configuration(map);
  }

  public static Configuration fromProperties(Reader file)
      throws IOException {

    Properties props = new Properties();
    props.load(file);
    // Java's sloppiness here. Properties was done in a rush and wasn't adapted to generics.
    return Configuration.from(new HashMap(props));
  }

  public <T> T loadToSchema(T schemaInstance) {
    Class<?> schemaClass = schemaInstance.getClass();
    try {
      for (Field field : schemaClass.getDeclaredFields()) {
        populateSchemaField(schemaInstance, field);
      }
      return schemaInstance;
    } catch (Exception reflectionError) {
      throw new InvalidSchemaError(schemaClass, reflectionError);
    }
  }

  public int getInt(String key) {
    String strValue = getString(key);
    try {
      return Integer.parseInt(strValue);
    } catch (NumberFormatException numberException) {
      throw new InvalidFormatError(key, strValue, "int");
    }
  }

  public double getDouble(String key) {
    String strValue = getString(key);
    try {
      return Double.parseDouble(strValue);
    } catch (NumberFormatException numberException) {
      throw new InvalidFormatError(key, strValue, "double");
    }
  }

  public boolean getBoolean(String key) {
    String value = getString(key);
    if (value.equalsIgnoreCase("true")) {
      return true;
    } else if (value.equalsIgnoreCase("false")) {
      return false;
    } else {
      throw new InvalidFormatError(key, value, "boolean");
    }
  }

  public String getString(String key) {
    String value = map.get(key);
    if (value == null) {
      throw new NoSuchFieldError(key);
    }
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Configuration that = (Configuration) o;
    return Objects.equals(map, that.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(map);
  }

  private <T> void populateSchemaField(T schemaInstance, Field field)
      throws IllegalAccessException {

    String fieldName = field.getName();
    String typeName = field.getType().getCanonicalName();

    switch (typeName) {
      case "int":
        field.setInt(schemaInstance, getInt(fieldName));
        break;
      case "double":
        field.setDouble(schemaInstance, getDouble(fieldName));
        break;
      case "boolean":
        field.setBoolean(schemaInstance, getBoolean(fieldName));
        break;
      case "java.lang.String":
        field.set(schemaInstance, getString(fieldName));
        break;
      default:
        throw new InvalidSchemaError(schemaInstance.getClass(),
            new RuntimeException("Invalid type: " + typeName));
    }
  }

}
