package co.helmethair.scalatest.runtime;

import org.scalatest.Stopper;

public class StopperImpl implements Stopper {
    volatile private boolean stopWasRequested = false;

    @Override
    public boolean stopRequested() {
        return stopWasRequested;
    }

    @Override
    public void requestStop() {
        stopWasRequested = true;
    }
};
