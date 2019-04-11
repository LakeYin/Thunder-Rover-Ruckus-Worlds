package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.HookLift;

@TeleOp(name = "HookLiftDemo", group = "ARC")
public class HookLiftDemo extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotor hookLift = hardwareMap.dcMotor.get("hookLift");
    hookLift.setMode(RunMode.STOP_AND_RESET_ENCODER);
    while (hookLift.getCurrentPosition() != 0 && opModeIsActive()) {
      ;
    }
    HookLift lift = new HookLift(hookLift);

    waitForStart();

    lift.liftToHook();

  }
}
