package za.co.entelect.challenge.command;

public class SelectCommand implements Command {

    private final int wormId;
    private final Command command;

    public SelectCommand(int wormId, Command command) {
        this.wormId = wormId;
        this.command = command;
    }

    @Override
    public String render() {
        return String.format("select %d;%s", wormId, command.render());
    }
}
