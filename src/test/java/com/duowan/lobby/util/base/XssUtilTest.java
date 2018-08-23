/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duowan.lobby.util.base;

import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class XssUtilTest {

    /**
     * 随机出32个字符,字母、数字和中划线的组合
     *
     * @return
     */
    public static final String random32Chars() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    static class MThread extends Thread {

        public void run() {
            int max = Integer.MAX_VALUE;
            max = 100000;
            long t1 = System.currentTimeMillis();
            int i = 0;
            for (; i < max; i++) {
                XssUtil.hasXSS(random32Chars());
            }
            long t2 = System.currentTimeMillis();
            System.out.println("runtimes:" + i + ",useT:" + (t2 - t1) + ",cur:" + t2);
        }
    }

    public static void main(String[] args) {
        System.out.println("start:" + System.currentTimeMillis());
        for(int i=0 ;i<30 ;i++) {
            new MThread().start();
        }
    }
}
