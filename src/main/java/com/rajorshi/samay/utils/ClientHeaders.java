package com.rajorshi.samay.utils;

import java.util.HashMap;
import java.util.Map;

public class ClientHeaders {
	private static final ThreadLocal<Map<String, String>> CURRENT = new ThreadLocal<>();

	public static Map<String, String> get() {
		if (CURRENT.get() == null) 
			CURRENT.set(new HashMap<>());
		return CURRENT.get();
	}

	public static String getByKey(String key) {
		Map<String, String> headers = get();
		return headers.get(key);
	}
	
	public static void removeAll() {
		Map<String, String> headers = get();
		headers.clear();
	}
	
	
	public static Map<String, String> put(String key, String value) {
		Map<String, String> headers = get();
		headers.put(key, value);
		return headers;
	}

	public static Map<String, String> putAll(Map<String, String> map) {
		Map<String, String> headers = get();
		headers.putAll(map);
		return headers;
	}
}
