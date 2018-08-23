package org.silcos.tictactoe;

import java.util.EventListener;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.silcos.tictactoe.player.ComputerPlayer;
import org.silcos.tictactoe.player.HumanPlayer;

/**
 * Controls all aspects a ongoing Tic-Tac-Toe game: board, players, level,
 * mode, excluding the UI. It can be instantiated as a two-player game or
 * a human-vs-computer game.
 * 
 * @author Shukant Pal
 *
 */
public class GameController {
	
	enum GameEventType {
		GAME_WON,
		GAME_DRAWN,
		GAME_CANCELLED
	}
	
	enum LineIdentifier {
		ROW,
		COLUMN,
		DIAGONAL_ULBR,
		DIAGONAL_URBL,
		OTHER
	}
	
	public class GameEvent {
		
		GameEventType type;
		MoveType origin;
		
		GameEvent(GameEventType type, MoveType origin) {
			this.type = type;
			this.origin = origin;
		}
		
		public GameEventType getType() {
			return (type);
		}
		
		public MoveType getOrigin() {
			return (origin);
		}

	}
	
	public class GameWonEvent extends GameEvent {

		LineIdentifier capturedLineType;
		int capturedLineIndex;
		
		GameWonEvent(GameEventType type, MoveType origin,
				LineIdentifier capturedLineType, int capturedLineIndex) {
			super(type, origin);
			
			if(capturedLineType == LineIdentifier.OTHER) {
				throw new IllegalArgumentException("All capturable lines have a specified type: " +
														"OTHER is not valid");
			}
			
			this.capturedLineType = capturedLineType;
			this.capturedLineIndex = capturedLineIndex;
		}
		
		public LineIdentifier getCapturedLineType() {
			return (capturedLineType);
		}
		
		public int getCapturedLineIndex() {
			return (capturedLineIndex);
		}

	}
	
	public interface PlayerMoveHandler {
		void handle(MoveType playerSide, int rowIdx, int colIdx);
	}
	
	public interface GameListener extends EventListener {
		void handle(GameEvent change);
	}
	
	/**
	 * Each <tt>Player</tt> is given a <tt>GameController.Bridge</tt> that
	 * gives them the privilege to modify the state of the game. Once a
	 * player uses <tt>Board.setState</tt> to places his move, he should
	 * also notify the <tt>GameController</tt> using the <tt>notifyMove</tt>
	 * method.
	 * 
	 * @author Shukant Pal
	 */
	public class Bridge {
		
		void notifyMove(Player mover, int rowIdx, int colIdx) {
			if(nextTurn == MoveType.O)
				nextTurn = MoveType.X;
			else
				nextTurn = MoveType.O;
			
			moveHandlers.forEach(
						(PlayerMoveHandler handler) -> {
							handler.handle(mover.assignedSide(), rowIdx, colIdx);
						}
					);
			
			MoveType winnerType = gameSet.findWinner();
			GameWonEvent gameWonEvent = new GameWonEvent(GameEventType.GAME_WON,
					winnerType, gameSet.getWinCacheIdentifier(), gameSet.getWinCacheIndex());
			
			if(winnerType != MoveType.EMPTY) {
				gameListeners.forEach(
							(GameListener listener) -> {
								listener.handle(gameWonEvent);
							}
						);
				return;
			}
			
			if(getNextMover() == null) {
				if(nextTurn == MoveType.O)
					internOffloader.submit(((ComputerPlayer) o).getCallablePlay());
				else
					internOffloader.submit(((ComputerPlayer) x).getCallablePlay());
			}		
		}
	
	}
	
	Board gameSet;
	Player o;
	Player x;
	MoveType nextTurn;
	
	LinkedList<PlayerMoveHandler> moveHandlers = new LinkedList<PlayerMoveHandler>();
	LinkedList<GameListener> gameListeners = new LinkedList<GameListener>();
	Bridge playerBridge = new Bridge();
	
	final ExecutorService internOffloader = Executors.newSingleThreadExecutor();
	
	/**
	 * Constructs a new <tt>GameController</tt> with both players
	 * as a <tt>HumanPlayer</tt>, and a board of given size.
	 * 
	 * @param boardSize - the size of the board
	 */
	GameController(int boardSize) {
		gameSet = new Board(boardSize);
		o = new HumanPlayer(playerBridge, gameSet, MoveType.O);
		x = new HumanPlayer(playerBridge, gameSet, MoveType.X);
		nextTurn = MoveType.O;
	}

	GameController(int boardSize, MoveType humanMove) {
		gameSet = new Board(boardSize);
		
		if(humanMove == MoveType.O) {
			o = new HumanPlayer(playerBridge, gameSet, MoveType.O);
			x = new ComputerPlayer(playerBridge, gameSet, MoveType.X);
		} else {
			o = new ComputerPlayer(playerBridge, gameSet, MoveType.O);
			x = new HumanPlayer(playerBridge, gameSet, MoveType.X);
		}
		
		nextTurn = MoveType.O;
	}
	
	public int getBoardSize() {
		return (gameSet.getSide());
	}
	
	public HumanPlayer getPlayerO() {
		if(o instanceof HumanPlayer)
			return ((HumanPlayer) o);
		else
			return (null);
	}
	
	public HumanPlayer getPlayerX() {
		if(x instanceof HumanPlayer)
			return ((HumanPlayer) x);
		else
			return (null);
	}
	
	public boolean isSinglePlayer() {
		return (getPlayerO() == null || getPlayerX() == null);
	}
	
	public boolean isDoublePlayer() {
		return (!isSinglePlayer());
	}
	
	public HumanPlayer getNextMover() {
		if(nextTurn == MoveType.O) {
			return (getPlayerO());
		} else {
			return (getPlayerX());
		}
	}
	
	public void addMoveHandler(PlayerMoveHandler moveHandler) {
		moveHandlers.add(moveHandler);
	}
	
	public void addGameListener(GameListener gameListener) {
		gameListeners.add(gameListener);
	}
	
	public void close() {
		internOffloader.shutdownNow();
		try {
			internOffloader.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void finalize() {
		internOffloader.shutdownNow();
	}
}
