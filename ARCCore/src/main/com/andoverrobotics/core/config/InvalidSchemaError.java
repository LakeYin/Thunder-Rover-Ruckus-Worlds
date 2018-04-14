package com.andoverrobotics.core.config;

public class InvalidSchemaError extends RuntimeException {

  InvalidSchemaError(Class<?> schemaClass, Exception problem) {
    super(schemaClass.getCanonicalName(), problem);
  }
}
