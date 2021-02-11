package za.co.entelect.challenge.command;

import za.co.entelect.challenge.entities.Position;

public class BananaCommand implements Command {
    private final int x;
    private final int y;

    public BananaCommand(Position p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public String render() {
        return String.format("banana %d %d", x, y);
    }
}
