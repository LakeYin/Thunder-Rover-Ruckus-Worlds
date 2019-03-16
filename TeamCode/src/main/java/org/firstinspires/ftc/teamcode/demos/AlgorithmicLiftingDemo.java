package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.teleop.TeleOpTaskHost;

@Autonomous(name = "AlgorithmicLiftingDemo", group = "ARC Lightning")
public class AlgorithmicLiftingDemo extends LinearOpMode {

  private double a = 0.266, b = 0.06;

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotorEx leftLift = hardwareMap.get(DcMotorEx.class, "leftLift"),
        rightLift = hardwareMap.get(DcMotorEx.class, "rightLift");
    setupMotors(leftLift, rightLift);

    telemetry.addData("Ensure", "both arms at collection position and fully extended");
    telemetry.update();

    waitForStart();

    strategy3(leftLift, rightLift);
  }

  private void strategy1(DcMotorEx leftLift, DcMotorEx rightLift) {
    while (opModeIsActive()) {
      double angle = angle(
          (int) ((leftLift.getCurrentPosition() + rightLift.getCurrentPosition()) / 2.0)),
          power = desiredPower(-angle);

      if (angle > 2.18) break;

      leftLift.setPower(power);
      rightLift.setPower(power);

      a += gamepad1.left_stick_y * -0.01;
      b += gamepad1.right_stick_y * -0.01;

      telemetry.addData("angle", "%.2f", angle);
      telemetry.addData("power", "%.3f", power);
      telemetry.addData("a & b", "%.2f %.2f", a, b);
      telemetry.update();
    }
  }

  private void strategy2(DcMotorEx leftLift, DcMotorEx rightLift) {
    leftLift.setMode(RunMode.RUN_TO_POSITION);
    rightLift.setMode(RunMode.RUN_TO_POSITION);
    leftLift.setTargetPosition(500);
    rightLift.setTargetPosition(500);
    while (leftLift.isBusy() || rightLift.isBusy() && opModeIsActive()) {
      leftLift.setPower((500 - leftLift.getCurrentPosition()) * 0.3);
      rightLift.setPower((500 - rightLift.getCurrentPosition()) * 0.3);

      telemetry.addData("left pos", leftLift.getCurrentPosition());
      telemetry.addData("right pos", rightLift.getCurrentPosition());
      telemetry.update();
    }
    sleep(500);
    leftLift.setPower(0);
    rightLift.setPower(0);
    leftLift.setMode(RunMode.RUN_USING_ENCODER);
    rightLift.setMode(RunMode.RUN_USING_ENCODER);
  }

  private void strategy3(DcMotorEx leftLift, DcMotorEx rightLift) {
    TeleOpTaskHost.getRaiseArm(new Arm(leftLift, null, null, 1, 0))
        .run();
    TeleOpTaskHost.getRaiseArm(new Arm(rightLift, null, null, 1, 0))
        .run();
  }

  private void setupMotors(DcMotorEx leftLift, DcMotorEx rightLift) {
    rightLift.setDirection(Direction.REVERSE);
    leftLift.setMode(RunMode.STOP_AND_RESET_ENCODER);
    rightLift.setMode(RunMode.STOP_AND_RESET_ENCODER);
    leftLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    rightLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    while (leftLift.getCurrentPosition() + rightLift.getCurrentPosition() != 0 && opModeIsActive());
    leftLift.setMode(RunMode.RUN_USING_ENCODER);
    rightLift.setMode(RunMode.RUN_USING_ENCODER);
  }

  private double angle(int ticks) {
    return ticks * Math.PI / 694.0;
  }

  private double desiredPower(double angleRadians) {
    return a * Math.cos(angleRadians) + b;
  }
}
