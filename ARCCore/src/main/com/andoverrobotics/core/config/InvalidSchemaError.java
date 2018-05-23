package com.andoverrobotics.core.config;

/**
 * Error indicating that a Schema given to {@link Configuration#loadToSchema} is not suitable for
 * loading. The detailed cause for this error is retrievable with {@link Throwable#getCause()}.
 *
 * @see Configuration
 */
public class InvalidSchemaError extends RuntimeException {

  InvalidSchemaError(Class<?> schemaClass, Exception problem) {
    super(schemaClass.getCanonicalName(), problem);
  }
}
