package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/5/input";
        String sessionCookie = "";

        try {
            String input = fetchInput(url, sessionCookie);
            int totalSum = calculateMiddleSum(input);

            System.out.println("Total Sum of Middle Pages: " + totalSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateMiddleSum(String input) {
        String[] sections = input.split("\n\n");
        String[] rulesInput = sections[0].split("\n");
        String[] updatesInput = sections[1].split("\n");

        List<int[]> rules = new ArrayList<>();
        for (String rule : rulesInput) {
            String[] parts = rule.split("\\|");
            rules.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        int totalSum = 0;

        for (String update : updatesInput) {
            String[] pages = update.split(",");
            List<Integer> updatePages = new ArrayList<>();
            for (String page : pages) {
                updatePages.add(Integer.parseInt(page));
            }

            if (isUpdateValid(updatePages, rules)) {
                totalSum += findMiddlePage(updatePages);
            }
        }

        return totalSum;
    }

    public static boolean isUpdateValid(List<Integer> updatePages, List<int[]> rules) {
        Map<Integer, Integer> pageIndices = new HashMap<>();
        for (int i = 0; i < updatePages.size(); i++) {
            pageIndices.put(updatePages.get(i), i);
        }

        for (int[] rule : rules) {
            int x = rule[0], y = rule[1];
            if (pageIndices.containsKey(x) && pageIndices.containsKey(y)) {
                if (pageIndices.get(x) > pageIndices.get(y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int findMiddlePage(List<Integer> updatePages) {
        return updatePages.get(updatePages.size() / 2);
    }

    public static String fetchInput(String url, String sessionCookie) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

        if (sessionCookie != null && !sessionCookie.isBlank()) {
            requestBuilder.header("Cookie", "session=" + sessionCookie);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch data: " + response.statusCode());
        }

        return response.body();
    }
}