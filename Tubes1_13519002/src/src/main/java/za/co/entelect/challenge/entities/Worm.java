package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.enums.Profession;

public class Worm {
    @SerializedName("id")
    public int id;

    @SerializedName("health")
    public int health;

    @SerializedName("position")
    public Position position;

    @SerializedName("diggingRange")
    public int diggingRange;

    @SerializedName("movementRange")
    public int movementRange;

    @SerializedName("roundsUntilUnfrozen")
    public int roundsUntilUnfrozen;

    @SerializedName("profession")
    public Profession profession;

    public Worm() {

    }

    public Worm(Profession profession) {
        this.id = -999;
        this.health = 0;
        this.profession = profession;
    }
}
