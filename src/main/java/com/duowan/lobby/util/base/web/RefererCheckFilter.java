/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duowan.lobby.util.base.web;

import com.duowan.lobby.util.base.StringUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * Redirect检查过滤器
<filter>
    <filter-name>RefererCheckFilter</filter-name>
    <filter-class>com.duowan.lobby.util.base.web.RefererCheckFilter</filter-class>
    <!-- host白名单,多个以","隔开 -->
    <init-param>
        <param-name>hostWhiteList</param-name>
        <param-value>sz.duowan.com,udb.duowan.com,szhuodong.duowan.com,wan.yy.com,zadan.duowan.com</param-value>
    </init-param>
    <!-- 忽略接口 -->
    <init-param>
        <param-name>ignoreUrl</param-name>
        <param-value>/webservice/abc.do,/webservice/to.do</param-value>
    </init-param>
 </filter>
<filter-mapping>
    <filter-name>RefererCheckFilter</filter-name>
    <url-pattern>/*.do</url-pattern>
</filter-mapping>
 *
 * @author lixinchao
 */
public class RefererCheckFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(RefererCheckFilter.class);
    // host白名单,多个以","隔开
    private static Set<String> HOST_WHITELIST = new HashSet<String>() ;
    // 忽略接口,多个以","隔开
    private static String IGNORE_URLS = "" ;
    
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;//获得request对象
        HttpServletResponse resp = (HttpServletResponse) response;//获得response对象

        String requestUrl = req.getRequestURI() ;
        boolean isIgnore = IGNORE_URLS.contains("," + requestUrl + ',') ;  
        
        String referer = req.getHeader("referer") ;
        String refererHost = parseHost(referer);
        // 忽略，同域，在白名单里
        boolean isPass = isIgnore ;
        if(! isPass) {
            isPass = checkReferer(refererHost ,req.getServerName()) ;
        }
        if(isPass) {
            chain.doFilter(req, resp);
        } else {
            log.info("Referer 检查受限:" + referer + ",uri:" + req.getRequestURL());
            resp.getOutputStream().close();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String hostWhiteList = filterConfig.getInitParameter("hostWhiteList") ;
        HOST_WHITELIST.addAll(str2List(hostWhiteList ,","));

        String ignoreUrl = filterConfig.getInitParameter("ignoreUrl") ;
        IGNORE_URLS = "," + ignoreUrl + "," ;
        if(! IGNORE_URLS.contains("/monitor/monitor.do")) {
        	IGNORE_URLS += "/monitor/monitor.do," ;
        }
        if(! IGNORE_URLS.contains("/udb/callback.do")) {
            IGNORE_URLS += "/udb/callback.do," ;
        }
        log.info("hostWhiteList:" + hostWhiteList + ",setsize:" + HOST_WHITELIST.size() + ",ignoreUrl:" + IGNORE_URLS);
    }
    
    static private String parseHost(String referer) {        
        if(StringUtil.isBlank(referer)) {
            return referer ;
        }
        String checkReferer = referer.toLowerCase() ;
        if(!checkReferer.startsWith("http://")) {
            return "" ;
        }
        
        int pos1 = checkReferer.indexOf("http://") ;
        if(pos1==-1) {
            return checkReferer ;
        }
        int pos2 = checkReferer.indexOf("/" ,pos1+7) ;
        if(pos2==-1) {
            return checkReferer ;
        }
        return checkReferer.substring(pos1+7 ,pos2) ;
    }
    
    static private List<String> str2List(String strs ,String splitChar) {        
        List<String> retList = new ArrayList<String>() ;
        if(StringUtil.isNotBlank(strs)) {
            String[] arr = strs.split(splitChar) ;
            for(String item : arr) {
                if(StringUtil.isBlank(item)) {
                    continue ;
                }
                retList.add(item);
            }
        }
        return retList ;
    }
    
    /**
     * 检查是否受限
     * @param strs
     * @param splitChar
     * @return 
     */
    private boolean checkReferer(String refererHost ,String serverName) {
        if(StringUtil.isBlank(refererHost)) {
            return false ;
        }
        if(refererHost.equalsIgnoreCase(serverName)) {
            return true ;
        }
        for(String whiteHost : HOST_WHITELIST) {            
            if(refererHost.equals(whiteHost)) {
                return true ;
            }
            // 如果相同或前缀带*,如*.game.duowan.com
            if(whiteHost.indexOf("*") != -1) {
                int pos = whiteHost.indexOf("*") ;
                String postHost = whiteHost.substring(pos + 1) ;
                if(refererHost.endsWith(postHost)) {
                    return true ;
                }
            }
        }
        return false ;
    }
    
    
//    public static void main(String[] args) {
//        String host = parseHost("http://abc.test.duowan.com/play/awardInfo.do") ;
//        HOST_WHITELIST.add("*.test.duowan.com") ;
//    }
}
