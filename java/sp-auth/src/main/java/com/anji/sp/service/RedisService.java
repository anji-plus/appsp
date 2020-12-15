package com.anji.sp.service;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lide
 * @Title: redis通用方法
 * @date 2018-08-21
 */
public interface RedisService {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    <T> ValueOperations<String, T> setCacheObject(String key, T value);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     * @return 缓存的对象
     */
    <T> ValueOperations<String, T> setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit);

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getCacheObject(String key);

    /**
     * 删除单个对象
     *
     * @param key
     */
    void deleteObject(String key);

    /**
     * 删除集合对象
     *
     * @param collection
     */
    void deleteObject(Collection collection);

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    <T> ListOperations<String, T> setCacheList(String key, List<T> dataList);

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    <T> List<T> getCacheList(String key);

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet);

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    <T> Set<T> getCacheSet(String key);

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap);
    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    <T> Map<String, T> getCacheMap(String key);

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    Collection<String> keys(String pattern);

    /**
     * 自增操作
     *
     * @param key
     * @param by
     * @return
     */
    long incr(String key, long by);

    /**
     * 自增获取
     *
     * @param key
     * @param by
     * @param expiresInSeconds
     * @return
     */
    long incr(String key, long by, long expiresInSeconds);
}