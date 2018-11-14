package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Bot;

import java.util.function.Consumer;
import java.util.function.Predicate;
import org.firstinspires.ftc.teamcode.SimpleArm;

public enum ControlMode implements IControlMode {
  DRIVE(g -> g.y, gamepad -> {
    final Coordinate leftTarget = Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
    final Bot bot = Bot.getInstance();

    if (Math.abs(gamepad.right_stick_x) > 0.1) {
      bot.drivetrain.setRotationPower(gamepad.right_stick_x);
    } else {
      bot.drivetrain.setStrafe(leftTarget, leftTarget.getPolarDistance());
    }
  }),

  LEFT_SLIDE(g -> g.x, gamepad -> controlArm(gamepad, Bot.getInstance().leftArm)),
  RIGHT_SLIDE(g -> g.b, gamepad -> controlArm(gamepad, Bot.getInstance().rightArm)),
  REAR_SLIDE(g -> g.a, gamepad -> controlSimpleArm(gamepad, Bot.getInstance().backArm));

  private static void controlArm(Gamepad gamepad, Arm arm) {
    controlSimpleArm(gamepad, arm);
    arm.rotateLateral(gamepad.left_stick_x * 0.01);
    arm.rotateVertical(gamepad.left_stick_y * -0.01);
  }

  private static void controlSimpleArm(Gamepad gamepad, SimpleArm arm) {
    arm.setLiftPower(-gamepad.right_stick_y);
    arm.moveGrabber((gamepad.right_trigger - gamepad.left_trigger) * 0.01);
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
