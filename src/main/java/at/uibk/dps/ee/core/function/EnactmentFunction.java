package at.uibk.dps.ee.core.function;

import java.util.Set;
import com.google.gson.JsonObject;

import java.util.AbstractMap.SimpleEntry;

import io.vertx.core.Future;

/**
 * The {@link EnactmentFunction} models the concrete behavior of an
 * {@link Enactable}. Assigning an {@link EnactmentFunction} to an
 * {@link Enactable} corresponds to the scheduling step.
 * 
 * @author Fedor Smirnov
 */
public interface EnactmentFunction {

  /**
   * Processes the input json object and returns the result of the function.
   * 
   * @param input
   * @return the json object generated as the result of the process
   * @throws StopException exception thrown if errors occur during the processing
   */
  Future<JsonObject> processInput(JsonObject input);

  /**
   * Returns the type ID of the function. The type ID describes the purpose of the
   * function within a workflow, i.e., the way in which it processes input data
   * (Example type ID: Addition).
   * 
   * @return the type ID of the function.
   */
  String getTypeId();

  /**
   * Returns a string indicating the enactment mode (e.g., local enactment or
   * enactment as a serverless function).
   * 
   * @return a string indicating the enactment mode of the function
   */
  String getEnactmentMode();

  /**
   * Returns a string uniquely identifying the code base used for the
   * implementation of the function.
   * 
   * @return a string uniquely identifying the code base used for the
   *         implementation of the function
   */
  String getImplementationId();

  /**
   * Generic interface for any additional attributes used to describe the
   * function.
   * 
   * @return a set of string-string key value pairs describing additional function
   *         attributes
   */
  Set<SimpleEntry<String, String>> getAdditionalAttributes();
}
