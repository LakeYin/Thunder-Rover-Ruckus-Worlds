package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;


@TeleOp(name = "ServoDemo", group = "ARC")
public class ServoDemo extends OpMode {

  //private DriveTrain drive;
  protected ArrayList<Servo> servos;
  private double servoPosition = -10;
  private double increment = 0.01;
  protected Servo servo;

  @Override
  public void init() {
    /*DcMotor motorL = hardwareMap.dcMotor.get("motorL");
    DcMotor motorR = hardwareMap.dcMotor.get("motorR");
    motorR.setDirection(Direction.REVERSE);*/

    servos = new ArrayList<Servo>();
    servo = hardwareMap.servo.get("servo0");
    //servos.add(hardwareMap.servo.get("servo0"));
    /*servos.add(hardwareMap.servo.get("servo1"));
    servos.add(hardwareMap.servo.get("servo2"));
    servos.add(hardwareMap.servo.get("servo3"));
    servos.add(hardwareMap.servo.get("servo4"));
    servos.add(hardwareMap.servo.get("servo5"));*/

    //drive = TankDrive.fromMotors(motorL, motorR, this, 1, 1);
  }

  @Override
  public void loop() {
    /*drive.setMovementAndRotation(-gamepad1.left_stick_y, gamepad1.left_stick_x);
    telemetry.addData("Left stick y", -gamepad1.left_stick_y);
    telemetry.addData("Left stick x", gamepad1.left_stick_x);
    telemetry.update();*/

    double position = servo.getPosition() + (gamepad1.left_trigger * 0.01) - (gamepad1.right_trigger * 0.01);
    servo.setPosition(position);
    telemetry.addData("Position", position);
  }

}