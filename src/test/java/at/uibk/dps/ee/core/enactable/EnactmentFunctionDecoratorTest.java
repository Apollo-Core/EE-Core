package at.uibk.dps.ee.core.enactable;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonObject;
import at.uibk.dps.ee.core.exception.StopException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class EnactmentFunctionDecoratorTest {

  protected static class TestedDecorator extends EnactmentFunctionDecorator {

    public TestedDecorator(EnactmentFunction decoratedFunction) {
      super(decoratedFunction);
    }

    @Override
    protected JsonObject preprocess(JsonObject input) {
      return input;
    }

    @Override
    protected JsonObject postprocess(JsonObject result) {
      return result;
    }
  }

  @Test
  public void test() {
    String typeId = "addition";
    String enactmentMode = "local";
    String implementationId = "nativeJava";

    EnactmentFunction mockOriginal = mock(EnactmentFunction.class);
    when(mockOriginal.getTypeId()).thenReturn(typeId);
    when(mockOriginal.getEnactmentMode()).thenReturn(enactmentMode);
    when(mockOriginal.getImplementationId()).thenReturn(implementationId);
    JsonObject input = new JsonObject();
    JsonObject result = new JsonObject();
    try {
      when(mockOriginal.processInput(input)).thenReturn(result);
    } catch (StopException e) {
      fail();
    }
    TestedDecorator tested = new TestedDecorator(mockOriginal);
    TestedDecorator spy = spy(tested);
    assertEquals(typeId, tested.getTypeId());
    assertEquals(enactmentMode, tested.getEnactmentMode());
    assertEquals(implementationId, tested.getImplementationId());
    try {
      assertEquals(result, spy.processInput(input));
      verify(spy).preprocess(input);
      verify(spy).postprocess(result);
    } catch (StopException e) {
      fail();
    }
  }
}
