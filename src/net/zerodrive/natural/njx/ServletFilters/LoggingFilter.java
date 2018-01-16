package net.zerodrive.natural.njx.ServletFilters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoggingFilter implements Filter {

	private FilterConfig filterConfig = null;
	private String pageDelimLeft = null;
	String pageDelimRight = ".html";
	String methodDelimLeft = ".html";
	String methodDelimRight = "false";
	String sessionDelimLeft = "CASA";
	String sessionDelimRight = "com.softwareag";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String xml = ((HttpServletRequest) request).getParameter("XML");
		String session = getSession(xml);
		String page = getPage(xml);
		String method = getMethod(xml);
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		filterConfig.getServletContext().log("Event: " + session + " " + page + " " + method + " " + (System.currentTimeMillis() - start));
	}

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
		this.pageDelimLeft = filterConfig.getInitParameter("pagePrefix");
	}

	public String getSession(String xml) {
		String session = null;
		if (xml != null) {
			Pattern p = Pattern.compile(sessionDelimLeft + "(.*?)" + sessionDelimRight);
			Matcher m = p.matcher(xml);
			if (m.find()) {
				session = sessionDelimLeft + m.group(1);
			}
		}
		if (session == null || session.isEmpty()) {
			session = " ";
		}
		return session;
	}

	public String getPage(String xml) {
		String page = null;
		if (xml != null) {
			Pattern p = Pattern.compile(pageDelimLeft + "(.*?)" + pageDelimRight);
			Matcher m = p.matcher(xml);
			if (m.find()) {
				page = m.group(1);
			}
		}
		if (page == null || page.isEmpty()) {
			page = " ";
		}
		return page;
	}

	public String getMethod(String xml) {
		String method = null;
		if (xml != null) {
			Pattern p = Pattern.compile(methodDelimLeft + "(.*?)" + methodDelimRight);
			Matcher m = p.matcher(xml);
			if (m.find()) {
				method = m.group(1);
			}
		}
		if (method == null || method.isEmpty()) {
			method = "_init_";
		}
		return method;
	}

}
