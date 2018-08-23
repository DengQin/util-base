package com.duowan.lobby.util.base.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.IOUtil;
import com.duowan.lobby.util.base.StringUtil;

public class CdnFileChecker {
	private static final Logger log = LoggerFactory.getLogger(CdnFileChecker.class);
	private static ThreadSafeClientConnManager cm = null;
	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		cm = new ThreadSafeClientConnManager(schemeRegistry);
		cm.setMaxTotal(10);
		// 每条通道的并发连接数设置（连接池）
		cm.setDefaultMaxPerRoute(10);
	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000); // 6000ms
		return new DefaultHttpClient(cm, params);
	}

	private static boolean compareStream(InputStream is1, InputStream is2) throws Exception {
		byte[] arr1 = new byte[128];
		byte[] arr2 = new byte[128];
		while (is1.read(arr1) > -1) {
			is2.read(arr2);
			if (!Arrays.equals(arr1, arr2)) {
				return false;
			}
		}
		return true;
	}

	public static boolean check(String url, String filePathName) {
		if (StringUtil.isBlank(url) || StringUtil.isBlank(filePathName)) {
			return false;
		}
		return compareContent(url, filePathName);
	}

	/**
	 * 比较网络和本地文件内容
	 */
	private static boolean compareContent(String url, String filePathName) {
		HttpGet get = null;
		InputStream is1 = null, is2 = null;
		try {
			HttpClient client = getHttpClient();
			get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				is1 = entity.getContent();

				File file = new File(filePathName);
				if (!file.exists()) {
					return false;
				}
				is2 = new FileInputStream(file);
				return compareStream(is1, is2);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtil.closeQuietly(is1);
			IOUtil.closeQuietly(is2);
			if (get != null) {
				get.abort();
			}
		}
		return false;
	}

}