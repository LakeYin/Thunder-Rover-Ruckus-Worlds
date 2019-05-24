package org.firstinspires.ftc.teamcode.autonomous.tasks;

public class Align90Task implements Task {
    @Override
    public void run() throws InterruptedException {
        int currentHdg = RotateByIMUTask.readHeading();
        int targetHdg = 90;

        RotateByIMUTask.rotate(currentHdg - targetHdg);
    }
}
