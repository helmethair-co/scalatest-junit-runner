package co.helmethair.scalatest.helper;

import org.mockito.Mockito;

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

    default void register() {
        synchronized (mock) {
            RegisterCall.mock.register();
        }
    }

    interface Body {
        void apply();
    }
}

