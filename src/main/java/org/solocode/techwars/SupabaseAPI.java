package org.solocode.techwars;

import okhttp3.*;
import com.google.gson.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class SupabaseAPI {

    private final String supabaseUrl;
    private final String supabaseKey;
    private final OkHttpClient client = new OkHttpClient();

    public SupabaseAPI(FileConfiguration config) {
        // Load from config with fallback to defaults (for backwards compatibility)
        this.supabaseUrl = config.getString("supabase.url", "");
        this.supabaseKey = config.getString("supabase.api-key", "");
        
        if (supabaseUrl.isEmpty() || supabaseKey.isEmpty() || "CHANGE_ME".equals(supabaseKey)) {
            throw new IllegalStateException(
                "Supabase configuration is missing or not set! " +
                "Please configure supabase.url and supabase.api-key in config.yml"
            );
        }
    }

    public void getAllTeams(Consumer<Map<String, List<String>>> callback) {
        Request request = new Request.Builder()
                .url(supabaseUrl)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.accept(Collections.emptyMap());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Map<String, List<String>> teams = new HashMap<>();
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray array = JsonParser.parseString(body).getAsJsonArray();

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject playerData = array.get(i).getAsJsonObject();
                        String team = playerData.get("team").getAsString();
                        String username = playerData.get("minecraft_username").getAsString();
                        
                        teams.computeIfAbsent(team, k -> new ArrayList<>()).add(username);
                    }
                }
                callback.accept(teams);
            }
        });
    }

    public void getPlayerTeam(String minecraftUsername, Consumer<String> callback) {
        HttpUrl url = HttpUrl.parse(supabaseUrl)
                .newBuilder()
                .addQueryParameter("minecraft_username", "eq." + minecraftUsername)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.accept(null); // return null on failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JsonArray array = JsonParser.parseString(body).getAsJsonArray();

                    if (array.size() > 0) {
                        JsonObject playerData = array.get(0).getAsJsonObject();
                        String team = playerData.get("team").getAsString();
                        callback.accept(team); // pass team to lambda
                    } else {
                        callback.accept(null);
                    }
                } else {
                    callback.accept(null);
                }
            }
        });
    }
}
