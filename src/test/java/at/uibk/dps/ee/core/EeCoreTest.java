package at.uibk.dps.ee.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import com.google.gson.JsonObject;

import at.uibk.dps.ee.core.enactable.EnactableRoot;
import at.uibk.dps.ee.core.enactable.EnactmentFunction;
import at.uibk.dps.ee.core.enactable.EnactmentStateListener;
import at.uibk.dps.ee.core.exception.FailureException;
import at.uibk.dps.ee.core.exception.StopException;

import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;

public class EeCoreTest {


  @Test
  public void testEnactCorrect() {
    InputDataProvider inputProviderMock = mock(InputDataProvider.class);
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    EnactableProvider enactableProvider = mock(EnactableProvider.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    EnactmentFunction functionMock = mock(EnactmentFunction.class);
    EnactableRoot mockEnactable = new EnactableRoot(new HashSet<>(), functionMock);
    JsonObject mockInput = new JsonObject();
    when(enactableProvider.getEnactableApplication()).thenReturn(mockEnactable);
    when(inputProviderMock.getInputData()).thenReturn(mockInput);
    EnactmentStateListener mockListener = mock(EnactmentStateListener.class);
    enactmentListeners.add(mockListener);
    JsonObject mockOutput = new JsonObject();
    try {
      when(functionMock.processInput(mockInput)).thenReturn(mockOutput);
      EeCore tested =
          new EeCore(inputProviderMock, outputDataHandler, enactableProvider, enactmentListeners);
      JsonObject result = tested.enactWorkflow();
      assertEquals(mockOutput, result);
      verify(outputDataHandler).handleOutputData(mockOutput);
      verify(mockListener).enactmentStarted();
      verify(mockListener).enactmentTerminated();
    } catch (FailureException | StopException stopExc) {
      fail();
    }
  }

  @Test
  public void testEnactException() {
    InputDataProvider inputProviderMock = mock(InputDataProvider.class);
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    EnactableProvider enactableProvider = mock(EnactableProvider.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    EnactmentFunction functionMock = mock(EnactmentFunction.class);
    EnactableRoot mockEnactable = new EnactableRoot(new HashSet<>(), functionMock);
    JsonObject mockInput = new JsonObject();
    when(enactableProvider.getEnactableApplication()).thenReturn(mockEnactable);
    when(inputProviderMock.getInputData()).thenReturn(mockInput);
    EnactmentStateListener mockListener = mock(EnactmentStateListener.class);
    enactmentListeners.add(mockListener);
    EeCore tested =
        new EeCore(inputProviderMock, outputDataHandler, enactableProvider, enactmentListeners);
    try {
      Mockito.doThrow(new StopException("bla")).when(functionMock).processInput(mockInput);
      tested.enactWorkflow();
      fail();
    } catch (StopException stopExc) {
      fail();
    } catch (FailureException stopExc) {
      return;
    }
  }

  @Test
  public void testConstructor() {
    InputDataProvider inputProviderMock = mock(InputDataProvider.class);
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    EnactableProvider wfProvider = mock(EnactableProvider.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    EeCore tested =
        new EeCore(inputProviderMock, outputDataHandler, wfProvider, enactmentListeners);
    assertEquals(inputProviderMock, tested.inputDataProvider);
    assertEquals(outputDataHandler, tested.outputDataHandler);
    assertEquals(wfProvider, tested.enactableProvider);
    assertEquals(enactmentListeners, tested.stateListeners);
  }
}
