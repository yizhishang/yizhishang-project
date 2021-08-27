package com.yizhishang.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * https 请求
 *
 * @author yizhishang
 */
@Slf4j
public class HttpsUtils implements ProtocolSocketFactory {

    private static volatile SSLContext sslcontext = null;

    private static final String HTTPS = "https";

    private static SSLContext getSSLContext() {
        if (sslcontext == null) {
            synchronized (HttpUtils.class) {
                if (sslcontext == null) {
                    sslcontext = createSSLContext();
                }
            }
        }
        return sslcontext;
    }

    private static SSLContext createSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("createSSLContext方法报错", e);
        }
        return sslContext;
    }

    public static String sendGetRequest(String url) {
        Protocol https = new Protocol(HTTPS, new HttpsUtils(), 443);
        Protocol.registerProtocol(HTTPS, https);
        GetMethod get = new GetMethod(url);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            String result = get.getResponseBodyAsString();
            Protocol.unregisterProtocol(HTTPS);
            return result;
        } catch (Exception e) {
            log.error("sendGetRequest方法报错", e);
        }
        return "error";
    }

    public static String sendPostRequest(String url, Map<String, String> params) {
        Protocol https = new Protocol(HTTPS, new HttpsUtils(), 443);
        Protocol.registerProtocol(HTTPS, https);
        PostMethod post = new PostMethod(url);
        if (null != params && !params.isEmpty()) {
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
        }

        try {
            HttpClient client = new HttpClient();
            client.executeMethod(post);
            String result = post.getResponseBodyAsString();
            Protocol.unregisterProtocol(HTTPS);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "error";
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        }
        try (Socket socket = socketfactory.createSocket()) {
            SocketAddress localAddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteAddr = new InetSocketAddress(host, port);
            socket.bind(localAddr);
            socket.connect(remoteAddr, timeout);
            return socket;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

    }
}
