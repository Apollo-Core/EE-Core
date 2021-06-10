package at.uibk.dps.ee.core;

/**
 * Interface for the access to and the configuration of resources available at
 * the host of the current Apollo instance.
 * 
 * @author Fedor Smirnov
 */
public interface LocalResources {

  /**
   * Initializes the local resources.
   * 
   */
  void init();

  /**
   * Performs the operations necessary prior to shutting down the local Apollo
   * instance (e.g., shutting down the local resources).
   */
  void close();

}
