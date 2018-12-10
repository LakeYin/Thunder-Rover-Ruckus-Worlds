package org.firstinspires.ftc.teamcode.simulation;

import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.andoverrobotics.core.utilities.Coordinate;
import com.andoverrobotics.core.utilities.IMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class SimDriveTrain extends StrafingDriveTrain {

  public SimDriveTrain(OpMode opMode) {
    super(opMode);
  }

  @Override
  public void strafeInches(Coordinate inchOffset, double power) {
    SimulationRelay.relayString("drivetrain:strafe:%f:%f:%f",
        inchOffset.getX(), inchOffset.getY(), power);
  }

  @Override
  public void setStrafe(Coordinate direction, double power) {
    SimulationRelay.relayString("drivetrain:setStrafe:%f:%f",
        direction.getPolarDirection(), power);
  }

  @Override
  public void rotateClockwise(int degrees, double power) {
    SimulationRelay.relayString("drivetrain:rotate:%d:%f", degrees, power);
  }

  @Override
  public void rotateCounterClockwise(int degrees, double power) {
    rotateClockwise(-degrees, power);
  }

  @Override
  public void setRotationPower(double power) {
    SimulationRelay.relayString("drivetrain:setRotate:%f", power);
  }

  @Override
  public void setMovementAndRotation(double movePower, double rotatePower) {
    SimulationRelay.relayString("drivetrain:moveRotate:%f:%f", movePower, rotatePower);
  }

  @Override
  protected IMotor[] getMotors() {
    return new IMotor[0];
  }
}
