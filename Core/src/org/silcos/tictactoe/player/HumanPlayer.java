package org.silcos.tictactoe.player;

import org.silcos.tictactoe.Board;
import org.silcos.tictactoe.GameController;
import org.silcos.tictactoe.MoveType;
import org.silcos.tictactoe.Player;

public class HumanPlayer extends Player {

	public HumanPlayer(GameController.Bridge gameBridge, Board gameSet, MoveType side) {
		super(gameBridge, gameSet, side);
	}

	protected void play() {
		throw new UnsupportedOperationException();
	}
	
	public void playAt(int row, int column) {
		if(!getGameSet().setState(assignedSide(), row, column)) {
			throw new IllegalArgumentException("(Row, Column) given has already been filled");
		}
		
		notifyPlay(row, column);
	}
}
