package com.andoverrobotics.core;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public interface MecanumDriveTrain extends DriveTrain {

    // [-1, 1]
    void strafeRight(double distanceInInches);
    void strafeRight(double distanceInInches, double power);

    // [-1, 1]
    void strafeLeft(double distanceInInches);
    void strafeLeft(double distanceInInches, double power);

    // [-1, 1]
    void strafeToCoordinate(double xInInches, double yInInches);
    void strafeToCoordinate(double xInInches, double yInInches, double power);

    // [0, 360]
    // [-1, 1]
    void strafeDegrees(int degrees, double distanceInInches);
    void strafeDegrees(int degrees, double distanceInInches, double power);

    // -- Teleop Methods --

    // [0, 360]
    // [-1, 1]
    void setDegreeOfStrafe(int degrees);
    void setDegreeOfStrafe(int degrees, double power);
}
