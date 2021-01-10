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

public class App {
    public static void main(String[] args) {
        //Connector dbConnector = new Connector("jdbc:postgresql://localhost:5432/application", "postgres", "invicto");
        Connector dbConnector = new Connector("jdbc:postgresql://ec2-52-211-108-161.eu-west-1.compute.amazonaws.com:5432/do00mpavp4pvs", "zwjzffiwjjkthd", "14dbcd2930ae062fe55da34c637474a6065b550392e4ed99f7b2d76dac264ba2");
        dbConnector.getConnection();
        UserRepository userRepository = new UserRepositoryImpl(dbConnector);
        RoomRepository roomRepository = new RoomRepositoryImpl(dbConnector, userRepository);
        UserService userService = new UserService(userRepository);
        RoomService roomService = new RoomService(roomRepository, userRepository);
        HttpServer server = new HttpServer();
        HttpRouter router = server.getRouter();
        router.addHandler("/create", new CreateHandler(router, userService, roomService));
        server.run();
    }
}
