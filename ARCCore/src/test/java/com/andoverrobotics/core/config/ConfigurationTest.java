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

  // Must be static; otherwise, it will contain a "declared" field named "this",
  //   referring to ConfigurationTest.
  static class TestSchema {

    public int autonomousTrials;
    public double motorSpeed;
    public boolean useEncoders;
    public boolean useSensors;
    public String robotName;
  }

  static class BadTestSchema {

    public double motorSpeed;
    public boolean useEncoders;
    public float badType;
  }

  @Test
  public void fromPropertiesReaderGivenPropertiesReturnsCorrectly() throws IOException {
    Configuration stringConfig =
        Configuration.fromProperties(new StringReader(schemaDataFileContents));
    assertEquals(config, stringConfig);
  }

  // NORMAL COURSE /////

  @Test
  public void getIntGivenIntKeyReturnsCorrectValue() {
    assertEquals(4,
        config.getInt("autonomousTrials"));
  }

  @Test
  public void getDoubleGivenDoubleKeyReturnsCorrectValue() {
    assertEquals(0.412,
        config.getDouble("motorSpeed"), 1e-8);
  }

  @Test
  public void getBooleanGivenBooleanKeyReturnsCorrectValue() {
    assertFalse(config.getBoolean("useEncoders"));
  }

  @Test
  public void getStringGivenKeyReturnsCorrectValue() {
    assertEquals("ConfigTestBot",
        config.getString("robotName"));
  }

  // BAD FORMAT COURSE /////

  @Test(expected = InvalidFormatError.class)
  public void getIntGivenFloatKeyThrowsException() {
    config.getInt("motorSpeed");
  }

  @Test(expected = InvalidFormatError.class)
  public void getDoubleGivenBooleanKeyThrowsException() {
    config.getDouble("useEncoders");
  }

  @Test(expected = InvalidFormatError.class)
  public void getBooleanGivenIntKeyThrowsException() {
    config.getBoolean("autonomousTrials");
  }

  // NONEXISTENT FIELD COURSE /////

  @Test(expected = NoSuchFieldError.class)
  public void getGivenInvalidKeyThrowsException() {
    config.getString("badKey");
  }

  @Test
  public void fromMapToSchemaGivenProviderLoadsCorrectValues() {
    TestSchema config = Configuration.from(schemaProvider).loadToSchema(new TestSchema());

    assertEquals(4, config.autonomousTrials);
    assertEquals(0.412, config.motorSpeed, 1e-5);
    assertFalse(config.useEncoders);
    assertTrue(config.useSensors);
    assertEquals("ConfigTestBot", config.robotName);
  }

  @Test
  public void equalsGivenEqualConfigsReturnsTrue() {
    Configuration config1 = Configuration.from(schemaProvider);
    assertEquals(config1, config);
  }

  @Test(expected = InvalidSchemaError.class)
  public void fromMapToSchemaGivenBadSchemaThrowsException() {
    Configuration.from(schemaProvider).loadToSchema(new BadTestSchema());
  }
}