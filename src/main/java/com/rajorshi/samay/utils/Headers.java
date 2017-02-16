package com.rajorshi.samay.utils;

import java.util.ArrayList;
import java.util.List;

public enum Headers {
	TXN_ID ( "HTTP_X_TRANSACTION_ID");

	private final String  X_HEADER;
	
	private Headers(String header) {
		X_HEADER = header;
	}
	
	public String getHeader() {
		return X_HEADER;
	}

	public static List<String> getAllHeader() {
		List<String> headers = new ArrayList<>();
		for (Headers header : Headers.values()) {
			headers.add(header.getHeader());
		}
		return headers;
	}
}
