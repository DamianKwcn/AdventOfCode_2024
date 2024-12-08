package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/6/input";
        String sessionCookie = "53616c7465645f5fda9e106db61622e93666d7c15a28ff9dd208d831d7a3963c8c6d1ea02c77089b92272f4892464cccd0fc923bdd2667eb07dcde8ca2cd26db";

        try {
            String input = fetchInput(url, sessionCookie);
            int visitedPositions = calculateVisitedPositions(input);
            System.out.println("Distinct positions visited: " + visitedPositions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateVisitedPositions(String input) {
        String[] lines = input.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();

        char[][] map = new char[rows][cols];
        int guardX = 0, guardY = 0;
        char guardDirection = '^';

        for (int i = 0; i < rows; i++) {
            map[i] = lines[i].toCharArray();
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == '^' || map[i][j] == 'v' || map[i][j] == '<' || map[i][j] == '>') {
                    guardX = i;
                    guardY = j;
                    guardDirection = map[i][j];
                    map[i][j] = '.';
                }
            }
        }

        int[][] moves = {
                {-1, 0},
                {0, 1},
                {1, 0},
                {0, -1}
        };
        int directionIndex = "^>v<".indexOf(guardDirection);

        Set<String> visited = new HashSet<>();
        visited.add(guardX + "," + guardY);

        while (true) {
            int nextX = guardX + moves[directionIndex][0];
            int nextY = guardY + moves[directionIndex][1];

            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
                break;
            }

            if (map[nextX][nextY] == '#') {
                directionIndex = (directionIndex + 1) % 4;
            } else {
                guardX = nextX;
                guardY = nextY;
                visited.add(guardX + "," + guardY);
            }
        }

        return visited.size();
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

        return response.body().trim();
    }
}