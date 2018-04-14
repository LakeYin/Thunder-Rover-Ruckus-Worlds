package com.andoverrobotics.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ConfigurationTest {

  // Must be static; otherwise, it will contain a "declared" field named "this",
  //   referring to ConfigurationTest.
  static class TestSchema {

    int autonomousTrials;
    double motorSpeed;
    boolean useEncoders;
    boolean useSensors;
    String robotName;
  }

  static class BadTestSchema {

    double motorSpeed;
    boolean useEncoders;
    float badType;
  }

  private static final Map<String, String> schemaProvider = new HashMap<String, String>() {{
    put("autonomousTrials", "4");
    put("motorSpeed", "0.412");
    put("useEncoders", "false");
    put("useSensors", "true");
    put("robotName", "ConfigTestBot");
  }};
  private static final String schemaDataFileContents =
      "autonomousTrials=4\n" +
          "motorSpeed=0.412\n" +
          "useEncoders=false\n" +
          "useSensors=true\n" +
          "robotName=ConfigTestBot";
  private static final Configuration config = Configuration.from(schemaProvider);

  @Test
  public void fromPropertiesReader_givenProperties_returnsCorrectly() throws IOException {
    Configuration stringConfig =
        Configuration.fromProperties(new StringReader(schemaDataFileContents));
    assertEquals(config, stringConfig);
  }

  // NORMAL COURSE /////

  @Test
  public void getInt_givenIntKey_returnsCorrectValue() {
    assertEquals(4,
        config.getInt("autonomousTrials"));
  }

  @Test
  public void getDouble_givenDoubleKey_returnsCorrectValue() {
    assertEquals(0.412,
        config.getDouble("motorSpeed"), 1e-8);
  }

  @Test
  public void getBoolean_givenBooleanKey_returnsCorrectValue() {
    assertFalse(config.getBoolean("useEncoders"));
  }

  @Test
  public void getString_givenKey_returnsCorrectValue() {
    assertEquals("ConfigTestBot",
        config.getString("robotName"));
  }

  // BAD FORMAT COURSE /////

  @Test(expected = InvalidFormatError.class)
  public void getInt_givenFloatKey_throwsException() {
    config.getInt("motorSpeed");
  }

  @Test(expected = InvalidFormatError.class)
  public void getDouble_givenBooleanKey_throwsException() {
    config.getDouble("useEncoders");
  }

  @Test(expected = InvalidFormatError.class)
  public void getBoolean_givenIntKey_throwsException() {
    config.getBoolean("autonomousTrials");
  }

  // NONEXISTENT FIELD COURSE /////

  @Test(expected = NoSuchFieldError.class)
  public void get_givenInvalidKey_throwsException() {
    config.getString("badKey");
  }

  @Test
  public void fromMapToSchema_givenProvider_loadsCorrectValues() {
    TestSchema config = Configuration.from(schemaProvider).loadToSchema(new TestSchema());

    assertEquals(4, config.autonomousTrials);
    assertEquals(0.412, config.motorSpeed, 1e-5);
    assertFalse(config.useEncoders);
    assertTrue(config.useSensors);
    assertEquals("ConfigTestBot", config.robotName);
  }

  @Test
  public void equals_givenEqualConfigs_returnsTrue() {
    Configuration config1 = Configuration.from(schemaProvider);
    assertEquals(config1, config);
  }

  @Test(expected = InvalidSchemaError.class)
  public void fromMapToSchema_givenBadSchema_throwsException() {
    Configuration.from(schemaProvider).loadToSchema(new BadTestSchema());
  }
}