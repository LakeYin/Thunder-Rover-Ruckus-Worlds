package com.andoverrobotics.core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public interface DriveTrain {

    void setDefaultDrivePower(double power);
    double getDefaultDrivePower();

    // -- Autonomous Methods --

    // [0, 1]
    void driveForwards(double distanceInInches);
    void driveForwards(double distanceInInches, double power);

    // [0, 1]
    void driveBackwards(double distanceInInches);
    void driveBackwards(double distanceInInches, double power);

    // TODO radians <-> degrees
    // [0, 360]
    void rotateClockwise(int degrees);
    void rotateCounterClockwise(int degrees);

    // -- Teleop Methods --

    // [-1, 1]
    void setMovementPower(double power);
    // [-1, 1]
    void setRotationPower(double power);



}
