package server;

import common.Message;
import services.AuthService;
import services.ProductService;

public class RequestRouter {

    public static Message route(Message message) {

        if(message == null) {
            return new Message("ERROR","0","ERROR","","NULL_MESSAGE");
        }

        switch(message.getType()) {

            case "PING":
                return new Message(
                        "PING",
                        message.getRequestId(),
                        "SUCCESS",
                        "PONG",
                        ""
                );

            case "LOGIN":
                return AuthService.login(message);

            case "REGISTER":
                return AuthService.register(message);

            case "PRODUCT_LIST":
                return ProductService.list(message);

            case "PRODUCT_DETAILS":
                return ProductService.details(message);

            case "STOCK_UPDATE":
                return ProductService.updateStock(message);

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
