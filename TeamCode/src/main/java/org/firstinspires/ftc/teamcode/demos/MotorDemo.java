package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.HashMap;
import java.util.Map;

@Autonomous(name = "MotorDemo", group = "ARC Lightning")
public class MotorDemo extends LinearOpMode {

  private static final int MAX_MOTOR_COUNT = 8;
  private Map<String, DcMotor> motors = new HashMap<>();

  @Override
  public void runOpMode() {
    for (int i = 0; i < MAX_MOTOR_COUNT; ++i) {
      String motorName = "motor" + i;
      if (hardwareMap.dcMotor.contains(motorName)) {
        motors.put(motorName, hardwareMap.dcMotor.get(motorName));
      }
    }
    telemetry.addData("# of motors found", motors.size());
    telemetry.update();

    waitForStart();

    while (opModeIsActive()) {
      for (Map.Entry<String, DcMotor> entry : motors.entrySet()) {
        entry.getValue().setPower(-gamepad1.left_stick_y);
      }
    }
  }
}
