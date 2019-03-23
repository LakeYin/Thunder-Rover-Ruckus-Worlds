package org.firstinspires.ftc.teamcode.teleop.chassis;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.teleop.TeleOpMode;

@TeleOp(name = "IntakeTest", group = "ARC")
public class IntakeTest extends TankDriveTester {

  private Intake intake;

  @Override
  protected void runLoop() throws InterruptedException {
    while (opModeIsActive()) {
      controlIntake(gamepad1);
    }
  }

  private boolean intakeIsExtended = false;

  private void controlIntake(Gamepad gamepad) throws InterruptedException {
    intake.controlSlidesManually((booleanToInt(gamepad.dpad_left) - booleanToInt(gamepad.dpad_right)));
    if(gamepad.a) {
      if(intakeIsExtended) {
        intake.orientToTransit();
        intake.retractFully();
      }
      else {
        intake.extendFully();
      }
      intakeIsExtended = !intakeIsExtended;
    }
    if(gamepad.b) {
      intake.orientToCollect();
    }
    else if(gamepad.x) {
      intake.orientToTransfer();
    }
    else if(gamepad.y) {
      intake.orientToTransit();
    }
  }

  @Override
  protected void setup() {
    intake = new Intake(hardwareMap.dcMotor.get("intakeSlide"),
        hardwareMap.servo.get("intakeOrientator"),
        hardwareMap.crservo.get("intakeSweeper"), this);
  }

  private static int booleanToInt(boolean bool) {
    return bool ? 1 : 0;
  }
}
