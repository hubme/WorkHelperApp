package com.example.serializable;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * @author VanceKing
 * @since 19-1-27.
 */
class SingletonObject implements Serializable, Cloneable{
    private SingletonObject() {
        
    }

    public static SingletonObject getInstance() {
        return SingleInstanceHolder.sInstance;
    }
    
    private static class SingleInstanceHolder{
        private static final SingletonObject sInstance = new SingletonObject();
    }

    private Object readResolve() throws ObjectStreamException {
        // instead of the object we're on,
        // return the class variable INSTANCE
        return SingleInstanceHolder.sInstance;
    }
}
