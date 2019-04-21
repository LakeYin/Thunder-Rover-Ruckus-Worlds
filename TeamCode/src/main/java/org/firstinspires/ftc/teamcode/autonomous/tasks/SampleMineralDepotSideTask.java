package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.GOLD;
import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.SILVER;

public class SampleMineralDepotSideTask extends SampleMineralTask {
    public SampleMineralDepotSideTask(HardwareMap map) {
        super(map);
    }

    @Override
    public void run() {

        tellemReadMinerals();
        detectAsNecessary();
        tellemReadMinerals();

        Bot.getInstance().drivetrain.rotateCounterClockwise(90);

        if (AutonomousBot.centerMineral.orElse(SILVER) == GOLD) {
            knockMineral();
            new TeamMarkerTask(0.85).run();
            unknockMineral();
        } else if (AutonomousBot.rightMineral.orElse(SILVER) == GOLD) {
            switchRight();
            knockMineral();
            rotateLeft();
            new TeamMarkerTask(0.8).run();
            rotateRight();
            unknockMineral();
            switchLeft();
        } else {
            switchLeft();
            knockMineral();
            rotateRight();
            new TeamMarkerTask(0.8).run();
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
        Bot.getInstance().drivetrain.rotateCounterClockwise(20);
    }
    protected void rotateRight() {
        Bot.getInstance().drivetrain.rotateClockwise(20);
    }
}
