package org.firstinspires.ftc.teamcode;

public class IntakeTest extends TankDriveTester {

  private Intake intake;

  @Override
  protected void runLoop() {
    while (opModeIsActive()) {
      // TODO complete
    }
  }

  @Override
  protected void setup() {
    intake = new Intake(hardwareMap.dcMotor.get("intakeSlide"),
        hardwareMap.servo.get("intakeOrientator"),
        hardwareMap.crservo.get("intakeSweeper"), this);
  }
}
