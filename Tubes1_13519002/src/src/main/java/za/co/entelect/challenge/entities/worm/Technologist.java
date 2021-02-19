package za.co.entelect.challenge.entities.worm;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.weapons.Snowball;
import za.co.entelect.challenge.enums.Profession;

public class Technologist extends MyWorm {

    @SerializedName("snowballs")
    public Snowball snowballs;

    public Technologist() {
        this.id = -999;
        this.health = 0;
        this.profession = Profession.TECHNOLOGIST;
    }
}
