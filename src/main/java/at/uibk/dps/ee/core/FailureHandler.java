package at.uibk.dps.ee.core;

/**
 * Interface for the classes reacting to enactment failures.
 * 
 * @author Fedor Smirnov
 */
public interface FailureHandler {

  /**
   * Handles the cause of the failure.
   * 
   * @param failureCause the failure cause
   */
  void handleFailure(Throwable failureCause);

}
