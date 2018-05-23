package com.andoverrobotics.core;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public interface MecanumDriveTrain extends DriveTrain {

    void strafeRight(double distanceInInches);
    void strafeRight(double distanceInInches, double power);

    void strafeLeft(double distanceInInches);
    void strafeLeft(double distanceInInches, double power);

    void strafeToCoordinate(double xInInches, double yInInches);
    void strafeToCoordinate(double xInInches, double yInInches, double power);

    void strafeDegrees(int degrees, double distanceInInches);
    void strafeDegrees(int degrees, double distanceInInches, double power);

    // -- Teleop Methods --

    void setDegreeOfStrafe(int degrees);
    void setDegreeOfStrafe(int degrees, double power);
}
