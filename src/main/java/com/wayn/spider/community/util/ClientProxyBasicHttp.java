package com.wayn.spider.community.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientProxyBasicHttp {

    public static void main(String args[]) throws Exception {
        // 目标地址
        String targetUrl = "https://jx.anjuke.com/community/?from=navigation";

        // 代理服务器
        String proxyHost = "221.9.18.42";
        int proxyPort = 43956;

        // http代理: Proxy.Type.HTTP, socks代理: Proxy.Type.SOCKS
        Proxy.Type proxyType = Proxy.Type.HTTP;

        // 代理验证
        String proxyUser = "";
        String proxyPwd = "";

        try {
            // 设置验证
            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPwd));

            // 创建代理服务器
            InetSocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
            Proxy proxy = new Proxy(proxyType, addr);
            // 访问目标网页
            URL url = new URL(targetUrl);
            URLConnection conn = url.openConnection(proxy);
            // 读取返回数据
            InputStream in = conn.getInputStream();
            // 将返回数据转换成字符串
            System.out.println(IO2String(in));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将输入流转换成字符串
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static String IO2String(InputStream inStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, len);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        return str;
    }

    static class ProxyAuthenticator extends Authenticator {
        private String authUser, authPwd;

        public ProxyAuthenticator(String authUser, String authPwd) {
            this.authUser = authUser;
            this.authPwd = authPwd;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(authUser, authPwd.toCharArray()));
        }
    }
}
