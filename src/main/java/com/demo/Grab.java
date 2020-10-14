package com.demo;

import com.subgraph.orchid.TorClient;
import com.subgraph.orchid.http.TorRequest;
import com.subgraph.orchid.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.demo.OrchidDemo.createInitalizationListner;

public class Grab {
    private static final Logger logger = Logger.getInstance(Grab.class);

    public static void main(String[] args) throws Exception {

        int startPort = 9190;
        int threads = 10;

        System.out.println("Args:" + Arrays.toString(args));

        if (args.length > 0) {
            threads = Integer.valueOf(args[0]);
        }

        ArrayList<TorClient> clients = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            clients.add(getTorClient(startPort + i));
        }
        ExecutorService service = Executors.newFixedThreadPool(10);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(clients.stream()
                .map(torClient -> CompletableFuture.runAsync(new Task(torClient), service))
                .toArray(CompletableFuture[]::new));

        allOf.join();


    }

    private static TorClient getTorClient(int i) {
        TorClient client1 = new TorClient();
        client1.getConfig().setWarnUnsafeSocks(false);
        client1.enableSocksListener(i);
        client1.addInitializationListener(createInitalizationListner());
        client1.start();
        return client1;
    }

    public static class Task implements Runnable {
        private final TorClient client;

        public Task(TorClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            client.start();
            for (int i = 0; i < 5; i++) {

                logger.info("Starting #" + i);
                TorRequest whatIsMyIp = TorRequest.getInstance("https://api.ipify.org/");
                try {
                    logger.info("Executing request #" + i);
                    whatIsMyIp.setEnforceSslCertificates(false);
                    whatIsMyIp.executeRequest(client);
                    String content = whatIsMyIp.getResponse().getContent();
                    logger.info("Content: \"" + content + "\"");
                   // logger.info(Thread.currentThread().getName() + ": " + content.substring(content.indexOf("{\"ip\":\"")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            client.stop();
        }
    }
}