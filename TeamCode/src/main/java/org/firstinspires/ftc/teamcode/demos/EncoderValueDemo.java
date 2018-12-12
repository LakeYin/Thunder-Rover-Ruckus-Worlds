package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping;
import java.util.Arrays;
import java.util.function.Consumer;

@Autonomous(name = "Encoder Value Demo", group = "ARC")
public class EncoderValueDemo extends LinearOpMode {
  // Before running, ensure that the wheels are attached to the axles tightly!

  private static final double SPEED_MAGNITUDE = 0.2;

  private DcMotor frontLeft, frontRight, backLeft, backRight;

  @Override
  public void runOpMode() throws InterruptedException {
    initDriveTrain();
    initTelemetry();

    waitForStart();

    forAllMotors(it -> it.setPower(SPEED_MAGNITUDE));
    until(gamepad1.b, this::showTickValues);
    forAllMotors(it -> it.setPower(0));

    telemetry.addLine("Record data for translation");
    telemetry.addLine("Prepare for rotation. Press A to continue");
    telemetry.update();

    until(gamepad1.a, () -> {});

    frontLeft.setPower(SPEED_MAGNITUDE);
    backLeft.setPower(SPEED_MAGNITUDE);
    frontRight.setPower(-SPEED_MAGNITUDE);
    backRight.setPower(-SPEED_MAGNITUDE);

    until (gamepad1.b, this::showTickValues);

    forAllMotors(it -> it.setPower(0));

    telemetry.addLine("Record data for rotation");
    telemetry.addLine("Press A or STOP on the robot controller to end");
    telemetry.update();

    until(gamepad1.a, () -> {});
  }

  private void until(boolean condition, Runnable toDo) {
    while (!condition && opModeIsActive()) {
      toDo.run();
    }
  }

  private void initTelemetry() {
    telemetry.setCaptionValueSeparator(" → ");
    telemetry.setAutoClear(false);
  }

  private void initDriveTrain() {
    DeviceMapping<DcMotor> motors = hardwareMap.dcMotor;

    frontLeft = motors.get("motorFL");
    frontRight = motors.get("motorFR");
    backLeft = motors.get("motorBL");
    backRight = motors.get("motorBR");

    frontLeft.setDirection(Direction.REVERSE);
    backLeft.setDirection(Direction.REVERSE);

    forAllMotors(it -> {
      it.setMode(RunMode.STOP_AND_RESET_ENCODER);
      it.setMode(RunMode.RUN_USING_ENCODER);
    });
  }

  private void forAllMotors(Consumer<DcMotor> fun) {
    Arrays.asList(frontLeft, frontRight, backLeft, backRight)
        .forEach(fun);
  }

  private void showTickValues() {
    telemetry.clearAll();
    telemetry.addData("Front left", frontLeft::getCurrentPosition);
    telemetry.addData("Front right", frontRight::getCurrentPosition);
    telemetry.addData("Back left", backLeft::getCurrentPosition);
    telemetry.addData("Back right", backRight::getCurrentPosition);
    telemetry.update();
  }
}
