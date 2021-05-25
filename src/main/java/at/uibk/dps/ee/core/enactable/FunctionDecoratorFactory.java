package at.uibk.dps.ee.core.enactable;

/**
 * Parent class for the classes used to do the actual wrapping (necessary
 * because of the injection logic). Note: Make sure that each class extending
 * this one is annotated as a singleton.
 * 
 * @author Fedor Smirnov
 */
public abstract class FunctionDecoratorFactory {

  /**
   * Decorates the given function and returns the result.
   * 
   * @param function
   * @return
   */
  public abstract EnactmentFunction decorateFunction(EnactmentFunction function);


  /**
   * Returns the priority of the created decorator. The priority determines the
   * order in which decorators are applied. Decorators with a lower priority are
   * applied later (they are closer to the exposed api of the resulting object).
   * 
   * @return the priority of this decorator
   */
  public abstract int getPriority();
}
