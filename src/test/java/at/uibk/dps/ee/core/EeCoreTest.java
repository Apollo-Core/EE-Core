package at.uibk.dps.ee.core;

import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.function.EnactmentStateListener;
import io.vertx.core.Future;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;

public class EeCoreTest {

  @Test
  public void testEnactException() {
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    CoreFunction functionMock = mock(CoreFunction.class);
    JsonObject mockInput = new JsonObject();
    EnactmentStateListener mockListener = mock(EnactmentStateListener.class);
    enactmentListeners.add(mockListener);

    Initializer initMock = mock(Initializer.class);
    Future<String> initResult = Future.succeededFuture("success");
    when(initMock.initialize()).thenReturn(initResult);

    Terminator termMock = mock(Terminator.class);
    Future<String> termResult = Future.succeededFuture("success");
    when(termMock.terminate()).thenReturn(termResult);
    
    FailureHandler fHandler = mock(FailureHandler.class);

    Throwable expectedExc = new IllegalArgumentException("exception");
    
    when(functionMock.processInput(mockInput))
        .thenReturn(Future.failedFuture(expectedExc));
    EeCore tested =
        new EeCore(outputDataHandler, enactmentListeners, functionMock, initMock, termMock, fHandler);
    tested.enactWorkflow(mockInput);

    verify(mockListener).enactmentStarted();
    verify(mockListener).enactmentTerminated();
    verify(fHandler).handleFailure(expectedExc);
    verify(initMock).initialize();
    verify(termMock).terminate();
  }

  @Test
  public void testEnactCorrect() {
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    CoreFunction functionMock = mock(CoreFunction.class);
    JsonObject mockInput = new JsonObject();
    EnactmentStateListener mockListener = mock(EnactmentStateListener.class);
    enactmentListeners.add(mockListener);
    JsonObject mockOutput = new JsonObject();


    Initializer initMock = mock(Initializer.class);
    Future<String> initResult = Future.succeededFuture("success");
    when(initMock.initialize()).thenReturn(initResult);

    Terminator termMock = mock(Terminator.class);
    Future<String> termResult = Future.succeededFuture("success");
    when(termMock.terminate()).thenReturn(termResult);

    FailureHandler fHandler = mock(FailureHandler.class);

    when(functionMock.processInput(mockInput)).thenReturn(Future.succeededFuture(mockOutput));
    EeCore tested = new EeCore(outputDataHandler, enactmentListeners, functionMock, initMock,
        termMock, fHandler);
    tested.enactWorkflow(mockInput);

    verify(mockListener).enactmentStarted();
    verify(mockListener).enactmentTerminated();
    verify(outputDataHandler).handleOutputData(mockOutput);
    verify(initMock).initialize();
    verify(termMock).terminate();
  }
}
