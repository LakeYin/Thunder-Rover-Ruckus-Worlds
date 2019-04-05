package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Second Chassis Demo", group = "ARC")
public class SecondChassisDemo extends LinearOpMode {

  private DcMotor intakeSlides, depositSlides;
  private Servo bucket, sorter;
  private CRServo sweeper;

  @Override
  public void runOpMode() throws InterruptedException {
    intakeSlides = hardwareMap.dcMotor.get("intake");
    depositSlides = hardwareMap.dcMotor.get("deposit");
    bucket = hardwareMap.servo.get("bucket");
    bucket.setPosition(0.5);
    sorter = hardwareMap.servo.get("sorter");
    sweeper = hardwareMap.crservo.get("sweeper");
    sorter.setPosition(0.5);

    waitForStart();

    while (opModeIsActive()) {
      float left_stick_y = -gamepad1.left_stick_y;
      intakeSlides.setPower(left_stick_y > 0 ? left_stick_y : left_stick_y * 0.2);

      depositSlides.setPower(-gamepad1.right_stick_y);

      bucket.setPosition(bucket.getPosition() + (gamepad1.y ? 0.01 : 0) + (gamepad1.a ? -0.01 : 0));
      sorter.setPosition(sorter.getPosition() + (gamepad1.dpad_up ? 0.01 : 0) + (gamepad1.dpad_down ? -0.01 : 0));
      sweeper.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

      telemetry.addData("data", "intakePower(left y)=%f depositPower(right y)=%f bucket(y/a)=%f sorter(dpad)=%f sweeper(triggers)=%f",
          intakeSlides.getPower(), depositSlides.getPower(), bucket.getPosition(), sorter.getPosition(), sweeper.getPower());
      telemetry.update();
    }
  }
}
