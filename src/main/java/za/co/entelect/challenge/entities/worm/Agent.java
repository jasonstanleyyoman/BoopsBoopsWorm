package za.co.entelect.challenge.entities.worm;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.weapons.BananaBomb;

public class Agent extends MyWorm {

    @SerializedName("bananaBombs")
    public BananaBomb weapons;

}
