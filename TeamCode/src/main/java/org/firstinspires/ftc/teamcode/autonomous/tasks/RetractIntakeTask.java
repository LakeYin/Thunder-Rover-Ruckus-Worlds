package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class RetractIntakeTask implements Task {
    @Override
    public void run() throws InterruptedException {
        Bot.getInstance().intake.retractFully().begin().waitUntilDone();
    }
}
