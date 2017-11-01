package com.hybhub.atp.Exception;

public class ParseStringException extends Exception {

    public ParseStringException() {
    }

    public ParseStringException(String s) {
        super(s);
    }

    public ParseStringException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParseStringException(Throwable throwable) {
        super(throwable);
    }
}
