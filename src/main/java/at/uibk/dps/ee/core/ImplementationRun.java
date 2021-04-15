package at.uibk.dps.ee.core;

import com.google.gson.JsonObject;

/**
 * Interface enabling to use Apollo within a method call.
 * 
 * @author Fedor Smirnov
 *
 */
public interface ImplementationRun {

  /**
   * Implements the application as specified by the provided strings. Returns the resulting Json object.
   * 
   * @param inputString the string specifying the application input
   * @param specString the string specifying the spec graph
   * @param configString the string specifying the module configuration
   * @return the result of the application
   */
  public JsonObject implement(String inputString, String specString, String configString);
}
