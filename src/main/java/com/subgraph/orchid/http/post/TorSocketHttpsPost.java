package com.subgraph.orchid.http.post;

import com.subgraph.orchid.http.NameValuePair;
import com.subgraph.orchid.http.TorClientFactory;
import com.subgraph.orchid.http.TorSocketStream;
import com.subgraph.orchid.TorClient;
import com.subgraph.orchid.sockets.sslengine.SSLEngineSSLSocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.net.ssl.SSLContext;

public class TorSocketHttpsPost extends TorSocketStream {

    private TorSocketHttpsPost(String url, List<NameValuePair> params, SSLContext sslContext) {
        super(url, params, sslContext);
    }

    public static TorSocketHttpsPost getInstance(String url, List<NameValuePair> params, SSLContext sslContext) {
        return new TorSocketHttpsPost(url, params, sslContext);
    }

    @Override
    public void executeRequest() throws Exception {
        this.executeRequest(TorClientFactory.getTorClient());
    }

    @Override
    public void executeRequest(TorClient client) throws Exception {
        socket = client.getSocketFactory().createSocket(getHost(), 80);
        sslSocket = new SSLEngineSSLSocket(socket, sslContext);
        try {
            PrintWriter writer = new PrintWriter(sslSocket.getOutputStream(), true);
            inputStream = sslSocket.getInputStream();
            writer.print("POST " + getPath() + getQuery() + " HTTP/1.0" + HTTP_LS);
            writer.print("Host: " + getHost() + HTTP_LS);
            writer.print("Content-Type: application/x-www-form-urlencoded" + HTTP_LS);
            writer.print("Content-Length: " + getParams().length() + HTTP_LS);
            writer.print("Connection: close" + HTTP_LS);
            writer.print(HTTP_LS);
            writer.print(getParams() + HTTP_LS);
            writer.flush();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Thread.sleep(800l);
            int character;
            try {
                while ((character = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(character);
                }
            } catch (Exception e) {
                //swallow
                byteArrayOutputStream.write(-1);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //Could not close stream
                }
            }
            inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            if (!sslSocket.getSession().isValid()) {
                throw new Exception("SSL Session is not valid. Either import the SSL Certificate; configure ApplicationProperties.setEnforceSslCertificates(false); or configure [TorRequest].setEnforceSslCertificates(false);");
            }
        } finally {
            sslSocket.close();
            socket.close();
        }
    }
}