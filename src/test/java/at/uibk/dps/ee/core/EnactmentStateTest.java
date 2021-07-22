package at.uibk.dps.ee.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


/**
 * Just so that the enum does not pull down the coverage.
 * 
 * @author Fedor Smirnov
 *
 */
public class EnactmentStateTest {

  @Test
  public void test() {
    assertEquals(EnactmentState.RUNNING, EnactmentState.valueOf(EnactmentState.RUNNING.name()));
    assertEquals(EnactmentState.PAUSED, EnactmentState.valueOf(EnactmentState.PAUSED.name()));
    assertEquals(EnactmentState.STOPPED, EnactmentState.valueOf(EnactmentState.STOPPED.name()));
  }

}
