package at.uibk.dps.ee.core.function;

/**
 * Interface for classes which act in reaction to events on the global scale of
 * the enactment (e.g., the start or the termination of the enactment).
 * 
 * @author Fedor Smirnov
 *
 */
public interface EnactmentStateListener {

  /**
   * Called once at the start of the enactment.
   */
  void enactmentStarted();

  /**
   * Called once at the end of the enactment.
   */
  void enactmentTerminated();
}
