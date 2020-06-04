package co.helmethair.scalatest.helper;

import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public interface RegisterCall {

    RegisterCall mock = mock(RegisterCall.class);

    static void verifyTestExecuteCode(int expectedTestCount, Body body) {
        synchronized (mock) {
            Mockito.reset(mock);
            body.apply();
            verify(mock, times(expectedTestCount)).register();
        }
    }

    static void verifyTestExecuteCode(Map<String, Integer> callers, Body body) {
        synchronized (mock) {
            Mockito.reset(mock);
            body.apply();
            callers.forEach((c, t) -> verify(mock, times(t)).register(c));
        }
    }

    static void verifyTestExecuteCode(List<String> callers, Body body) {
        synchronized (mock) {
            Mockito.reset(mock);
            body.apply();
            InOrder inOrder = inOrder(mock);
            callers.forEach(c -> inOrder.verify(mock).register(c));
        }
    }

    default void register() {
        synchronized (mock) {
            RegisterCall.mock.register();
        }
    }

    default void register(String caller) {
        synchronized (mock) {
            RegisterCall.mock.register(caller);
        }
    }

    interface Body {
        void apply();
    }
}

