package com.yizhishang.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
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

    private SSLContext sslcontext = null;

    public static String sendGetRequest(String url) {
        String result;
        Protocol https = new Protocol("https", new HttpsUtils(), 443);
        Protocol.registerProtocol("https", https);
        GetMethod get = new GetMethod(url);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            result = get.getResponseBodyAsString();
            Protocol.unregisterProtocol("https");
            return result;
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "error";
    }

    public static String sendPostRequest(String url, Map<String, String> params) {
        String result = "";
        Protocol https = new Protocol("https", new HttpsUtils(), 443);
        Protocol.registerProtocol("https", https);
        PostMethod post = new PostMethod(url);
        if (null != params && !params.isEmpty()) {
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpClient client = new HttpClient();
        try {
            client.executeMethod(post);
            result = post.getResponseBodyAsString();
            Protocol.unregisterProtocol("https");
            return result;
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "error";
    }

    private SSLContext createSSLContext() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslcontext;
    }

    private SSLContext getSSLContext() {
        if (null == this.sslcontext) {
            this.sslcontext = createSSLContext();
        }
        return this.sslcontext;
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
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
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
