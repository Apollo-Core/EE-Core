package at.uibk.dps.ee.core;

import java.util.Set;

import com.google.gson.JsonObject;

import at.uibk.dps.ee.core.enactable.EnactableRoot;
import at.uibk.dps.ee.core.enactable.EnactmentStateListener;
import at.uibk.dps.ee.core.exception.FailureException;
import at.uibk.dps.ee.core.exception.StopException;

/**
 * Core class performing the enactment.
 * 
 * @author Fedor Smirnov
 *
 */
public class EeCore {

  protected final OutputDataHandler outputDataHandler;
  protected final EnactableProvider enactableProvider;

  protected final Set<EnactmentStateListener> stateListeners;
  protected final Set<LocalResources> localResources;

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
      final EnactableProvider enactableProvider, final Set<EnactmentStateListener> stateListeners,
      final Set<LocalResources> localResources) {
    this.outputDataHandler = outputDataHandler;
    this.enactableProvider = enactableProvider;
    this.stateListeners = stateListeners;
    this.localResources = localResources;
    localResources.forEach(locRes -> locRes.init());
  }

  /**
   * Obtains the root enactable from the provider, inits it with the input data,
   * and triggers the execution. It is expected that the root enactable cannot
   * throw {@link StopException}.
   * 
   * @param inputData the {@link JsonObject} containing input data
   * 
   * @throws FailureException
   */
  public JsonObject enactWorkflow(final JsonObject inputData) throws FailureException {
    final EnactableRoot enactableRoot = enactableProvider.getEnactableApplication();
    enactableRoot.setInput(inputData);
    for (final EnactmentStateListener stateListener : stateListeners) {
      stateListener.enactmentStarted();
    }
    try {
      enactableRoot.play();
      final JsonObject outputData = enactableRoot.getResult();
      outputDataHandler.handleOutputData(outputData);
      return outputData;
    } catch (StopException stopException) {
      // The root should never throw exceptions.
      throw new FailureException(stopException);
    } finally {
      for (final EnactmentStateListener stateListener : stateListeners) {
        stateListener.enactmentTerminated();
      }
    }
  }

  /**
   * Called to clean up before terminating the current Apollo instance.
   */
  public void close() {
    localResources.forEach(locRes -> locRes.close());
  }
}
