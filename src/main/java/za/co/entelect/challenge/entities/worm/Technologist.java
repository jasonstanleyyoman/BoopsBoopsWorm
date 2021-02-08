package za.co.entelect.challenge.entities.worm;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.weapons.Snowball;

public class Technologist extends MyWorm {

    @SerializedName("snowballs")
    public Snowball snowballs;

}
