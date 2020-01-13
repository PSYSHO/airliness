package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        /*int port = 8000;
        List fly = new ArrayList<AirLinesServer>();
        try (ServerSocket server = new ServerSocket(port)
        ) {
            while (!server.isClosed()) {
                Thread client = new Thread(new AirLinesServer(server.accept()));//todo передавать список Handler
                client.start();
                System.out.println("accepted");
                fly.add(client);
                for (Object controlInterface:fly){
                }
            }
        } catch (IOException e) {
        }
*/

    }
    //todo сделать настройку в пом для клиента и ервера где лежат мейн классы
    // сделать сборку джарок с либами мавена
    // почитать про runnable  при измменении данных оповещать вех клиентов
    // сделать проверку уникальности по id при добавлении или редактировании
    // клиенты работают с своими данными сервер хранит у себя оригинал и сохраняет в конце своей работы

}
