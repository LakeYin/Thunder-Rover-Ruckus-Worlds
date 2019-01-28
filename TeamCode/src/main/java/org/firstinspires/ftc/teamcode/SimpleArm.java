package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

public class SimpleArm {
  public final DcMotor liftMotor;

  public SimpleArm(DcMotor liftMotor) {
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    this.liftMotor = liftMotor;
  }

  public void setLiftPower(double power) {
    liftMotor.setPower(power);
  }

}
