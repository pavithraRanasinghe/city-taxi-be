package com.esoft.citytaxi.exceptions;

public class EntityExistsException extends RuntimeException {
    /**
     * Instantiates a new Entity exists exception.
     *
     * @param id the id
     */
    public EntityExistsException(Long id) {
        super("Existing entity found for id - " + id);
    }

    public EntityExistsException(String property, String value) {
        super("Existing entity found for " + property + " - " + value);
    }

}
