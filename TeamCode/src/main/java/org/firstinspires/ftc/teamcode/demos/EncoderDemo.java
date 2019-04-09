package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Arrays;
import java.util.stream.Stream;

@Autonomous(name = "EncoderDemo", group = "ARC Lightning")
public class EncoderDemo extends LinearOpMode {

  @Override
  public void runOpMode() {
    DcMotor intakeSlides = hardwareMap.dcMotor.get("intakeSlide");
    DcMotor depositSlides = hardwareMap.dcMotor.get("depositSlide");
    Servo intakeOrientator = hardwareMap.servo.get("intakeOrientator");
    Servo depositOrientator = hardwareMap.servo.get("depositOrientator");
    CRServo sweeper = hardwareMap.crservo.get("intakeSweeper");
    intakeOrientator.setPosition(0);
    depositOrientator.setPosition(.5);

    waitForStart();

    while (opModeIsActive()) {
      intakeSlides.setPower(intakePower(gamepad1.left_stick_x));
      depositSlides.setPower(-gamepad1.right_stick_y);
      intakeOrientator.setPosition(intakeOrientator.getPosition() - gamepad1.left_stick_y * 0.008);
      depositOrientator
          .setPosition(depositOrientator.getPosition() + gamepad1.right_stick_x * 0.008);
      sweeper.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

      telemetry.addData("Time passed", getRuntime())
          .addData("intake[left x]", "power=%f pos=%d", intakeSlides.getPower(),
              intakeSlides.getCurrentPosition())
          .addData("score[right y]", "power=%f pos=%d", depositSlides.getPower(),
              depositSlides.getCurrentPosition())
          .addData("bucket[left y]", "pos=%f", intakeOrientator.getPosition())
          .addData("score[right x]", "pos=%f", depositOrientator.getPosition());

      telemetry.update();
    }
  }

  private double intakePower(float left_stick_x) {
    return left_stick_x > 0 ? -left_stick_x : left_stick_x * -0.2;
  }
}
