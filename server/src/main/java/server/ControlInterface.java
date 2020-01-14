package server;

import java.io.IOException;

public interface ControlInterface extends Runnable {
    @Override
    void run();
    void Update() throws IOException;

}
