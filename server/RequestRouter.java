package server;

import common.Message;
import services.AuthService;

public class RequestRouter {

    public static Message route(Message message) {

        if(message == null) {
            return new Message("ERROR","0","ERROR","","NULL_MESSAGE");
        }

        switch(message.getType()) {

            case "LOGIN":
                return AuthService.login(message);

            case "REGISTER":
                return AuthService.register(message);

            default:
                return new Message(
                        message.getType(),
                        message.getRequestId(),
                        "ERROR",
                        "",
                        "UNKNOWN_REQUEST"
                );
        }
    }
}
