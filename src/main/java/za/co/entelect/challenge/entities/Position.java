package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;

public class Position {

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SerializedName("x")
    public int x;

    @SerializedName("y")
    public int y;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Position || o instanceof Cell))
            return false;

        Position p = (Position) o;
        return this.x == p.x && this.y == p.y;
    }
}
