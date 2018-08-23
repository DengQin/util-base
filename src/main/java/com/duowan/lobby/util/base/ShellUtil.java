package com.duowan.lobby.util.base;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.vo.RunShellResult;

/**
 * 执行shell命令
 * 
 * @author tanjh 2012-9-27
 */
public class ShellUtil {
	private static Logger log = LoggerFactory.getLogger(ShellUtil.class);

	public static RunShellResult runShell(String cmd) throws Exception {
		String[] cmds = { "/bin/sh", "-c", cmd };
		Process p = Runtime.getRuntime().exec(cmds);
		InputStream input = p.getErrorStream();
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = input.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		RunShellResult result = new RunShellResult();
		String errorMsg = new String(buffer.toString().getBytes("iso-8859-1"), "utf-8");
		result.setErrorMsg(errorMsg);
		log.info("错误信息：" + errorMsg);
		input = p.getInputStream();
		buffer = new StringBuffer();
		for (;;) {
			int c = input.read();
			if (c == -1)
				break;
			buffer.append((char) c);
		}
		String infoMsg = new String(buffer.toString().getBytes("iso-8859-1"), "utf-8");
		result.setInfoMsg(infoMsg);
		log.info("提示信息：" + infoMsg);
		return result;
	}
}
