package at.uibk.dps.ee.core.function;

import com.google.gson.JsonObject;
import io.vertx.core.Future;

/**
 * The {@link EnactmentFunctionDecorator} offers a flexible way to dynamically
 * extend the behavior of enactment functions.
 * 
 * @author Fedor Smirnov
 *
 */
public abstract class EnactmentFunctionDecorator implements EnactmentFunction {

  protected final EnactmentFunction decoratedFunction;

  /**
   * Default constructor.
   * 
   * @param decoratedFunction the functions whose behavior is being extended.
   */
  public EnactmentFunctionDecorator(final EnactmentFunction decoratedFunction) {
    this.decoratedFunction = decoratedFunction;
  }

  @Override
  public Future<JsonObject> processInput(final JsonObject input){
    return preprocess(input).compose(preprocessedJson -> {
      return decoratedFunction.processInput(preprocessedJson);
    }).compose(resultJson -> {
      return postprocess(resultJson);
    });
  }

  /**
   * Method to define the steps which are to be taken before the processing
   * performed by the decorated function.
   * 
   * @param input the input of the decorated function.
   * @return the input actually given to the decorated function.
   */
  protected abstract Future<JsonObject> preprocess(JsonObject input);

  /**
   * Method to define the steps which are to be taken after the processing
   * performed by the decorated function.
   * 
   * @param result the result of the decorated function
   * @return the result after the postprocessing
   */
  protected abstract Future<JsonObject> postprocess(JsonObject result);
}
