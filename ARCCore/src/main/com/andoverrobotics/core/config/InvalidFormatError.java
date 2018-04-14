package com.andoverrobotics.core.config;

/**
 * Error indicating that a {@link Configuration} value cannot be parsed into a specified type. <p>
 * The <code>detailMessage</code> of an instance indicates the key, the mis-formatted value, and the
 * format into which it was attempted to be parsed.
 *
 * @see Configuration
 */
public class InvalidFormatError extends RuntimeException {

  InvalidFormatError(String key, String value, String expectedFormat) {
    super(String.format("%s (%s) not a %s", key, value, expectedFormat));
  }
}
