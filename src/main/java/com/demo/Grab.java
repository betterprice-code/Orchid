package com.demo;

import com.subgraph.orchid.http.TorRequest;
import com.subgraph.orchid.logging.Logger;

public class Grab {
    private static final Logger logger = Logger.getInstance(Grab.class);

    public static void main(String[] args) throws Exception {
        try {
            TorRequest.openTunnel();
            TorRequest whatIsMyIp = TorRequest.getInstance("https://www.alibaba.com/product-detail/New-Style-Slim-Fit-European-Navy_60775201422.html?spm=a27aq.13957185.0_2.1.56341ed4htNUMx");
            for (int i = 0; i < 20; i++) {
                whatIsMyIp.executeRequest();
              //  String content = whatIsMyIp.getResponse().getContent();
              //  logger.info(content.substring(content.indexOf("{\"ip\":\"")));
                logger.info(whatIsMyIp.getResponse().getContent());
            }


        } finally {
            TorRequest.closeTunnel();
        }
    }
}