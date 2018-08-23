/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.duowan.lobby.util.base.exception;

/**
 *
 * @author lixinchao
 */
public class CsrfTokenInvalidException  extends SecurityException {

	private static final long serialVersionUID = 1L;

	public CsrfTokenInvalidException(String message) {
		super(message);
	}

}
