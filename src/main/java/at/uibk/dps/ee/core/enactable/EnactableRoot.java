package at.uibk.dps.ee.core.enactable;

import java.util.Set;

import com.google.gson.JsonObject;

/**
 * The {@link EnactableRoot} is the enactable modeling the enactment of the
 * overall Workflow.
 * 
 * @author Fedor Smirnov
 *
 */
public class EnactableRoot extends Enactable {

  /**
   * Standard constructor.
   * 
   * @param stateListeners
   */
  public EnactableRoot(final Set<EnactableStateListener> stateListeners,
      final EnactmentFunction function) {
    super(stateListeners);
    this.init();
    this.schedule(function);
  }

  /**
   * Sets the json object which is to be used as WF input.
   * 
   * @param wfInput the json object which is to be used as WF input
   */
  public void setInput(final JsonObject wfInput) {
    this.jsonInput = wfInput;
    if (getState().equals(State.FINISHED)) {
      // repeated runs with different inputs => reset to Launchable
      setState(State.LAUNCHABLE);
    }
  }
}
