package at.uibk.dps.ee.core;

import com.google.gson.JsonObject;
import io.vertx.core.Future;

/**
 * The {@link OutputDataHandler} processes the workflow output data following
 * the EE configuration.
 * 
 * @author Fedor Smirnov
 */
public interface OutputDataHandler {

  /**
   * Handles the output data produced as the result of the execution.
   * 
   * @param outputData completed future of the output data
   */
  void handleOutputData(Future<JsonObject> outputData);
}
