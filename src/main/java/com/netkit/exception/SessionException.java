package com.netkit.exception;

public class SessionException extends RuntimeException {
    
    private static final long serialVersionUID = 4355490979786852770L;

    public SessionException() {
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Throwable e) {
        super(e);
    }

    public SessionException(String message, Throwable e) {
        super(message, e);
    }
    
}
