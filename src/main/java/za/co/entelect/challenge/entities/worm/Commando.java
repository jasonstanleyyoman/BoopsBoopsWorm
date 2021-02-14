package za.co.entelect.challenge.entities.worm;

import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.enums.Profession;

public class Commando extends MyWorm {

    public Commando() {
        this.id = -999;
        this.health = 0;
        this.profession = Profession.COMMANDO;
    }
}
