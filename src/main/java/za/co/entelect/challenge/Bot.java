package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Direction;

import java.util.*;
import java.util.stream.Collectors;

public class Bot 
{
    private Random random;
    private GameState gameState;
    private Opponent opponent;
    private MyWorm currentWorm;

    public Bot(Random random, GameState gameState) 
    {
        this.random = random;
        this.gameState = gameState;
        this.opponent = gameState.opponents[0];
        this.currentWorm = getCurrentWorm(gameState);
    }

    private MyWorm getCurrentWorm(GameState gameState) 
    {
        return Arrays.stream(gameState.myPlayer.worms)
                .filter(myWorm -> myWorm.id == gameState.currentWormId)
                .findFirst()
                .get();
    }

    public Command run() 
    {

        Worm enemyWorm = getFirstWormInRange();
        if (enemyWorm != null) 
        {
            Direction direction = resolveDirection(currentWorm.position, enemyWorm.position);
            return new ShootCommand(direction);
        }

        List<Cell> surroundingBlocks = getSurroundingCells(currentWorm.position.x, currentWorm.position.y);
        int cellIdx = random.nextInt(surroundingBlocks.size());

        Cell block = surroundingBlocks.get(cellIdx);
        if (block.type == CellType.AIR) 
        {
            return new MoveCommand(block.x, block.y);
        } 
        else if (block.type == CellType.DIRT) 
        {
            return new DigCommand(block.x, block.y);
        }

        return new DoNothingCommand();
    }

    private Worm getFirstWormInRange() 
    {
        Set<String> cells = constructFireDirectionLines(currentWorm.weapon.range)
                .stream()
                .flatMap(Collection::stream)
                .map(cell -> String.format("%d_%d", cell.x, cell.y))
                .collect(Collectors.toSet());

        for (Worm enemyWorm : opponent.worms) 
        {
            String enemyPosition = String.format("%d_%d", enemyWorm.position.x, enemyWorm.position.y);
            if (cells.contains(enemyPosition)) 
            {
                return enemyWorm;
            }
        }

        return null;
    }

    private List<List<Cell>> constructFireDirectionLines(int range) 
    {
        List<List<Cell>> directionLines = new ArrayList<>();
        for (Direction direction : Direction.values()) 
        {
            List<Cell> directionLine = new ArrayList<>();
            for (int directionMultiplier = 1; directionMultiplier <= range; directionMultiplier++) 
            {
                int coordinateX = currentWorm.position.x + (directionMultiplier * direction.x);
                int coordinateY = currentWorm.position.y + (directionMultiplier * direction.y);

                if (!isValidCoordinate(coordinateX, coordinateY)) 
                {
                    break;
                }

                if (euclideanDistance(currentWorm.position.x, currentWorm.position.y, coordinateX, coordinateY) > range) 
                {
                    break;
                }

                Cell cell = gameState.map[coordinateY][coordinateX];
                if (cell.type != CellType.AIR) 
                {
                    break;
                }

                directionLine.add(cell);
            }
            directionLines.add(directionLine);
        }

        return directionLines;
    }

}
