package com.duowan.lobby.util.base.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.JsonUtil;
import com.duowan.lobby.util.base.StringUtil;

/**
 * json视图.
 * 
 */
public class JsonView extends AbstractView {

	private static Logger log = LoggerFactory.getLogger(JsonView.class);

	private int status;

	private String message;

	private Object data;

	public JsonView() {
	}

	public JsonView(Object data) {
		this(200, data);
	}

	public JsonView(int status, Object data) {
		this.status = status;
		this.data = data;
		this.message = "";
	}

	public JsonView(int status, Object data, String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public static JsonView success() {
		return new JsonView(200, null, "");
	}

	public static JsonView success(Object obj) {
		return new JsonView(200, obj, "");
	}

	public static JsonView fail(String message) {
		return new JsonView(-1, null, message);
	}

	public static JsonView fail(int status, String message) {
		return new JsonView(status, null, message);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

	private Map<String, Object> getResult() {
		Map<String, Object> map = new HashMap<String, Object>(4, 1);
		map.put("status", this.status);
		map.put("message", this.message);
		map.put("data", this.data);
		return map;
	}

	/**
	 * 
	 * 1、默认返回json <br/>
	 * URL:http://xxx/test/json.do<br/>
	 * 返回:{"status":200,"message":"","data":{xx}}<br/>
	 * 
	 * 2、自定义callback参数，返回jsonp<br/>
	 * URL:http://xxx/test/json.do?callback=callback2 <br/>
	 * 返回:callback2({"status":200,"message":"","data":{xx}}); <br/>
	 * 
	 * 3、自定义var参数，返回script <br/>
	 * URL:http://xxx/test/json.do?var=abc <br/>
	 * 返回:var abc = {"status":200,"message":"","data":{xx}} ; <br/>
	 * 
	 */
	@Override
	public String getBody(HttpServletRequest request) {
		try {
			return _getBody(request, JsonUtil.toJson(getResult()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return _getBody(request, "{\"status\":-1,\"message\":\"发生错误\",\"data\":null}");
		}
	}

	private String _getBody(HttpServletRequest request, String json) {
		String callback = request.getParameter("callback");
		if (StringUtil.isNotBlank(callback) && isValidCallback(callback)) {
			return callback + "(" + json + ");";
		}
		String var = request.getParameter("var");
		if (StringUtil.isNotBlank(var) && isValidCallback(var)) {
			return "var " + var + " = " + json + ";";
		}
		return json;
	}

	private static String VALID_REGEX = "^[a-zA-Z0-9_\\.]+$";

	private static boolean isValidCallback(String callback) {
		return callback.matches(VALID_REGEX);
	}

	@Override
	public String toString() {
		return "JsonView [status=" + status + ", message=" + message + ", data=" + data + "]";
	}

}
