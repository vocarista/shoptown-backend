package com.shoptown.backend.databaseAndAuth.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PingService {

    @Scheduled(cron = "0 14 * * * *")
    public void pingServer() {
        String serverUrl = "http://localhost:8080/ping"; // Replace with the actual server URL
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // Handle the response here if needed
            int responseCode = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            // Handle any exceptions that occur during the request
            e.printStackTrace();
        }
    }
}
