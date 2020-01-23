package server;

import com.google.gson.Gson;
import request.Message;

import java.io.IOException;

public interface ControlInterface extends Runnable {
    @Override
    void run();

}
