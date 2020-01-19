package server;

import request.Message;

import java.io.IOException;

public interface ControlInterface extends Runnable {
    @Override
    void run();
    void Update(Message message) throws IOException;

}
