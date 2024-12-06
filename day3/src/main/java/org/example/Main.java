package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/3/input";
        String sessionCookie = "";

        try {
            String input = fetchInput(url, sessionCookie);
            int totalSum = calculateMulSum(input);

            System.out.println("Total Sum: " + totalSum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateMulSum(String input) {
        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int totalSum = 0;
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            totalSum += x * y;
        }
        return totalSum;
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