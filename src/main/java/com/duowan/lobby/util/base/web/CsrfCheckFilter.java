/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duowan.lobby.util.base.web;

import com.duowan.lobby.util.base.CookieUtil;
import com.duowan.lobby.util.base.EncryptUtil;
import com.duowan.lobby.util.base.Util;
import com.duowan.lobby.util.base.exception.CsrfTokenInvalidException;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * csrf检查过滤器 如果允许用户不登录直接使用服务，需要由业务服务端提供登录接口，所有未登录用户都要先请求登录接口，并把登录接口加入ignoreUrl
 * <filter>
 * <filter-name>CsrfCheckFilter</filter-name>
 * <filter-class>com.duowan.lobby.util.base.web.CsrfCheckFilter</filter-class>
 * <!-- 忽略接口 -->
 * <init-param>
 * <param-name>ignoreUrl</param-name>
 * <param-value>/webservice/abc.do,/webservice/to.do</param-value>
 * </init-param>
 * <!-- 是否仅写日志:true|false -->
 * <init-param>
 * <param-name>logOnly</param-name>
 * <param-value>true</param-value>
 * </init-param>
 * </filter>
 * <filter-mapping>
 * <filter-name>CsrfCheckFilter</filter-name>
 * <url-pattern>*.do</url-pattern>
 * </filter-mapping>
 *
 * @author lixinchao
 */
public class CsrfCheckFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CsrfCheckFilter.class);

    public static final String publicKey = "csrfTokenKeyxx123";
    public static final String PARAMETER_NAME_CSRF_TOKEN = "csrf-token";
    // 忽略接口,多个以","隔开
    private static String IGNORE_URLS = "";
    // 是否只记日志
    private static boolean isLogOnly = true;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;//获得request对象
        HttpServletResponse resp = (HttpServletResponse) response;//获得response对象

        String requestUrl = req.getRequestURI();
        boolean isIgnore = IGNORE_URLS.contains("," + requestUrl + ',');
        if (!isIgnore) {
            String username = CookieUtil.getCookie("username", req);
            String token = request.getParameter(PARAMETER_NAME_CSRF_TOKEN);
            String proxyIp = Util.getRealIp(req);
            boolean isPass = checkToken(username, token);
            if (!isPass) {
                log.warn("check csrf fail ,token:" + token + ",username:" + username + ",proxyIp:" + proxyIp + ",requestUrl:" + requestUrl);

                if (!isLogOnly) {
                    throw new CsrfTokenInvalidException("非法token");
                }
            }
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String ignoreUrl = filterConfig.getInitParameter("ignoreUrl");
        IGNORE_URLS = "," + ignoreUrl + ",";
        if (!IGNORE_URLS.contains("/monitor/monitor.do")) {
            IGNORE_URLS += "/monitor/monitor.do,";
        }

        String logOnly = filterConfig.getInitParameter("logOnly");
        if (!"TRUE".equalsIgnoreCase(logOnly)) {
            isLogOnly = false;
        }
        log.info("ignoreUrl:" + IGNORE_URLS + ",isLogOnly:" + isLogOnly);
    }

    private boolean checkToken(String username, String token) {
        if (token == null || "".equals(token.trim())) {
            return false;
        }

        String[] list = token.split("-");
        if (list.length != 3) {
            return false;
        }

        String prefix = list[0];
        long time = Long.parseLong(list[2]);
        String sha1 = EncryptUtil.getSHA1(time + " " + username + " " + publicKey); // 使用SHA1加密算法
        if (!sha1.startsWith(prefix)) {
            log.warn("prefix:" + prefix + " username:" + username + " time:" + time + " sha1:" + sha1 + " token:" + token);
            return false;
        }
        return true;
    }

}
