package com.invicto;

import com.invicto.server.handlers.CreateHandler;
import com.invicto.server.HttpRouter;
import com.invicto.server.HttpServer;
import com.invicto.services.RoomService;
import com.invicto.services.UserService;
import com.invicto.storage.RoomRepository;
import com.invicto.storage.UserRepository;
import com.invicto.storage.postgresql.Connector;
import com.invicto.storage.postgresql.RoomRepositoryImpl;
import com.invicto.storage.postgresql.UserRepositoryImpl;

import java.net.URI;
import java.net.URISyntaxException;

public class App {
    public static void main(String[] args) throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        Connector dbConnector = new Connector(dbUrl, username, password);
        dbConnector.getConnection();
        UserRepository userRepository = new UserRepositoryImpl(dbConnector);
        RoomRepository roomRepository = new RoomRepositoryImpl(dbConnector, userRepository);
        UserService userService = new UserService(userRepository);
        RoomService roomService = new RoomService(roomRepository, userRepository);
        HttpServer server = new HttpServer(Integer.parseInt(System.getenv("PORT")));
        HttpRouter router = server.getRouter();
        router.addHandler("/create", new CreateHandler(router, userService, roomService));
        server.run();
    }
}
