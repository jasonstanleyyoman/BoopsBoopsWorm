package za.co.entelect.challenge.entities.worm;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.weapons.BananaBomb;
import za.co.entelect.challenge.enums.Profession;

public class Agent extends MyWorm {

    @SerializedName("bananaBombs")
    public BananaBomb bananaBomb;

    public Agent() {
        this.id = -999;
        this.health = 0;
        this.profession = Profession.AGENT;
    }
}
