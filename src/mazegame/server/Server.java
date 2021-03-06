// A server is the main tool of the client.
//
// The server will be created by passing it some ServerSpecs,
// generated by CLOptions processing the command line arguments.
//
// The server will accept the hero movements invoked by the client
// and pass them to the maze.
//
// The server will also provide the client with the ClientView, which
// is all the client needs to renderize the game status to the
// player.

package mazegame.server;

import java.util.EmptyStackException;

import mazegame.core.Maze;
import mazegame.core.Map;
import mazegame.server.ServerSpec;
import mazegame.core.End;
import mazegame.core.Hero;
import mazegame.util.Direction;
import mazegame.util.Queue;
import mazegame.util.Stack;
import mazegame.util.StackArray;

public class Server {

    private Maze maze;
    private Stack<Direction> undo;

    public Server(ServerSpec spec) {
        Map map = spec.generateMap();
        End end = spec.generateEnd();
        Hero hero = spec.generateHero();
        int trailCapacity = spec.generateTrailCapacity();
        this.maze = new Maze(map, end, hero, trailCapacity);
        undo = new StackArray<Direction>();
    }

    private static Direction reverse(Direction d) {
        switch (d) {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case EAST:
                return Direction.WEST;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported Direction: " + d);
        }
    }

    public boolean moveHero(Direction dir) {
        boolean ok =  maze.moveHero(dir);
        if (ok) {
            undo.push(reverse(dir));
        }
        return ok;
    }

    public ClientView getClientView() {
        return maze.getClientView();
    }

    public Queue<Update> getUpdates() {
        return maze.getUpdates();
    }

    public boolean isGameOver() {
        return maze.isGameOver();
    }

    public boolean undo() {
        Direction d;
        try {
            d = undo.pop();
        } catch (EmptyStackException ex) {
            return false;
        }
        maze.moveHero(d);
        return true;
    }
}
