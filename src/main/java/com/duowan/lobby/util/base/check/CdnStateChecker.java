package com.duowan.lobby.util.base.check;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.IntUtil;
import com.duowan.lobby.util.base.LongUtil;
import com.duowan.lobby.util.base.StringUtil;

public class CdnStateChecker {
	protected static final Logger log = LoggerFactory.getLogger(CdnStateChecker.class);

	private static String reqType = "GET";
	private static String httpType = "HTTP/1.1";

	/**
	 * 获取当前日期的字符串
	 * 
	 * @param url
	 *            访问地址
	 * 
	 * @param fileLength
	 *            文件长度，小于1为不检查
	 */
	public static boolean check(String url, long fileLength) {
		String host = StringUtil.getBetweenString(url, "http://", "/");
		String head = send(host, 80, url, false);
		if (StringUtil.isBlank(head)) {
			return false;
		}
		if (fileLength < 1) {
			return true;
		}
		String lenthStr = StringUtil.getBetweenString(head, "content-length: ", "\r\n");
		long lenth = LongUtil.parseLong(lenthStr);
		if (fileLength == lenth) {
			return true;
		}
		log.info("访问地址[{}]的文件长度为[{}],不等于参数fileLength[{}]", new Object[] { url, lenth, fileLength });
		return false;
	}

	private static String send(String host, int port, String reqUrl, boolean getContent) {
		try {
			Socket socket = new Socket(host, port);
			socket.setSoTimeout(6000);// 6s
			socket.setKeepAlive(true);
			String s = getHeaderInfo(reqUrl, host);
			socket.getOutputStream().write(s.getBytes());

			List<Integer> list = new LinkedList<Integer>();
			while (true) {
				int i = socket.getInputStream().read();
				list.add(i);
				if (i == 10) {
					int len = list.size();
					if (list.get(len - 2) == 13 && list.get(len - 3) == 10 && list.get(len - 4) == 13) {
						break;
					}
				}
			}
			StringBuilder sb = new StringBuilder();
			for (Integer integer : list) {
				sb.append((char) integer.intValue());
			}
			String head = sb.toString().toLowerCase();
			// log.info("访问头部\n" + head);
			if (!getContent) {
				if (head.startsWith("http/1.1 200 ok") || head.startsWith("http/1.0 200 ok")) {
					return head;
				} else {
					return null;
				}
			}
			int len = IntUtil.parseNonnegative(StringUtil.getBetweenString(head, "content-length:", "\n"));
			if (len < 1) {
				return null;
			}
			sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				sb.append((char) socket.getInputStream().read());
			}
			String content = sb.toString();
			// log.info("访问内容\n" + content);
			return content;
		} catch (Exception e) {
			return null;
		}
	}

	private static String getHeaderInfo(String reqUrl, String host) {
		String line = reqType + " " + reqUrl + " " + httpType + "\r\n";
		line += "Accept: application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		line += "Accept-Encoding: gzip, deflate\r\n";
		line += "Accept-Language: zh-CN\r\n";
		line += "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)\r\n";
		line += "Host: " + host + "\r\n";
		line += "Connection: Keep-Alive\r\n";
		return line + "\r\n";
	}
}