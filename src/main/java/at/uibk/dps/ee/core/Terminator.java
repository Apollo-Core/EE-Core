package at.uibk.dps.ee.core;

import io.vertx.core.Future;

/**
 * Interface for the components which require some sort of finalization or
 * cleanup after the enactment ends (this includes both the regular end of an
 * enactment and termination due to exceptions or user actions).
 * 
 * @author Fedor Smirnov
 */
public interface Terminator {

  /**
   * Triggers the start of the termination routine. Returns a future which is
   * completed as soon as the termination is complete.
   * 
   * @return a future which is completed as soon as the termination is complete
   */
  public Future<String> terminate();
}
