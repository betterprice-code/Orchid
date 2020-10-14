package com.subgraph.orchid.http.post;

import com.subgraph.orchid.TorClient;
import com.subgraph.orchid.http.NameValuePair;
import com.subgraph.orchid.http.TorClientFactory;
import com.subgraph.orchid.http.TorSocketStream;

import java.io.PrintWriter;
import java.util.List;

public class TorSocketHttpPost extends TorSocketStream {

    protected TorSocketHttpPost(String url, List<NameValuePair> params) {
        super(url, params, null);
    }

    public static TorSocketHttpPost getInstance(String url, List<NameValuePair> params) {
        return new TorSocketHttpPost(url, params);
    }

    @Override
    public void executeRequest() throws Exception {
        this.executeRequest(TorClientFactory.getTorClient());

    }

    @Override
    public void executeRequest(TorClient client) throws Exception {
        socket = client.getSocketFactory().createSocket(getHost(), 80);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        inputStream = socket.getInputStream();
        writer.print("POST " + getPath() + getQuery() + " HTTP/1.0" + HTTP_LS);
        writer.print("Host: " + getHost() + HTTP_LS);
        writer.print("Content-Type: application/x-www-form-urlencoded" + HTTP_LS);
        writer.print("Content-Length: " + getParams().length() + HTTP_LS);
        writer.print("Connection: close" + HTTP_LS);
        writer.print(HTTP_LS);
        writer.print(getParams() + HTTP_LS);
        writer.flush();
    }
}