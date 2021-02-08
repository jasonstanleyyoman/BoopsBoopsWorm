package za.co.entelect.challenge.entities;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.worm.Agent;

public class MyPlayer {
    @SerializedName("id")
    public int id;

    @SerializedName("score")
    public int score;

    @SerializedName("health")
    public int health;

    @SerializedName("worms")
    public MyWorm[] worms;

}
