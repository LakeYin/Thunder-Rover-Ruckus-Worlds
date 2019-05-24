package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

import java.io.IOException;

@Autonomous(name = "ResetDemo", group = "ARC")
public class ResetDemo extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            Bot bot = new Bot(hardwareMap, telemetry, hardwareMap.appContext, this);
            waitForStart();
            bot.intake.retractFully().begin().waitUntilDone();
            bot.hookLift.lowerToBottom().begin().waitUntilDone();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
