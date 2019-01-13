package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.SimpleArm;

public enum ControlMode implements IControlMode {
  DRIVE(g -> g.y, gamepad -> {
    final boolean microAdjust = gamepad.left_bumper || gamepad.right_bumper;
    Coordinate leftTarget = Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y)
            .multiply(microAdjust ? 0.3 : 1);
    final Bot bot = Bot.getInstance();

    if (Math.abs(gamepad.right_stick_x) > 0.1) {
      bot.drivetrain.setRotationPower(gamepad.right_stick_x);
    } else {
      bot.drivetrain.setStrafe(leftTarget, leftTarget.getPolarDistance());
    }
  }),


  HOOK_SLIDE(g -> g.start, gamepad -> controlHook(gamepad, Bot.getInstance().hookArm)),
  LEFT_SLIDE(g -> g.x, gamepad -> controlArmWithMicroAdjust(gamepad, Bot.getInstance().leftArm)),
  RIGHT_SLIDE(g -> g.b, gamepad -> controlArmWithMicroAdjust(gamepad, Bot.getInstance().rightArm)),
  BOTH_SLIDES(g -> g.a, ControlMode::controlBothArms);

  private static void controlBothArms(Gamepad gamepad) {
    Bot bot = Bot.getInstance();

    if (gamepad.dpad_down) {
      bot.leftArm.openGrabber();
      bot.rightArm.openGrabber();
    }

    controlLiftByTriggerBumper(bot.leftArm, gamepad.left_bumper, gamepad.left_trigger);
    controlLiftByTriggerBumper(bot.rightArm, gamepad.right_bumper, gamepad.right_trigger);
  }

  private static void controlLiftByTriggerBumper(Arm arm, boolean bumper,
      float trigger) {
    arm.setLiftPower(booleanToInt(bumper) * 0.3 - trigger);
  }

  private static void controlArmWithMicroAdjust(Gamepad gamepad, Arm arm) {
    controlArm(gamepad, arm);
    Bot.getInstance().drivetrain.setStrafe(getMicroAdjustCoord(gamepad), 0.3);
  }

  private static Coordinate getMicroAdjustCoord(Gamepad gamepad) {
    double x = booleanToInt(gamepad.dpad_right) - booleanToInt(gamepad.dpad_left);
    double y = booleanToInt(gamepad.dpad_up) - booleanToInt(gamepad.dpad_down);
    return Coordinate.fromXY(x, y);
  }

  private static int booleanToInt(boolean bool) {
    return bool ? 1 : 0;
  }

  private static void controlArm(Gamepad gamepad, Arm arm) {
    controlSimpleArm(gamepad, arm);
    arm.moveGrabber((gamepad.right_trigger - gamepad.left_trigger) * 0.01);
  }

  private static void controlSimpleArm(Gamepad gamepad, SimpleArm arm) {
    arm.setLiftPower(-gamepad.right_stick_y * 0.15);
  }

  private static void controlHook(Gamepad gamepad, SimpleArm arm) {
    arm.setLiftPower(-gamepad.right_stick_y * 0.8);
  }

  /**
   * Function that checks the state of the {@link Gamepad} to determine whether its control mode
   * should be switched to this mode.
   */
  private Predicate<Gamepad> activator;

  /**
   * Function that triggers output according to the input values of the supplied {@link Gamepad}.
   */
  private Consumer<Gamepad> applier;

  ControlMode(Predicate<Gamepad> activator,
      Consumer<Gamepad> applier) {
    this.activator = activator;
    this.applier = applier;
  }

  public void apply(Gamepad gamepad) {
    applier.accept(gamepad);
  }

  public boolean shouldBeActivated(Gamepad gamepad) {
    return activator.test(gamepad);
  }
}
