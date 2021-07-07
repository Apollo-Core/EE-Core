package at.uibk.dps.ee.core;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.function.EnactmentStateListener;
import io.vertx.core.Future;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;

public class EeCoreTest {

  @Test
  public void testEnactCorrect() {
    OutputDataHandler outputDataHandler = mock(OutputDataHandler.class);
    Set<EnactmentStateListener> enactmentListeners = new HashSet<>();
    CoreFunction functionMock = mock(CoreFunction.class);
    JsonObject mockInput = new JsonObject();
    EnactmentStateListener mockListener = mock(EnactmentStateListener.class);
    enactmentListeners.add(mockListener);
    JsonObject mockOutput = new JsonObject();

    LocalResources locRes1 = mock(LocalResources.class);
    LocalResources locRes2 = mock(LocalResources.class);
    Set<LocalResources> locRes = new HashSet<>();
    locRes.add(locRes1);
    locRes.add(locRes2);

    when(functionMock.processInput(mockInput)).thenReturn(Future.succeededFuture(mockOutput));
    EeCore tested = new EeCore(outputDataHandler, enactmentListeners, locRes, functionMock);
    JsonObject result = tested.enactWorkflow(mockInput).result();
    assertEquals(mockOutput, result);
    verify(outputDataHandler).handleOutputData(mockOutput);
    verify(mockListener).enactmentStarted();
    verify(mockListener).enactmentTerminated();

    tested.close();
    verify(locRes1).init();
    verify(locRes1).close();
    verify(locRes2).init();
    verify(locRes2).close();
  }
}
