package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "DepositTest", group = "ARC")
public class DepositTest extends LinearOpMode {

    private final double SORTER_LOADING_POSITION = 0.4, SORTER_TRANSIT_POSITION = 0.2, SORTER_DEPOSIT_POSITION = 0.65;
    private final double SORTER_DEFAULT_POSITION = SORTER_LOADING_POSITION;

    private DcMotor slideMotor;
    private Servo sorterServo;

    public void runOpMode() {
        setup();
        waitForStart();
        while (opModeIsActive()) {
            controlDeposit(gamepad1);
        }
    }

    private void controlDeposit(Gamepad gamepad) {
        if (gamepad1.right_trigger >= 0.25)
            slideMotor.setPower(gamepad1.right_trigger);
        else if (gamepad1.left_trigger >= 0.25)
            slideMotor.setPower(-gamepad1.left_trigger);
        else
            slideMotor.setPower(0);

        double sorterPosition = SORTER_DEFAULT_POSITION;

        if(gamepad1.x)
            sorterPosition = SORTER_DEPOSIT_POSITION;
        else if (gamepad1.b)
            sorterPosition = SORTER_TRANSIT_POSITION;

        sorterServo.setPosition(sorterPosition);

    }

    protected void setup() {
        slideMotor = hardwareMap.dcMotor.get("depositSlide");
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sorterServo = hardwareMap.servo.get("sorterServo");
    }
}
