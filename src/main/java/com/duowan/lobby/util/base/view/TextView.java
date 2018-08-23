package com.duowan.lobby.util.base.view;

import javax.servlet.http.HttpServletRequest;

public class TextView extends AbstractView {

	private String message;

	public TextView() {
		super();
		this.message = "";
	}

	public TextView(String message) {
		this.message = message;
	}

	public static TextView create(String message) {
		return new TextView(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getContentType() {
		return "text/plain; charset=UTF-8";
	}

	@Override
	public String getBody(HttpServletRequest request) {
		return this.getMessage();
	}
}
