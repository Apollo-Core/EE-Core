package at.uibk.dps.ee.core.function;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonObject;
import io.vertx.core.Future;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class EnactmentFunctionDecoratorTest {

  protected static class TestedDecorator extends EnactmentFunctionDecorator {

    public TestedDecorator(EnactmentFunction decoratedFunction) {
      super(decoratedFunction);
    }

    @Override
    protected Future<JsonObject> preprocess(JsonObject input) {
      return Future.succeededFuture(input);
    }

    @Override
    protected Future<JsonObject> postprocess(JsonObject result) {
      return Future.succeededFuture(result);
    }
  }

  @Test
  public void test() {
    String typeId = "addition";
    String enactmentMode = "local";
    String implementationId = "nativeJava";

    Set<SimpleEntry<String, String>> additionalAttributes = new HashSet<>();
    additionalAttributes.add(new SimpleEntry<String, String>("key", "value"));

    EnactmentFunction mockOriginal = mock(EnactmentFunction.class);
    when(mockOriginal.getTypeId()).thenReturn(typeId);
    when(mockOriginal.getEnactmentMode()).thenReturn(enactmentMode);
    when(mockOriginal.getImplementationId()).thenReturn(implementationId);
    when(mockOriginal.getAdditionalAttributes()).thenReturn(additionalAttributes);
    JsonObject input = new JsonObject();
    JsonObject result = new JsonObject();
    when(mockOriginal.processInput(input)).thenReturn(Future.succeededFuture(result));
    TestedDecorator tested = new TestedDecorator(mockOriginal);
    TestedDecorator spy = spy(tested);
    assertEquals(typeId, tested.getTypeId());
    assertEquals(enactmentMode, tested.getEnactmentMode());
    assertEquals(implementationId, tested.getImplementationId());
    assertEquals(additionalAttributes, tested.getAdditionalAttributes());
    assertEquals(result, spy.processInput(input).result());
    verify(spy).preprocess(input);
    verify(spy).postprocess(result);
  }
}
