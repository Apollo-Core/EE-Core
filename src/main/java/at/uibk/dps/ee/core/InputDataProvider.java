package at.uibk.dps.ee.core;

import com.google.gson.JsonObject;

/**
 * The {@link InputDataProvider} provides the input data for the enactment of
 * the workflow (used for single runs).
 * 
 * @author Fedor Smirnov
 *
 */
public interface InputDataProvider {

  /**
   * Provides the input data for the workflow.
   * 
   * @return the input data for the workflow enactment
   */
  JsonObject getInputData();
}
