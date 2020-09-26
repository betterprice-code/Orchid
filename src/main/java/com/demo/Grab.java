package com.demo;

import com.subgraph.orchid.http.TorRequest;
import com.subgraph.orchid.logging.Logger;

public class Grab {
    private static final Logger logger = Logger.getInstance(Grab.class);

    public static void main(String[] args) throws Exception {
        try {
            TorRequest.openTunnel();
            TorRequest whatIsMyIp = TorRequest.getInstance("https://api.ipify.org/?format=json");
            for (int i = 0; i < 20; i++) {
                whatIsMyIp.executeRequest();
                String content = whatIsMyIp.getResponse().getContent();
                logger.info(content.substring(content.indexOf("{\"ip\":\"")));
            }


        } finally {
            TorRequest.closeTunnel();
        }
    }
}