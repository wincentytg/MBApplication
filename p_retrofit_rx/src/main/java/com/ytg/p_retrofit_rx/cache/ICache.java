package com.ytg.p_retrofit_rx.cache;

/**
 *
 * @author 于堂刚
 */

public interface ICache {

    void put(String key, Object value);

    Object get(String key);

    void remove(String key);

    boolean contains(String key);

    void clear();

}
