package org.firstinspires.ftc.teamcode.bottest;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

@TeleOp(name = "SixServoTest", group = "ARC")
public class SixServoTest extends ServoTester {

    @Override
    public void init() {
    /*DcMotor motorL = hardwareMap.dcMotor.get("motorL");
    DcMotor motorR = hardwareMap.dcMotor.get("motorR");
    motorR.setDirection(Direction.REVERSE);*/

        servos = new ArrayList<Servo>();
        servos.add(hardwareMap.servo.get("servo0"));
        servos.add(hardwareMap.servo.get("servo1"));
        servos.add(hardwareMap.servo.get("servo2"));
        servos.add(hardwareMap.servo.get("servo3"));
        servos.add(hardwareMap.servo.get("servo4"));
        servos.add(hardwareMap.servo.get("servo5"));

        //drive = TankDrive.fromMotors(motorL, motorR, this, 1, 1);
    }
}
