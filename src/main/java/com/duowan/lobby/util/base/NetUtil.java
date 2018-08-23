package com.duowan.lobby.util.base;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtil {

	private static final Logger log = LoggerFactory.getLogger(NetUtil.class);
	private static ThreadSafeClientConnManager cm = null;
	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		cm = new ThreadSafeClientConnManager(schemeRegistry);
		cm.setMaxTotal(10);
		// 每条通道的并发连接数设置（连接池）
		cm.setDefaultMaxPerRoute(10);
	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000); // 6s
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000); // 6s
		return new DefaultHttpClient(cm, params);
	}

	/**
	 * 获取网络文件内容
	 * 
	 * @throws Exception
	 */
	public static String postURL(String url) throws Exception {
		return postURL(url, "UTF-8");
	}

	/**
	 * 获取网络文件内容
	 * 
	 * @throws Exception
	 */
	public static String postURL(String url, String charset) throws Exception {
		HttpPost post = null;
		InputStream is = null;
		try {
			HttpClient client = getHttpClient();
			post = new HttpPost(url);
			HttpResponse response = client.execute(post);
			client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, charset);
			} else {
				log.error("请求[{}]失败，返回[{}]", new Object[] { url, response.toString() });
				return "";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			IOUtil.closeQuietly(is);
			if (post != null) {
				post.abort();
			}
		}
	}
	
	public static String postURL(String url, Map<String, String> params) throws Exception {
		return postURL(url, "utf-8", params);
	}

	/**
	 * 获取网络文件内容
	 */
	public static String postURL(String url, String charset, Map<String, String> params) throws Exception {
		HttpPost post = null;
		InputStream is = null;
		try {
			HttpClient client = getHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
			post = new HttpPost(url);
			List<BasicNameValuePair> paramlist = new LinkedList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
				paramlist.add(pair);
			}
			// 设置参数
			post.setEntity(new UrlEncodedFormEntity(paramlist, charset));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, charset);
			} else {
				log.error("请求[{}]失败,参数[{}],返回[{}]", new Object[] { url, params, response.toString() });
				return "";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			IOUtil.closeQuietly(is);
			if (post != null) {
				post.abort();
			}
		}
	}

	public static String getURL(String url) throws Exception {
		return getURL(url, "UTF-8");
	}

	/**
	 * 获取网络文件内容
	 * 
	 * @throws Exception
	 */
	public static String getURL(String url, String charset) throws Exception {
		HttpGet get = null;
		InputStream is = null;
		try {
			HttpClient client = getHttpClient();
			get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, charset);
			} else {
				log.error("请求[{}]失败，返回[{}]", new Object[] { url, response.toString() });
				return "";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			IOUtil.closeQuietly(is);
			if (get != null) {
				get.abort();
			}
		}
	}

	public static String encode(String str) {
		return encode(str, "utf-8");
	}

	public static String encode(String str, String enc) {
		if (StringUtil.isBlank(str)) {
			return "";
		}
		try {
			return URLEncoder.encode(str, enc);
		} catch (UnsupportedEncodingException e) {
			log.error("编码[" + str + "]出错" + e.getMessage(), e);
			return str;
		}
	}

	public static String decode(String str) {
		return decode(str, "utf-8");
	}

	public static String decode(String str, String enc) {
		if (StringUtil.isBlank(str)) {
			return "";
		}
		try {
			return URLDecoder.decode(str, enc);
		} catch (UnsupportedEncodingException e) {
			log.error("解码[" + str + "]出错" + e.getMessage(), e);
			return str;
		}
	}
}
