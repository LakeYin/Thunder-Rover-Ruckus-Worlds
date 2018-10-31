package org.firstinspires.ftc.teamcode;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.teleop.ControlMapper;
import org.firstinspires.ftc.teamcode.teleop.ControlMode;
import org.firstinspires.ftc.teamcode.teleop.IControlMode;
import org.junit.Before;
import org.junit.Test;

public class ControlModeTest {
  private Gamepad gamepad1, gamepad2;
  private ControlMapper mapper;
  private IControlMode mockMode1 = mock(IControlMode.class),
      mockMode2 = mock(IControlMode.class);

  @Before
  public void setUp() {
    mapper = new ControlMapper();
    gamepad1 = new Gamepad();
    gamepad2 = new Gamepad();

    reset(mockMode1, mockMode2);
  }

  @Test
  public void initialValues() {
    assertEquals(mapper.getModeForGamepad1(), ControlMode.DRIVE);
    assertEquals(mapper.getModeForGamepad2(), ControlMode.SLIDE_1);
  }

  @Test
  public void modeMapping() {
    mapper.setModeForGamepad1(mockMode2);
    mapper.setModeForGamepad2(mockMode1);

    mapper.applyGamepadInputs(gamepad1, gamepad2);

    verify(mockMode1).apply(gamepad2);
    verify(mockMode2).apply(gamepad1);
  }

  @Test
  public void modeActivation() {
    gamepad1.a = true;
    gamepad2.x = true;

    mapper.applyGamepadInputs(gamepad1, gamepad2);

    assertEquals(mapper.getModeForGamepad1(), ControlMode.SLIDE_1);
    assertEquals(mapper.getModeForGamepad2(), ControlMode.DRIVE);
  }
}
