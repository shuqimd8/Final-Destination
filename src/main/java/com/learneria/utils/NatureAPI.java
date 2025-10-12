package com.learneria.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class NatureAPI {
    private static final OkHttpClient HTTP = new OkHttpClient();

    public static void syncNatureWords() {
        String[][] topics = {
                {"animal", "Animal"},
                {"plant", "Plant"},
                {"rock", "Non-Living"}
        };

        for (String[] topic : topics) {
            String url = "https://api.datamuse.com/words?topics=" + topic[0] + "&max=100";
            try (Response resp = HTTP.newCall(new Request.Builder().url(url).build()).execute()) {
                if (!resp.isSuccessful() || resp.body() == null) continue;

                JSONArray arr = new JSONArray(resp.body().string());
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT OR IGNORE INTO words (word, category) VALUES (?, ?)"
                );

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if (!obj.has("word")) continue;
                    ps.setString(1, capitalize(obj.getString("word")));
                    ps.setString(2, topic[1]);
                    ps.addBatch();
                }

                ps.executeBatch();
                ps.close();
                System.out.println("🌳 Synced " + arr.length() + " nature words for " + topic[1]);

            } catch (Exception e) {
                System.err.println("⚠️ Nature sync failed for " + topic[1] + ": " + e.getMessage());
            }
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
