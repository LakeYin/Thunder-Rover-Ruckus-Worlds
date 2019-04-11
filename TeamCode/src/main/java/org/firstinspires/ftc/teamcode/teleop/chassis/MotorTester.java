package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Intake;

@TeleOp(name = "Motor Tester", group = "ARC")

public class MotorTester extends LinearOpMode {

    private DcMotor anyMotor;

    public void runOpMode()throws InterruptedException {
        setup();
        waitForStart();
        while (opModeIsActive()) {
            controlMotor(gamepad1);
        }
    }

    public void controlMotor(Gamepad gamepad) throws InterruptedException{
        if(gamepad1.right_trigger >= 0.25)
            anyMotor.setPower(0.2);
        else if (gamepad1.left_trigger >= 0.25)
            anyMotor.setPower(-0.2);
        else
            anyMotor.setPower(0);
    }

    public void setup(){
        anyMotor = hardwareMap.dcMotor.get("anyMotor");
        anyMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
