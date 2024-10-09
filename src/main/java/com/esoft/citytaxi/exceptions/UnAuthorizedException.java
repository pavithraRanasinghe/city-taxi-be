package com.esoft.citytaxi.exceptions;

/**
 * The type Un authorized exception.
 *
 */
public class UnAuthorizedException extends RuntimeException {

    /**
     * Instantiates a new Un authorized exception.
     */
    public UnAuthorizedException() {
        super();
    }

    /**
     * Instantiates a new Un authorized exception.
     *
     * @param msg the msg
     */
    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
