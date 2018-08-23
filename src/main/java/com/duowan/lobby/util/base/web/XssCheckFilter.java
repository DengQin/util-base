/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duowan.lobby.util.base.web;

import com.duowan.lobby.util.base.XssUtil;
import com.duowan.lobby.util.base.exception.XssInvalidException;

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
 * xss检查过滤器 <filter> <filter-name>XssCheckFilter</filter-name>
 * <filter-class>com.duowan.lobby.util.base.web.XssCheckFilter</filter-class>
 * <!-- 忽略接口 --> <init-param> <param-name>ignoreUrl</param-name>
 * <param-value>/webservice/abc.do,/webservice/to.do</param-value> </init-param>
 * </filter> <filter-mapping> <filter-name>XssCheckFilter</filter-name>
 * <url-pattern>*.do</url-pattern> </filter-mapping>
 *
 * @author lixinchao
 */
public class XssCheckFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(XssCheckFilter.class);
    // 忽略接口,多个以","隔开
    private static String IGNORE_URLS = "";

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
        boolean hasXss = false;
        if (!isIgnore) {
            long t1 = System.currentTimeMillis();
            try {
                XssUtil.checkAllXSS(req.getParameterMap());
            } catch (XssInvalidException e) {
                log.warn("xss check alarm", e);
                hasXss = true;
            }
            long t2 = System.currentTimeMillis();
            if (t2 - t1 > 5) {
                log.info("xss check useT:" + (t2 - t1));
            }
        }
        if (!hasXss) {
            chain.doFilter(req, resp);
        } else {
            throw new XssInvalidException("XSS Exception");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("XssCheckFilter init");
        String ignoreUrl = filterConfig.getInitParameter("ignoreUrl");
        IGNORE_URLS = "," + ignoreUrl + ",";
        log.info("ignoreUrl:" + IGNORE_URLS);
    }
}
