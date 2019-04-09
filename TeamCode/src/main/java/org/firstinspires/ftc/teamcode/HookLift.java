package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
  private ConfigSchema schema;

  public HookLift(DcMotor liftMotor) {
    this.liftMotor = liftMotor;
    liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    loadSchema();
  }

  public RunToPosition liftToHook() {
    return new RunToPosition(liftMotor, schema.topPosition, schema.runningSpeed);
  }

  public RunToPosition lowerToBottom() {
    return new RunToPosition(liftMotor, 0, schema.runningSpeed);
  }

  public void adjust(double power) {
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    liftMotor.setPower(wouldPowerExceedBoundaries(power) ? 0 : power);
  }

  private boolean wouldPowerExceedBoundaries(double power) {
    return liftMotor.getCurrentPosition() <= 10 && power < 0 ||
        Math.abs(schema.topPosition - liftMotor.getCurrentPosition()) <= 10 && power > 0;
  }

  private void loadSchema() {
    try {
      schema = new ConfigSchema();
      Configuration.fromPropertiesFile("hookLift.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
