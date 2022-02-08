package at.uibk.dps.ee.core;

import io.vertx.core.Future;

/**
 * Interface for classes performing the initialization required before the
 * actual workflow execution can be started.
 * 
 * @author Fedor Smirnov
 */
public interface Initializer {
  /**
   * Asynchronously performs the initialization necessary to start the WF
   * execution.
   * 
   * @return a future which is completed as soon as the initialization steps are
   *         finished. The response body can be used to convey additional
   *         information.
   */
  Future<String> initialize();
}
