package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.*;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ChassisBot;
import org.firstinspires.ftc.teamcode.teleop.TeleOpState;

import java.io.IOException;

@TeleOp(name = "Main TeleOp", group = "ARC")
public class TeleOpLeapFrog extends LinearOpMode{

    private ChassisBot bot;
    private TeleOpState state = MANUAL;

    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        while (opModeIsActive()) {

            controlDrivetrain(gamepad1);
            if (gamepad2.back) {
                stopAllMotion();
            }

            telemetry.addData("Connection Keep-Alive", getRuntime());
            telemetry.addData("State", state);
            addPowerDrawDebug();
            telemetry.update();
        }
    }

    private void stopAllMotion() {
        bot.drivetrain.stop();
        bot.hub2.setLedColor(255, 0, 0);
        bot.hub7.setLedColor(255, 0, 0);
    }

    private void initialize() {
        try {
            bot = new ChassisBot(hardwareMap, telemetry, hardwareMap.appContext, this);
        } catch (IOException e) {
            stop();
            e.printStackTrace();
        }
    }

    private void controlDrivetrain(Gamepad gamepad) {
        Coordinate strafe = getLeftDrivetrainTarget(gamepad)
                .add(getMicroAdjustCoord(gamepad).multiply(0.7));
        double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.35;
        bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
                strafe.getPolarDistance());

        if (gamepad.start) {
            bot.drivetrain.memorizeCurrentPosition();
        } else if (gamepad.back) {
            bot.drivetrain.goToMemorizedPosition(0.6);
        }
    }

    private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
        return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
    }

    private void addPowerDrawDebug() {
        telemetry.addData("Hub 2 total current draw", bot.hub2.getTotalModuleCurrentDraw());
        telemetry.addData("Hub 7 total current draw", bot.hub7.getTotalModuleCurrentDraw());
    }

    private static Coordinate getMicroAdjustCoord(Gamepad gamepad) {
        double x = booleanToInt(gamepad.dpad_right) - booleanToInt(gamepad.dpad_left);
        double y = booleanToInt(gamepad.dpad_up) - booleanToInt(gamepad.dpad_down);
        return Coordinate.fromXY(x * 1.4, y);
    }

    private static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }
}
