package com.duowan.lobby.util.base;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtils {
	private static Logger log = LoggerFactory.getLogger(IpUtils.class);

	public static String getIp(String hostName) {
		try {
			InetAddress address = InetAddress.getByName(hostName);
			return address.getHostAddress().toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static Set<String> listIpsByHost(String hostName) throws UnknownHostException {
		if (StringUtil.isBlank(hostName)) {
			return Collections.emptySet();
		}
		Set<String> ips = new HashSet<String>();
		InetAddress[] array = InetAddress.getAllByName(hostName);
		// 遍历获取的域名所有的IP
		for (int i = 0; i < array.length; i++) {
			String host = array[i].getHostAddress();
			ips.add(host);
		}
		return ips;
	}

	public static Set<String> listLocalIps() {
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
		InetAddress inetAddress = null;
		Set<String> ips = new HashSet<String>();
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				inetAddress = addresses.nextElement();
				if (inetAddress != null && inetAddress instanceof Inet4Address) {
					String ip = inetAddress.getHostAddress();
					if ("127.0.0.1".equals(StringUtil.trim(ip))) {
						continue;
					}
					if ("localhost".equals(inetAddress.getHostName())) {
						continue;
					}
					ips.add(ip);
				}
			}
		}
		try {
			String result = NetUtil.getURL("http://isp.duowan.com/");
			int start = result.indexOf("data=") + 5;
			int end = result.lastIndexOf("}") + 1;
			if (start < end) {
				result = result.substring(start, end);
				String s = (String) JsonUtil.toMap(result).get("ip");
				if (StringUtil.isNotBlank(s)) {
					ips.add(s);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return ips;
	}

	public static String getLocalIp() {
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return "";
		}
		InetAddress inetAddress = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				inetAddress = addresses.nextElement();
				if (inetAddress != null && inetAddress instanceof Inet4Address) {
					String ip = inetAddress.getHostAddress();
					if ("127.0.0.1".equals(StringUtil.trim(ip))) {
						continue;
					}
					if ("localhost".equals(inetAddress.getHostName())) {
						continue;
					}
					return ip;
				}
			}
		}
		return "";
	}

}
