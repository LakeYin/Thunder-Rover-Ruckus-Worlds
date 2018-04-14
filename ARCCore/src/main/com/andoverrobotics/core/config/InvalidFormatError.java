package com.andoverrobotics.core.config;

public class InvalidFormatError extends RuntimeException {

  InvalidFormatError(String key, String value, String expectedFormat) {
    super(String.format("%s (%s) not a %s", key, value, expectedFormat));
  }
}
