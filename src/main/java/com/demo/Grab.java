package com.demo;

import com.subgraph.orchid.TorClient;
import com.subgraph.orchid.http.TorRequest;
import com.subgraph.orchid.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.demo.OrchidDemo.createInitalizationListner;

public class Grab {
    private static final Logger logger = Logger.getInstance(Grab.class);

    public static void main(String[] args) throws Exception {

        int startPort = 9150;

        ArrayList<TorClient> clients = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
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

                TorRequest whatIsMyIp = TorRequest.getInstance("https://api.ipify.org?format=json");
                try {
                    whatIsMyIp.executeRequest(client);
                    String content = whatIsMyIp.getResponse().getContent();
                    logger.info(Thread.currentThread().getName() + ": " + content.substring(content.indexOf("{\"ip\":\"")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            client.stop();
        }
    }
}