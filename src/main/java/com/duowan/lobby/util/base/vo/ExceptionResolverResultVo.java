package com.duowan.lobby.util.base.vo;

import org.springframework.web.servlet.ModelAndView;

public class ExceptionResolverResultVo {
	private boolean success;
	private ModelAndView modelAndView;

	public ExceptionResolverResultVo(boolean success, ModelAndView modelAndView) {
		super();
		this.success = success;
		this.modelAndView = modelAndView;
	}

	public ExceptionResolverResultVo() {
		super();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ModelAndView getModelAndView() {
		return modelAndView;
	}

	public void setModelAndView(ModelAndView modelAndView) {
		this.modelAndView = modelAndView;
	}

}
