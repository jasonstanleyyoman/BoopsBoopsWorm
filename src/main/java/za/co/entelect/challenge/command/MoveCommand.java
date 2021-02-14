package za.co.entelect.challenge.command;

import za.co.entelect.challenge.entities.Position;

public class MoveCommand implements Command {
    private final int x;
    private final int y;

    public MoveCommand(Position p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public String render() {
        return String.format("move %d %d", x, y);
    }
}