package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Arm extends SimpleArm {

  private final Servo lateralServo;
  private final CRServo verticalServo;

  public Arm(DcMotor liftMotor, Servo grabber, Servo lateralServo, CRServo verticalServo,
      double openPos, double closedPos) {
    super(liftMotor, grabber, openPos, closedPos);
    this.lateralServo = lateralServo;
    this.verticalServo = verticalServo;
  }

  public void rotateLateral(double offset) {
    lateralServo.setPosition(Range.clip(lateralServo.getPosition() + offset, 0, 1));
  }

  public void rotateVerticalByPower(double power) {
    verticalServo.setPower(power);
  }
}
