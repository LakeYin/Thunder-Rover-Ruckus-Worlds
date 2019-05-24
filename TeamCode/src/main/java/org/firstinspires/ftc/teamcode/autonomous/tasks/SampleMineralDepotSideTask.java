package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RunToPosition;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.*;

public class SampleMineralDepotSideTask extends SampleMineralTask {
    public SampleMineralDepotSideTask(HardwareMap map) {
        super(map);
    }

    @Override
    public void run() {

        tellemReadMinerals();
        detectAsNecessary();
        tellemReadMinerals();

        RotateByIMUTask.rotate(-90);

        if (AutonomousBot.rightMineral.orElse(SILVER) == GOLD) {
            switchRight();
            knockMineral();
            rotateLeft();
            new TeamMarkerTask(1).run();
            Bot.getInstance().intake.retractFully().begin().waitUntilDone();
            rotateRight();
            unknockMineral();
            switchLeft();
        } else if (AutonomousBot.centerMineral.orElse(SILVER) == GOLD) {
            knockMineral();
            Bot.getInstance().drivetrain.driveForwards(12);
            new TeamMarkerTask(0.8).run();
            RunToPosition pos = Bot.getInstance().intake.retractFully().begin();
            Bot.getInstance().drivetrain.driveBackwards(12);
            unknockMineral();
            pos.waitUntilDone();
        } else {
            switchLeft();
            knockMineral();
            rotateRight();
            new TeamMarkerTask(1).run();
            Bot.getInstance().intake.retractFully().begin().waitUntilDone();
            rotateLeft();
            unknockMineral();
            switchRight();
        }
    }

    protected void knockMineral() {
        Bot.getInstance().drivetrain.driveForwards(25);
    }

    protected void unknockMineral() {
        Bot.getInstance().drivetrain.driveBackwards(25);
    }

    protected void rotateLeft() {
        RotateByIMUTask.rotate(-25);
    }
    protected void rotateRight() {
        RotateByIMUTask.rotate(25);
    }
}
