package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.CellType;

public class Cell extends Position {

    public Cell() {
        super();
    }

    public Cell(Cell other) {
        super(other);
        this.type = other.type;
        this.powerUp = other.powerUp;
    }

    public Cell(int x, int y) {
        super(x, y);
    }

    @SerializedName("type")
    public CellType type;

    @SerializedName("powerup")
    public PowerUp powerUp;

}
