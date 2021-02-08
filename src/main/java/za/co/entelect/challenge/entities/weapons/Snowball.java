package za.co.entelect.challenge.entities.weapons;

import com.google.gson.annotations.SerializedName;

public class Snowball {
    @SerializedName("freezeDuration")
    public int freezeDuration;

    @SerializedName("range")
    public int range;

    @SerializedName("count")
    public int count;

    @SerializedName("freezeRadius")
    public int freezeRadius;

}
