package at.uibk.dps.ee.core.enactable;

import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.exception.StopException;

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
  public JsonObject processInput(final JsonObject input) throws StopException {
    final JsonObject preprocessedInput = preprocess(input);
    final JsonObject rawResult = decoratedFunction.processInput(preprocessedInput);
    return postprocess(rawResult);
  }

   @Override
  public String getEnactmentMode() {
    return decoratedFunction.getEnactmentMode();
  }
   
   @Override
  public String getImplementationId() {
    return decoratedFunction.getImplementationId();
  }
   
   @Override
  public String getTypeId() {
    return decoratedFunction.getTypeId();
  }
   
   @Override
  public Set<SimpleEntry<String, String>> getAdditionalAttributes() {
    return decoratedFunction.getAdditionalAttributes();
  }

  /**
   * Method to define the steps which are to be taken before the processing
   * performed by the decorated function.
   * 
   * @param input the input of the decorated function.
   * @return the input actually given to the decorated function.
   */
  protected abstract JsonObject preprocess(JsonObject input);

  /**
   * Method to define the steps which are to be taken after the processing
   * performed by the decorated function.
   * 
   * @param result the result of the decorated function
   * @return the result after the postprocessing
   */
  protected abstract JsonObject postprocess(JsonObject result);
}
