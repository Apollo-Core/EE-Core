package at.uibk.dps.ee.core;

import java.util.Set;

import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.function.EnactmentStateListener;
import io.vertx.core.Future;
import io.vertx.core.Promise;

/**
 * Core class performing the enactment.
 * 
 * @author Fedor Smirnov
 *
 */
public class EeCore {

  protected final OutputDataHandler outputDataHandler;

  protected final Set<EnactmentStateListener> stateListeners;
  protected final Initializer initializer;
  protected final Terminator terminator;
  protected final CoreFunction coreFunction;
  protected final FailureHandler failureHandler;

  /**
   * Default constructor (also the one used by Guice)
   * 
   * @param outputDataHandler class handling the data obtained as the result of
   *        the WF execution
   * @param enactableProvider provider of the WF description
   * @param stateListeners classes which react to changes of the enactment state
   * @param initializer the initializer for the enactment
   * @param terminator the terminator for the enactment
   * @param failureHandler the failure handler
   */
  public EeCore(final OutputDataHandler outputDataHandler,
      final Set<EnactmentStateListener> stateListeners, final CoreFunction coreFunction,
      final Initializer initializer, final Terminator terminator,
      final FailureHandler failureHandler) {
    this.outputDataHandler = outputDataHandler;
    this.stateListeners = stateListeners;
    this.coreFunction = coreFunction;
    this.initializer = initializer;
    this.terminator = terminator;
    this.failureHandler = failureHandler;
  }

  /**
   * Obtains the root enactable from the provider, inits it with the input data,
   * and triggers the execution. It is expected that the root enactable cannot
   * throw {@link StopException}.
   * 
   * @param inputData the {@link JsonObject} containing input data
   */
  public Future<JsonObject> enactWorkflow(final JsonObject inputData) {
    Promise<JsonObject> resultPromise = Promise.promise();
    initializer.initialize().onComplete(initRes -> {
      executeWorkflow(inputData).onComplete(asyncJsonRes -> {
        if (asyncJsonRes.succeeded()) {
          terminator.terminate().onComplete(termRes -> {
            resultPromise.complete(asyncJsonRes.result());
          });
        } else {
          terminator.terminate().onComplete(termRes -> {
            resultPromise.fail("Enactment failed");
          });
        }
      });
    });
    return resultPromise.future();
  }

  /**
   * Executes the workflow asyncronously with the given input data. The provided
   * promise is completed as soon as the workflow execution is finished.
   * 
   * @param inputData the input data
   * @param resultPromise the promise for the wf execution result
   * @return a future with the Json result, which is completed as soon as the
   *         workflow execution is finished
   */
  protected Future<JsonObject> executeWorkflow(final JsonObject inputData) {
    Promise<JsonObject> resultPromise = Promise.promise();
    for (final EnactmentStateListener stateListener : stateListeners) {
      stateListener.enactmentStarted();
    }
    final Future<JsonObject> wfCompletion = coreFunction.processInput(inputData);
    wfCompletion.onComplete(asyncJson -> {
      if (asyncJson.succeeded()) {
        outputDataHandler.handleOutputData(asyncJson.result());
        resultPromise.complete(asyncJson.result());
      } else {
        failureHandler.handleFailure(asyncJson.cause());
        resultPromise.fail("Enactment Failed");
      }
      // better use event bus for this
      for (final EnactmentStateListener stateListener : stateListeners) {
        stateListener.enactmentTerminated();
      }
    });
    return resultPromise.future();
  }
}
