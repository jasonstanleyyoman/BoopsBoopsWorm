package za.co.entelect.challenge.entities;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.Profession;

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
    private MyWorm getSpecifiedWorm(Profession prof, Predicate<String> filter) {
        Gson gson = new Gson();
        try {
            Map map = gson.fromJson(Bot.getState(), Map.class);
            Map players = (Map) map.get("myPlayer");
            List<Object> lworms = (List<Object>) players.get("worms");
            for (int i = 0; i < 3; i++) {
                String wormsString = gson.toJson(lworms.get(i));
                if (filter.test(wormsString)) {
                    switch (prof) {
                        case AGENT:
                            Agent agent = gson.fromJson(wormsString, Agent.class);
                            return agent;
                        case TECHNOLOGIST:
                            Technologist technologist = gson.fromJson(wormsString, Technologist.class);
                            return technologist;
                        case COMMANDO:
                            Commando commando = gson.fromJson(wormsString, Commando.class);
                            return commando;
                        default:
                            return null;
                    }
                }
            }
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

    public Agent getAgent() {
        MyWorm worm = getSpecifiedWorm(Profession.AGENT, ws -> ws.toLowerCase().contains("bananabombs"));
        if (worm == null)
            return new Agent();
        return ((Agent) worm);
    }

    public Technologist getTechnologist() {
        MyWorm worm = getSpecifiedWorm(Profession.TECHNOLOGIST, ws -> ws.toLowerCase().contains("snowballs"));
        if (worm == null)
            return new Technologist();
        return ((Technologist) worm);
    }

    public Commando getCommando() {
        MyWorm worm = getSpecifiedWorm(Profession.COMMANDO,
                ws -> (!(ws.toLowerCase().contains("snowballs") || ws.toLowerCase().contains("bananabombs"))));
        if (worm == null)
            return new Commando();
        return ((Commando) worm);
    }

}
