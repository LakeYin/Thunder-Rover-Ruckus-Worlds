package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import java.io.IOException;

public class HookLift {
  public static class ConfigSchema {
    public int topPosition;
    public double runningSpeed;
  }

  public final DcMotor liftMotor;
  private final Bot bot;
  private ConfigSchema schema;

  public HookLift(DcMotor liftMotor, Bot bot) {
    this.liftMotor = liftMotor;
    liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    this.bot = bot;
    loadSchema();
  }

  public void liftToHook() {
    runToPosition(schema.topPosition, schema.runningSpeed);
    while (liftMotor.isBusy());
    liftMotor.setPower(0);
  }

  public void lowerToBottom() {
    runToPosition(0, schema.runningSpeed);
    while (liftMotor.isBusy());
    liftMotor.setPower(0);
  }

  public void adjust(double power) {
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    liftMotor.setPower(isLiftInsideBoundaries() ? power : 0);
  }

  private boolean isLiftInsideBoundaries() {
    return liftMotor.getCurrentPosition() > 10 && Math.abs(schema.topPosition - liftMotor.getCurrentPosition()) > 10;
  }

  private void loadSchema() {
    try {
      schema = new ConfigSchema();
      Configuration.fromPropertiesFile("hookLift.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void runToPosition(int pos, double speed) {
    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(pos);
    liftMotor.setPower(Math.abs(speed));
  }
}
