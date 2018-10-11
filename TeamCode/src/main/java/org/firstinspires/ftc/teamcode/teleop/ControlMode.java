package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum ControlMode {
  DRIVE(g -> g.x, gamepad -> {

  }),
  SLIDE_1(g -> g.a, gamepad -> {

  }),
  SLIDE_2(g -> g.b, gamepad -> {

  }),
  BOTH_SLIDES(g -> g.y, gamepad -> {

  });

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
}
