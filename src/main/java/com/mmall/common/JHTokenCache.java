package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JHTokenCache {

	private static String cacheUnHitDefaultValue = "null";

	//LRU算法
	private static LoadingCache<String, String> loadingCache =
			CacheBuilder.newBuilder()
					.initialCapacity(1000)
					.maximumSize(10000)
					.expireAfterAccess(10, TimeUnit.MINUTES)
					.build(new CacheLoader<String, String>() {
						//默认的数据加载实现，当调用get聚会的时候，如果key没有对应的值，就调用这个方法进行加载
						@Override
						public String load(String s) throws Exception {
							return cacheUnHitDefaultValue;
						}
					});

	private static String getKey(String key) {
		return "token_" + key;
	}

	public static void setKey(String key, String value) {
		loadingCache.put(getKey(key), value);
	}

	public static String getValue(String key) {
		String value = null;
		try {
			value = loadingCache.get(getKey(key));
			if (cacheUnHitDefaultValue.equals(value)) {
				value = null;
			}
		} catch (ExecutionException e) {
			log.error("localCache get error");
		}
		return value;
	}
}
