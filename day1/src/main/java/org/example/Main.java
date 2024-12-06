package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/1/input";
        String sessionCookie = "";

        try {
            String input = fetchInput(url, sessionCookie);
            int totalDistance = calculateDistance(input);

            System.out.println("Distance: " + totalDistance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateDistance(String input) {
        List<Integer> leftColumn = new ArrayList<>();
        List<Integer> rightColumn = new ArrayList<>();

        input.lines()
                .filter(line -> !line.isBlank())
                .forEach(line -> {
                    String[] numbers = line.split("\\s+");
                    leftColumn.add(Integer.parseInt(numbers[0]));
                    rightColumn.add(Integer.parseInt(numbers[1]));
                });

        leftColumn.sort(Integer::compareTo);
        rightColumn.sort(Integer::compareTo);

        return IntStream.range(0, leftColumn.size())
                .map(i -> Math.abs(leftColumn.get(i) - rightColumn.get(i)))
                .sum();
    }

    public static String fetchInput(String url, String sessionCookie) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", "session=" + sessionCookie)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch data: " + response.statusCode());
        }

        return response.body();
    }
}