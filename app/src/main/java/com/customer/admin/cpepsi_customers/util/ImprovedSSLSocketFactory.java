package com.customer.admin.cpepsi_customers.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ImprovedSSLSocketFactory extends  SSLSocketFactory    {
    //    SSLSocketFactory _factory = this(Defaul);
    SSLSocketFactory factory = (SSLSocketFactory) getDefault();
    @Override
    public String[] getDefaultCipherSuites() {
        return factory.getDefaultCipherSuites();
//        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
//        return new String[0];
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {

        SSLSocket socket = (SSLSocket)factory.createSocket(s,host,port,autoClose);

        socket.setEnabledProtocols(socket.getSupportedProtocols());
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket)factory.createSocket(host,port);

        socket.setEnabledProtocols(socket.getSupportedProtocols());
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket)factory.createSocket(host,port,localHost,localPort);

        socket.setEnabledProtocols(socket.getSupportedProtocols());
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket socket = (SSLSocket)factory.createSocket(host,port);

        socket.setEnabledProtocols(socket.getSupportedProtocols());
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocket socket = (SSLSocket) factory.createSocket(address,port,localAddress,localPort);
        socket.setEnabledProtocols(socket.getSupportedProtocols());

        return socket;
    }

    @Override
    public Socket createSocket() throws IOException {
        SSLSocket socket = (SSLSocket)factory.createSocket();
        socket.setEnabledProtocols(socket.getSupportedProtocols());
        return socket;
    }
}