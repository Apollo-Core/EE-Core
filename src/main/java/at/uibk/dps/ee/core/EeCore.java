package at.uibk.dps.ee.core;

import java.util.Set;

import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.function.EnactmentStateListener;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Core class performing the enactment.
 * 
 * @author Fedor Smirnov
 *
 */
public class EeCore extends AbstractVerticle{

  protected final OutputDataHandler outputDataHandler;

  protected final Set<EnactmentStateListener> stateListeners;
  protected final Set<LocalResources> localResources;
  protected final CoreFunction coreFunction;

  /**
   * Default constructor (also the one used by Guice)
   * 
   * @param outputDataHandler class handling the data obtained as the result of
   *        the WF execution
   * @param enactableProvider provider of the WF description
   * @param stateListeners classes which react to changes of the enactment state
   * @param localResources a set of interfaces for the access to the resources on
   *        the current Apollo instance
   */
  public EeCore(final OutputDataHandler outputDataHandler,
      final Set<EnactmentStateListener> stateListeners,
      final Set<LocalResources> localResources, CoreFunction coreFunction) {
    this.outputDataHandler = outputDataHandler;
    this.stateListeners = stateListeners;
    this.localResources = localResources;
    this.coreFunction = coreFunction;
    localResources.forEach(locRes -> locRes.init());
  }

  /**
   * Obtains the root enactable from the provider, inits it with the input data,
   * and triggers the execution. It is expected that the root enactable cannot
   * throw {@link StopException}.
   * 
   * @param inputData the {@link JsonObject} containing input data
   */
  public Future<JsonObject> enactWorkflow(final JsonObject inputData) {
    for (final EnactmentStateListener stateListener : stateListeners) {
      stateListener.enactmentStarted();
    }
    Future<JsonObject> wfCompletion = coreFunction.processInput(inputData);
    wfCompletion.onComplete(asyncJson ->{
      JsonObject result = asyncJson.result();
      outputDataHandler.handleOutputData(result);
      // better use event bus for this
      for (final EnactmentStateListener stateListener : stateListeners) {
        stateListener.enactmentTerminated();
      }
    });
    return wfCompletion;
  }

  /**
   * Called to clean up before terminating the current Apollo instance.
   */
  public void close() {
    localResources.forEach(locRes -> locRes.close());
  }
}
