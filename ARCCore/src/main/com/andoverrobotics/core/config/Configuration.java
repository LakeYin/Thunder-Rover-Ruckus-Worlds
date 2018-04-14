package com.andoverrobotics.core.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * A key-value mapping abstraction that allows for the retrieval of values in a variety of types.
 * <p> <h2>Schemas</h2> Configuration Schemas are small classes that only contain non-private fields
 * that are declared in compatible types. The following is an example:
 * <pre>{@code
 *   class ExampleSchema {
 *     int numberOfBlocks;
 *     double servoPositionAtRest;
 *     boolean usePredefinedAutonomous;
 *     String selectedProcedureName;
 *   }
 * }</pre>
 * <p> Each field in a schema corresponds to an entry in the configuration map with the same name.
 * For the example above, a valid configuration map would be as follows, formatted in Properties
 * style:
 * <pre>{@code
 *   numberOfBlocks=4
 *   servoPositionAtRest=0.51
 *   usePredefinedAutonomous=false
 *   selectedProcedureName=auto2
 * }</pre>
 * <p>
 *
 * @see InvalidFormatError
 * @see InvalidSchemaError
 */
public final class Configuration {

  private static final String PROPERTIES_DIRECTORY = "/storage/self/primary/FIRST/config";

  private final Map<String, String> map;

  private Configuration(Map<String, String> map) {
    this.map = map;
  }

  /**
   * Reads a file with the given name from the standard directory for configuration file storage,
   * parses it into {@link Properties} format, and returns a new instance of {@link Configuration}
   * with the parsed mapping.
   * @param fileName Name of the file to be read
   * @return The Configuration instance whose entries have been read from the given file
   * @throws IOException If the file with the given name cannot be read
   */
  public static Configuration fromPropertiesFile(String fileName)
      throws IOException {
    return fromProperties(new FileReader(new File(PROPERTIES_DIRECTORY, fileName)));
  }
  /**
   * Parses data from the given {@link Reader} in Java Properties format, and then creates a new
   * instance of {@link Configuration} from the parsed result.
   * @param file The Reader from which to parse data
   * @return The new Configuration instance
   * @throws IOException if an error is encountered while reading from the given Reader
   */
  public static Configuration fromProperties(Reader file)
      throws IOException {

    Properties props = new Properties();
    props.load(file);
    // Java's sloppiness here. Properties was done in a rush and wasn't adapted to generics.
    return Configuration.from(new HashMap(props));
  }

  /**
   * Creates a new instance of {@link Configuration} from the given String-to-String {@link Map}.
   *
   * @param map The map to apply to the new Configuration instance
   * @return The new Configuration instance
   */
  public static Configuration from(Map<String, String> map) {
    return new Configuration(map);
  }

  /**
   * Loads the entries from this Configuration to the given Schema instance.
   * @param schemaInstance Configuration Schema instance into which the entries are loaded
   * @param <T> Type of the given Schema instance
   * @return The given Schema instance with its fields populated
   * @throws InvalidSchemaError if the type of the given Schema instance is not suitable for loading
   */
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

  /**
   * Retrieves the value of the given key in the mapping, parsed as an integer.
   * @param key The key for the requesting value
   * @return The parsed integer represented by the key's corresponding value
   * @throws InvalidFormatError if the given key's corresponding value cannot be parsed as an integer
   * @throws NoSuchFieldError if the given key does not exist in this Configuration instance's mapping
   */
  public int getInt(String key) {
    String strValue = getString(key);
    try {
      return Integer.parseInt(strValue);
    } catch (NumberFormatException numberException) {
      throw new InvalidFormatError(key, strValue, "int");
    }
  }

  /**
   * Retrieves the value of the given key in the mapping, parsed as a double.
   * @param key The key for the requesting value
   * @return The parsed double represented by the key's corresponding value
   * @throws InvalidFormatError if the given key's corresponding value cannot be parsed as a double
   * @throws NoSuchFieldError if the given key does not exist in this Configuration instance's mapping
   */
  public double getDouble(String key) {
    String strValue = getString(key);
    try {
      return Double.parseDouble(strValue);
    } catch (NumberFormatException numberException) {
      throw new InvalidFormatError(key, strValue, "double");
    }
  }

  /**
   * Retrieves the value of the given key in the mapping, parsed as a boolean. Accepted values are
   * <code>"true"</code> and <code>"false"</code>, case-insensitive.
   * @param key The key for the requesting value
   * @return The parsed boolean represented by the key's corresponding value
   * @throws InvalidFormatError if the given key's corresponding value is not an accepted value
   * @throws NoSuchFieldError if the given key does not exist in this Configuration instance's mapping
   */
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

  /**
   * Retrieves the value of the given key in the mapping.
   * @param key The key for the requesting value
   * @return The key's corresponding value
   * @throws NoSuchFieldError if the given key does not exist in this mapping
   */
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
            new UnsupportedOperationException("Invalid type: " + typeName));
    }
  }

}
