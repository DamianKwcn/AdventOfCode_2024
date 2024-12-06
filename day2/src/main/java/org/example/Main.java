package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/2/input";
        String sessionCookie = "";
        try {
            String input = fetchInput(url, sessionCookie);
            long safeReportsCount = countSafeReports(input);

            System.out.println("Number of safe raports: " + safeReportsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static long countSafeReports(String input) {
        return input.lines()
                .filter(line -> !line.isBlank())
                .filter(Main::isReportSafe)
                .count();
    }

    private static boolean isReportSafe(String line) {
        List<Integer> levels = parseLineToIntegers(line);

        for (int i = 1; i < levels.size(); i++) {
            int diff = Math.abs(levels.get(i) - levels.get(i - 1));
            if (diff < 1 || diff > 3) {
                return false;
            }
        }

        boolean increasing = true, decreasing = true;
        for (int i = 1; i < levels.size(); i++) {
            if (levels.get(i) > levels.get(i - 1)) decreasing = false;
            if (levels.get(i) < levels.get(i - 1)) increasing = false;
        }

        return increasing || decreasing;
    }

    private static List<Integer> parseLineToIntegers(String line) {
        return List.of(line.split("\\s+"))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
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