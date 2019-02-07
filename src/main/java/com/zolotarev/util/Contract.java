package com.zolotarev.util;

/**
 * Contains utility methods for check preconditions of any method
 */
public final class Contract {

    /**
     * @param condition The condition which must be satisfied
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if the condition is not satisfied
     */
    public static void requires(boolean condition, String message) {
        if(!condition)
            throw new IllegalArgumentException(message);
    }

    /**
     * @param object Object to be checked
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if an object is null
     */
    public static void requiresNotNull(Object object, String message) {
        requires(object != null, message);
    }

    /**
     * @param object Object to be checked
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if an object is not null
     */
    public static void requiresNull(Object object, String message) {
        requires(object == null, message);
    }

    /**
     * @param first First object to equality test
     * @param second Second object to equality test
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if first parameter equals second parameter
     */
    public static void requiresNotEquals(Object first, Object second, String message) {
        requires(!first.equals(second), message);
    }

    /**
     * @param first First object to compare
     * @param second Second object to compare
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if first parameter less or equals than second parameter
     */
    public static <T extends Comparable<T>> void requiresMore(T first, T second, String message) {
        requires(first.compareTo(second) > 0, message);
    }

    /**
     * @param first First object to compare
     * @param second Second object to compare
     * @param message An information, which will be passed into exception's constructor
     * @throws IllegalArgumentException if first parameter less than second parameter
     */
    public static <T extends Comparable<T>> void requiresMoreOrEquals(T first, T second, String message) {
        requires(first.compareTo(second) >= 0, message);
    }
}
