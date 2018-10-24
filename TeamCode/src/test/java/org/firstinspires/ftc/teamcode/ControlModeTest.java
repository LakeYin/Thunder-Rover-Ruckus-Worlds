package org.firstinspires.ftc.teamcode;

import static junit.framework.Assert.assertEquals;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Consumer;
import org.firstinspires.ftc.teamcode.teleop.ControlMapper;
import org.firstinspires.ftc.teamcode.teleop.ControlMode;
import org.junit.Before;
import org.junit.Test;

public class ControlModeTest {
  private Gamepad gamepad1, gamepad2;
  private ControlMapper mapper;

  @Before
  public void setUp() {
    mapper = new ControlMapper();
  }

  @Test
  public void initialValues() {
    assertEquals(mapper.getModeForGamepad1(), ControlMode.DRIVE);
    assertEquals(mapper.getModeForGamepad2(), ControlMode.SLIDE_1);
  }

  @Test
  public void modeMapping() {
    mapper.setModeForGamepad1(ControlMode.SLIDE_1);
    mapper.setModeForGamepad2(ControlMode.SLIDE_2);

    gamepad1 = new Gamepad();
    gamepad2 = new Gamepad();
    mapper.applyGamepadInputs(gamepad1, gamepad2);

    // TODO mock ControlMode?
  }
}
