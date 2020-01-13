package sample.interfaceController;

import sample.Client;

public class ThreadAlert implements Runnable {
    private Client client;

    public ThreadAlert(Client client){
        this.client=client;
    }

    @Override
    public void run() {
        client.waitingMessage();
    }
}
