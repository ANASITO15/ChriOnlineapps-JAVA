package services;

import common.Message;

public class AuthService {

    public static Message login(Message request) {

        return new Message(
                "LOGIN",
                request.getRequestId(),
                "SUCCESS",
                "User logged in",
                ""
        );
    }

    public static Message register(Message request) {

        return new Message(
                "REGISTER",
                request.getRequestId(),
                "SUCCESS",
                "User registered",
                ""
        );
    }
}
