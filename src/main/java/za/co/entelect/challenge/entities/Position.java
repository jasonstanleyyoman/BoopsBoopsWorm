package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;

import za.co.entelect.challenge.common.PlaneUtils;

public class Position {

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position p) {
        this.x = p.x;
        this.y = p.y;
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

    public Position modifyX(int dx) {
        this.x += dx;
        return this;
    }

    public Position modifyY(int dy) {
        this.y += dy;
        return this;
    }

    public double distance(Position other) {
        return PlaneUtils.realEuclideanDistance(this.x, this.y, other.x, other.y);
    }
}
