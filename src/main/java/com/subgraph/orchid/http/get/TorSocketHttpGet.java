package com.subgraph.orchid.http.get;

import com.subgraph.orchid.TorClient;
import com.subgraph.orchid.http.TorClientFactory;
import com.subgraph.orchid.http.TorSocketStream;

import java.io.PrintWriter;

public class TorSocketHttpGet extends TorSocketStream {

    private TorSocketHttpGet(String url) {
        super(url, null);
    }

    public static TorSocketHttpGet getInstance(String url) {
        return new TorSocketHttpGet(url);
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
        writer.print("GET " + getPath() + getQuery() + " HTTP/1.0" + HTTP_LS);
        writer.print("Host: " + getHost() + HTTP_LS);
        writer.print("Connection: close" + HTTP_LS);
        writer.print(HTTP_LS);
        writer.flush();
    }
}