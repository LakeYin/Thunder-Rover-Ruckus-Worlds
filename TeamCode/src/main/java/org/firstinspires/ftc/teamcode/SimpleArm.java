package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;

public class SimpleArm {
  protected final DcMotor liftMotor;
  protected final Servo grabber;

  private final double openPos, closedPos;

  public SimpleArm(DcMotor liftMotor, Servo grabber, double openPos, double closedPos) {
    this.openPos = openPos;
    this.closedPos = closedPos;
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    this.liftMotor = liftMotor;
    this.grabber = grabber;
  }

  public void setLiftPower(double power) {
    liftMotor.setPower(power);
  }

  public void moveLiftToPosition(int posOffset, double power) {
    liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    liftMotor.setMode(RunMode.RUN_USING_ENCODER);

    liftMotor.setTargetPosition(posOffset);
    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setPower(power);

    while (liftMotor.isBusy());

    liftMotor.setPower(0);
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
  }

  public void openGrabber() {
    setGrabberPosition(openPos);
  }

  public void closeGrabber() {
    setGrabberPosition(closedPos);
  }

  public void moveGrabber(double offset) {
    setGrabberPosition(getGrabberPosition() + offset);
  }

  public void setGrabberPosition(double position) {
    grabber.setPosition(position);
  }

  public double getGrabberPosition() {
    return grabber.getPosition();
  }
}
