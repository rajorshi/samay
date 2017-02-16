package com.rajorshi.samay.utils;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class TransactionFilter implements Filter {
	private static final String TXN_ID = "HTTP_X_TRANSACTION_ID";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		try {
			MDC.put("txnId", createTxnId(httpServletRequest, httpServletResponse));
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			ClientHeaders.removeAll();
			MDC.remove("txnId");
		}
	}

	private String createTxnId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String txnId = httpServletRequest.getHeader(TXN_ID) == null ? createTxnId(httpServletResponse)
				: httpServletRequest.getHeader(TXN_ID);
		setResponse(httpServletRequest,httpServletResponse);
		
		return txnId;
	}

	private void setResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		List<String> allHeader = Headers.getAllHeader();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String header = (String) headerNames.nextElement();
			if (allHeader.contains(header)) {
				httpServletResponse.addHeader(header, httpServletRequest.getHeader(header));
				ClientHeaders.put(header, httpServletRequest.getHeader(header));
			}
		}
	}

	private String createTxnId(HttpServletResponse httpServletResponse) {
		String txnId = "TXN-" + UUID.randomUUID().toString();
		httpServletResponse.addHeader(Headers.TXN_ID.getHeader(),txnId);
		ClientHeaders.put(Headers.TXN_ID.getHeader(), txnId);
		return txnId;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
