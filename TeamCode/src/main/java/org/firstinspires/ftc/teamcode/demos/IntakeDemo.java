package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Intake;

@TeleOp(name = "IntakeDemo", group = "ARC")
public class IntakeDemo extends OpMode {
  private Intake intake;

  public void init() {
    intake = new Intake(hardwareMap.dcMotor.get("intakeSlide"), hardwareMap.servo.get("intakeOrientator"),
        hardwareMap.crservo.get("intakeSweeper"));
  }

  public void loop() {
    if (gamepad1.a)
      intake.orientToCollect();
    if (gamepad1.b)
      intake.orientToTransit();
    if (gamepad1.y)
      intake.orientToTransfer();
    if (gamepad1.left_bumper)
      intake.runSweeperIn();
    else if (gamepad1.right_bumper)
      intake.runSweeperOut();
    else
      intake.stopSweeper();
    if (gamepad1.dpad_down)
      intake.orientator.setPosition(gamepad1.left_trigger);
    intake.controlSlidesManually(-gamepad1.left_stick_x);
    //intake.orientManually(-gamepad1.right_stick_y);
    telemetry.addData("pos", intake.orientator.getPosition());
  }
}
