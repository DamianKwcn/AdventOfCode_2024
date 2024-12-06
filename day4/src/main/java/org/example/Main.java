package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        String url = "https://adventofcode.com/2024/day/4/input";
        String sessionCookie = "";

        try {
            String input = fetchInput(url, sessionCookie);
            char[][] grid = parseInputToGrid(input);
            String word = "MAS";

            int totalOccurrences = countWordOccurrences(grid, word);
            System.out.println("Total occurrences of XMAS: " + totalOccurrences);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static char[][] parseInputToGrid(String input) {
        String[] lines = input.strip().split("\n");
        int rows = lines.length;
        int cols = lines[0].length();

        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines[i].toCharArray();
        }
        return grid;
    }

    public static int countWordOccurrences(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int[] dir : directions) {
                    if (checkWord(grid, word, row, col, dir[0], dir[1])) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static boolean checkWord(char[][] grid, String word, int row, int col, int rowDirection, int colDirection) {
        int rows = grid.length;
        int columns = grid[0].length;
        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            int newRow = row + i * rowDirection;
            int newCol = col + i * colDirection;

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= columns) {
                return false;
            }

            if (grid[newRow][newCol] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}