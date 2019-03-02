package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm extends SimpleArm {

  private final Servo grabber;
  private final CRServo extender;

  public Arm(DcMotor liftMotor, Servo grabber, CRServo extender, double openPos, double closedPos) {
    super(liftMotor);
    this.grabber = grabber;
    this.extender = extender;

    initScaleRange(openPos, closedPos);
  }

  private void initScaleRange(double openPos, double closedPos) {
    if (openPos > closedPos) {
      grabber.setDirection(Servo.Direction.REVERSE);
      grabber.scaleRange(1 - openPos, 1 - closedPos);
    } else {
      grabber.scaleRange(openPos, closedPos);
    }
  }

  public void openGrabber() {
    setGrabberPosition(1.0);
  }

  public void closeGrabber() {
    setGrabberPosition(0.0);
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

  public void setExtenderPower(double power) {
    extender.setPower(power * 0.5);
  }

  public void startRunningLiftToPosition(int newPosition, double speed) {
    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(newPosition);
    liftMotor.setPower(Math.abs(speed));
  }

  public boolean isLiftRunningToPosition() {
    return liftMotor.isBusy();
  }
}
