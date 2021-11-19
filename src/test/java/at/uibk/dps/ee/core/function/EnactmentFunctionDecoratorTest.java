package at.uibk.dps.ee.core.function;

import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonObject;
import io.vertx.core.Future;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Set;
import org.junit.jupiter.api.Test;
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
    Set<SimpleEntry<String, String>> additionalAttributes = new HashSet<>();
    additionalAttributes.add(new SimpleEntry<String, String>("key", "value"));

    EnactmentFunction mockOriginal = mock(EnactmentFunction.class);
    JsonObject input = new JsonObject();
    JsonObject result = new JsonObject();
    when(mockOriginal.processInput(input)).thenReturn(Future.succeededFuture(result));
    TestedDecorator tested = new TestedDecorator(mockOriginal);
    TestedDecorator spy = spy(tested);
    assertEquals(result, spy.processInput(input).result());
    verify(spy).preprocess(input);
    verify(spy).postprocess(result);
  }
}
