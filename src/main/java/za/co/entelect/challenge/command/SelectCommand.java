package za.co.entelect.challenge.command;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.MyWorm;

public class SelectCommand implements Command {
    private final MyWorm worm;
    private final Command command;

    public SelectCommand(MyWorm worm, Command command) {
        this.worm = worm;
        this.command = command;
    }

    @Override
    public String render() {
        if (Bot.getGameState().currentWormId == this.worm.id) {
            return String.format("%s", command.render());
        }
        return String.format("select %d;%s", this.worm.id, command.render());
    }
}
