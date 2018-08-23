package org.silcos.tictactoe.player;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.silcos.tictactoe.Board;
import org.silcos.tictactoe.BoardAnalyzer;
import org.silcos.tictactoe.GameController;
import org.silcos.tictactoe.MoveType;
import org.silcos.tictactoe.Player;

/**
 * <p>
 * Holds heuristics engine that works to defeat the other player. It plays
 * on the board, but also holds a copy of its moves. It uses its moves to
 * analyze how to game is progressing and to cache what it was trying to
 * do before.
 * 
 * <p>
 * The front-end application should use the <tt>ComputerPlayer.CallablePlay
 * </tt> object to separate the computational load from the GUI thread. This
 * prevents it from becoming unresponsive when playing on large boards.
 * 
 * @author Shukant Pal
 *
 */
public class ComputerPlayer extends Player {

	enum MoveRelation {
		HORIZONTAL,
		VERTICAL,
		DIAGONAL_ULBR,
		DIAGONAL_URBL
	}
	
	enum Mode {
		EASY,
		MEDIUM,
		HARD
	}

	private class Move {
		int row;
		int column;
		
		Move(int row, int column) throws IllegalArgumentException {
			if(row < 0 || column < 0 ||
					row >= getGameSet().getSide() ||
					column > getGameSet().getSide())
				throw new IllegalArgumentException();
			
			this.row = row;
			this.column = column;
		}
		
		int getColumnDiff(Move other) {
			return (this.column - other.column);
		}
		
		int getRowDiff(Move other) {
			return (this.row - other.row);
		}
		
		/**
		 * Finds the relation of this move w.r.t other move, allowing
		 * the caller to find the orientation in which these squares
		 * are making a line.
		 * 
		 * @param other
		 * @return
		 */
		MoveRelation getRelationWith(Move other) {
			if(!(Math.abs(other.row - this.row) <= 1)
					|| !(Math.abs(other.column - this.column) <= 1))
				return (null);
			
			if(other.row == this.row)
				return (MoveRelation.HORIZONTAL);
			else if(other.column == this.column)
				return (MoveRelation.VERTICAL);
			else if(Math.signum(other.row - this.row)
					== Math.signum(other.column - this.column)) {
				return (MoveRelation.DIAGONAL_URBL);
			} else {
				return (MoveRelation.DIAGONAL_ULBR);
			}
		}
		
		/**
		 * Calculates the move just opposite to the <code>other</code> move
		 * taking <code>this</code> as the center. The three <code>Move</code>
		 * objects are guaranteed to be in a straight line.
		 * 
		 * @param other - the move w.r.t which the opposite one is required
		 * @return a move which is opposite to <code>other</code>; null, if
		 * 			the calculated move is not in bounds;
		 */
		@SuppressWarnings("unused")
		Move getOpposite(Move other) {
			MoveRelation rel = getRelationWith(other);
			
			if(rel == null) {
				return (null);
			} else {
				try {
					return (new Move(this.row + getRowDiff(other),
							this.column + getColumnDiff(other)));
				} catch(IllegalArgumentException e) {
					return (null);/* The move is off-board :) */
				}
			}
		}
	}
	
	/**
	 * Exposes a <tt>Callable</tt> task that can be used by the front-end
	 * application to offload the <tt>ComputerPlayer</tt> object's computation
	 * to another worker thread.
	 * 
	 * @author Shukant Pal
	 */
	public class CallablePlay implements Callable<Void> {

		@Override
		public Void call() throws Exception {
			play();
			return (null);
		}
		
	}
	
	LinkedList<Move> moves;
	Board.LineIterator rowIterator;
	Board.LineIterator columnIterator;
	Board.LineIterator diagonalIterator;
	CallablePlay callable;
	
	private void cleanState() {
		rowIterator.setLineIndex(0);
		columnIterator.setLineIndex(0);
		diagonalIterator.setLineIndex(0);
	}
	
	private MoveType opponentSide() {
		if(assignedSide() == MoveType.O) {
			return (MoveType.X);
		} else {
			return (MoveType.O);
		}
	}
	
	/**
	 * 
	 * @param observer
	 * @return
	 */
	private Move victoryFor(MoveType observer) {
		cleanState();
		
		if(BoardAnalyzer.getCapturable(rowIterator, observer) != null) {
			System.out.println("Found row capturable:" + rowIterator.getLineIndex());
			int rowIdx = rowIterator.getLineIndex();
			int colIdx = BoardAnalyzer.newRowFinder(rowIdx, getGameSet()).closestEmpty().getCurColumn();
			return (new Move(rowIdx, colIdx));
		}
		
		if(BoardAnalyzer.getCapturable(columnIterator, observer) != null) {
			System.out.println("Found column capturable:" + columnIterator.getLineIndex());
			int colIdx = columnIterator.getLineIndex();
			int rowIdx = BoardAnalyzer.newColumnFinder(colIdx, getGameSet()).closestEmpty().getCurRow();
			return (new Move(rowIdx, colIdx));
		}
		
		if(BoardAnalyzer.getCapturable(diagonalIterator, observer) != null) {
			System.out.println("Found diagonal capturable: " + diagonalIterator.getLineIndex());
			int diagIdx = diagonalIterator.getLineIndex();
			Board.CellIterator diagFinder = BoardAnalyzer.newDiagonalFinder(
					diagIdx, getGameSet()).closestEmpty();
			return (new Move(diagFinder.getCurRow(), diagFinder.getCurColumn()));
		}
		
		return (null);
	}
	
	/**
	 * The number of recent moves this player has made that are
	 * in a single line.
	 */
	int linePipe;
	
	public ComputerPlayer(GameController.Bridge gameInstance, Board gameSet, MoveType side) {
		super(gameInstance, gameSet, side);
		moves = new LinkedList<Move>();
		
		rowIterator = getGameSet().rowIterator();
		columnIterator = getGameSet().columnIterator();
		diagonalIterator = getGameSet().diagonalIterator();
		
		callable = new CallablePlay();
	}
	
	/**
	 * Returns a move that will win the game for <tt>this</tt> player.
	 */
	private Move victory() {
		return (victoryFor(assignedSide()));
	}
	
	/**
	 * Returns a move that will win the game for <tt>this.opponentSide()</tt>
	 * player.
	 */
	private Move opponentVictory() {
		return (victoryFor(opponentSide()));
	}
	
	private Move getRandomMove() {
		System.out.println("Generating Random Move... ");
		int emptyIndex = (int) ((Math.random()*100) % getGameSet().getEmptyArea());
		
		for(int i=0; i<getGameSet().getSide(); i++) {
			for(int j=0; j<getGameSet().getSide(); j++) {
				if(getGameSet().getState(i, j) == MoveType.EMPTY) {
					if(emptyIndex == 0) {
						return (new Move(i, j));
					} else {
						--emptyIndex;
					}
				}
			}
		}
		
		return (null);/* Can't happen :( */
	}
	
	public CallablePlay getCallablePlay() {
		return (callable);
	}
	
	@Override
	protected void play() {
		Move mm = victory();
		
		if(mm == null)
			mm = opponentVictory();// Put your piece where opponent could get victory
		
		if(mm == null)
			mm = getRandomMove();
		
		if(mm == null) {
			System.out.println("Error: No valid move could be found!");
			return;
		}
			
		moves.push(mm);
		
		
		if(getGameSet().setState(assignedSide(), mm.row, mm.column))
			notifyPlay(mm.row, mm.column);
		else
			System.err.println("Fatal Error *:* Calculated move not valid at" +
								"(" + mm.row + "," + mm.column + ")");
	}
	
}
