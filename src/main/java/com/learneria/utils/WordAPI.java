package com.learneria.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * WordAPI connects to the Datamuse API to fetch random words
 * by parts of speech (noun, verb, adjective).
 * This API is 100% free and requires no key.
 */
public class WordAPI {

    private static final String BASE_URL = "https://api.datamuse.com/words?";

    // =============================
    // MAIN SYNC ENTRY
    // =============================
    public static void syncPOSWords(int limit) {
        try {
            fetchAndStore("rel_jja=thing", "Noun", limit);       // Nouns
            fetchAndStore("rel_trg=run", "Verb", limit);         // Verbs
            fetchAndStore("rel_jjb=happy", "Adjective", limit);  // Adjectives
        } catch (Exception e) {
            System.err.println("⚠️ Word API sync failed: " + e.getMessage());
        }
    }

    // =============================
    // FETCH + STORE
    // =============================
    private static void fetchAndStore(String query, String category, int limit) {
        try {
            String urlStr = BASE_URL + query + "&max=" + limit;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONArray arr = new JSONArray(response.toString());
            Set<String> uniqueWords = new HashSet<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String word = obj.getString("word").trim();
                if (word.matches("^[a-zA-Z]+$") && word.length() > 2) {
                    uniqueWords.add(capitalize(word));
                }
            }

            storeWords(uniqueWords, category);
            System.out.println("🧠 Synced " + uniqueWords.size() + " words for " + category);

        } catch (Exception e) {
            System.err.println("⚠️ Failed to fetch " + category + ": " + e.getMessage());
        }
    }

    // =============================
    // STORE INTO DATABASE
    // =============================
    private static void storeWords(Set<String> words, String category) {
        String sql = "INSERT OR IGNORE INTO words (word, category) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String w : words) {
                ps.setString(1, w);
                ps.setString(2, category);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

