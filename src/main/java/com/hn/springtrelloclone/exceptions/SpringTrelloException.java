package com.hn.springtrelloclone.exceptions;

public class SpringTrelloException extends RuntimeException {
    public SpringTrelloException(String exMessage, Exception exception){
        super(exMessage,exception);
    }

    public SpringTrelloException(String exMessage){
        super(exMessage);
    }
}
