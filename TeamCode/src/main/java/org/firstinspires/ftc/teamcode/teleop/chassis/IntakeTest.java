package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Intake;

@TeleOp(name = "Intake Tester", group = "ARC")
public class IntakeTest extends LinearOpMode {

    private Intake intake;
    private DcMotor slideMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        setup();
        waitForStart();
        while (opModeIsActive()) {
            controlIntake(gamepad1);
        }
    }

    private boolean intakeIsExtended = false;

    private void controlIntake(Gamepad gamepad) throws InterruptedException {
        //-*intake.controlSlidesManually((booleanToInt(gamepad.dpad_left) - booleanToInt(gamepad.dpad_right)));

        if(gamepad1.right_trigger >= 0.25)
            slideMotor.setPower(gamepad1.right_trigger);
        else if (gamepad1.left_trigger >= 0.25)
            slideMotor.setPower(-gamepad1.left_trigger);
        else
            slideMotor.setPower(0);

//        if (gamepad.a) {
//            if (intakeIsExtended) {
////                intake.orientToTransit();
//                intake.retractFully();
//            } else {
//                intake.extendFully();
//            }
//            intakeIsExtended = !intakeIsExtended;
//        }
//        if (gamepad.b) {
//            intake.orientToCollect();
//        } else if (gamepad.x) {
//            intake.orientToTransfer();
//        } else if (gamepad.y) {
//            intake.orientToTransit();
//        }
    }

    public void setup() {
        /*intake = new Intake(hardwareMap.dcMotor.get("intakeSlide"),
                *//*hardwareMap.servo.get("intakeOrientator")*//* null,
                *//*hardwareMap.crservo.get("intakeSweeper")*//*null , this);*/
        slideMotor = hardwareMap.dcMotor.get("intakeSlide");
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }
}
