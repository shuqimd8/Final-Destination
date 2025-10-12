package com.learneria.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FoodAPI {
    private static final String APP_ID = "YOUR_APP_ID";
    private static final String APP_KEY = "YOUR_APP_KEY";
    private static final String BASE_URL = "https://api.edamam.com/api/food-database/v2/parser";
    private static final OkHttpClient HTTP = new OkHttpClient();

    public static void syncFoodWords() {
        String[] foodCategories = {"fruit", "meat", "grain", "dairy"};

        for (String cat : foodCategories) {
            try {
                String url = BASE_URL + "?app_id=" + APP_ID + "&app_key=" + APP_KEY + "&ingr=" + cat;
                Response resp = HTTP.newCall(new Request.Builder().url(url).build()).execute();

                if (!resp.isSuccessful() || resp.body() == null) {
                    System.out.println("❌ Failed to fetch " + cat);
                    continue;
                }

                JSONObject json = new JSONObject(resp.body().string());
                JSONArray hints = json.getJSONArray("hints");

                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT OR IGNORE INTO words (word, category) VALUES (?, ?)"
                );

                for (int i = 0; i < hints.length(); i++) {
                    JSONObject food = hints.getJSONObject(i).getJSONObject("food");
                    ps.setString(1, food.getString("label"));
                    ps.setString(2, capitalize(cat));
                    ps.addBatch();
                }

                ps.executeBatch();
                ps.close();
                System.out.println("🍎 Synced " + hints.length() + " foods for " + cat);

            } catch (Exception e) {
                System.err.println("⚠️ Food sync failed for " + cat + ": " + e.getMessage());
            }
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
