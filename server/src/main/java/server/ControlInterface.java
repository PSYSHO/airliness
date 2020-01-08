package server;

public interface ControlInterface extends Runnable {
    @Override
    void run();
    String listUpdate();
}
