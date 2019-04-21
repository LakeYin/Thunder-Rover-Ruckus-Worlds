package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Diagnosable {

  public static void addAll(Telemetry telemetry, Diagnosable... modules) {
    for (Diagnosable module : modules) {
      module.addData(telemetry);
    }
  }

  public abstract void addData(Telemetry telemetry);

  protected void addMotorData(Telemetry telemetry, String name, DcMotor motor) {
    telemetry.addData(name, "power=%.3f currPos=%d targetPos=%d mode=%s",
        motor.getPower(), motor.getCurrentPosition(), motor.getTargetPosition(), motor.getMode());
  }

  protected void addServoData(Telemetry telemetry, String name, Servo servo) {
    telemetry.addData(name, "pos=%.3f", servo.getPosition());
  }
}
