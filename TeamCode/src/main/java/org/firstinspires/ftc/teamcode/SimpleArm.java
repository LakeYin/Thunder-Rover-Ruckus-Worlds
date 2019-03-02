package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class SimpleArm {
  public final DcMotor liftMotor;

  public SimpleArm(DcMotor liftMotor) {
    this.liftMotor = liftMotor;
  }

  public void setLiftPower(double power) {
    liftMotor.setPower(power);
  }

}
