package com.duowan.lobby.util.base;

import java.io.File;

public class EncryptUtilDemo {

	public static void main(String[] args) {
		File file = new File("D:\\build_me.rar");
		System.out.println(EncryptUtil.getFileMd5String(file));
	}
}
