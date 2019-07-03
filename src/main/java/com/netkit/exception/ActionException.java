package com.netkit.exception;

public class ActionException extends Exception {

    private static final long serialVersionUID = -8685590849631414583L;

    public ActionException() {
    }
    
    public ActionException(String message) {
        super(message);
    }
    
    public ActionException(Throwable e){
        super(e);
    }
    
    public ActionException(Throwable e, String message){
        super(message, e);
    }
    
}
