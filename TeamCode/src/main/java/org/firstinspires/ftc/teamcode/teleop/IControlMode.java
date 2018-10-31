package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

public interface IControlMode {

    void apply(Gamepad gamepad);

    boolean shouldBeActivated(Gamepad gamepad);
}
