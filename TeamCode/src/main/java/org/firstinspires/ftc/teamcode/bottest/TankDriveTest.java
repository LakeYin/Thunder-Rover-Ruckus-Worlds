package org.firstinspires.ftc.teamcode.bottest;

import com.andoverrobotics.core.drivetrain.DriveTrain;
import com.andoverrobotics.core.drivetrain.TankDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@TeleOp(name = "TankDriveTest_TeleOp", group = "ARC")
public class TankDriveTest extends OpMode {

  private DriveTrain drive;

  @Override
  public void init() {
    DcMotor motorL = hardwareMap.dcMotor.get("motorL");
    DcMotor motorR = hardwareMap.dcMotor.get("motorR");
    motorR.setDirection(Direction.REVERSE);

    drive = TankDrive.fromMotors(motorL, motorR, this, 1, 1);
  }

  @Override
  public void loop() {
    drive.setMovementAndRotation(-gamepad1.left_stick_y, gamepad1.left_stick_x);

    telemetry.addData("Left stick y", -gamepad1.left_stick_y);
    telemetry.addData("Left stick x", gamepad1.left_stick_x);
    telemetry.update();
  }
}
