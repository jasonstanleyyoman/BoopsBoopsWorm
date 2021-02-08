package za.co.entelect.challenge.entities;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;

public class MyPlayer {
    @SerializedName("id")
    public int id;

    @SerializedName("score")
    public int score;

    @SerializedName("health")
    public int health;

    @SerializedName("worms")
    public MyWorm[] worms;

    @SuppressWarnings("all")
    public Agent getAgent() {
        Gson gson = new Gson();
        try {
            Map map = gson.fromJson(Bot.getState(), Map.class);
            Map players = (Map) map.get("myPlayer");
            List<Object> lworms = (List<Object>) players.get("worms");
            for (int i = 0; i < 3; i++) {
                String wormsString = gson.toJson(lworms.get(i));
                if (wormsString.toLowerCase().contains("bananabombs")) {
                    Agent agent = gson.fromJson(wormsString, Agent.class);
                    return agent;
                }
            }
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

    @SuppressWarnings("all")
    public Technologist getTechnologist() {
        Gson gson = new Gson();
        try {
            Map map = gson.fromJson(Bot.getState(), Map.class);
            Map players = (Map) map.get("myPlayer");
            List<Object> lworms = (List<Object>) players.get("worms");
            for (int i = 0; i < 3; i++) {
                String wormsString = gson.toJson(lworms.get(i));
                if (wormsString.toLowerCase().contains("snowballs")) {
                    Technologist tech = gson.fromJson(wormsString, Technologist.class);
                    return tech;
                }
            }
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

    @SuppressWarnings("all")
    public Commando getCommando() {
        Gson gson = new Gson();
        try {
            Map map = gson.fromJson(Bot.getState(), Map.class);
            Map players = (Map) map.get("myPlayer");
            List<Object> lworms = (List<Object>) players.get("worms");
            for (int i = 0; i < 3; i++) {
                String wormsString = gson.toJson(lworms.get(i));
                if (!(wormsString.toLowerCase().contains("snowballs")
                        || wormsString.toLowerCase().contains("bananabombs"))) {
                    Commando cmdo = gson.fromJson(wormsString, Commando.class);
                    return cmdo;
                }
            }
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

}
