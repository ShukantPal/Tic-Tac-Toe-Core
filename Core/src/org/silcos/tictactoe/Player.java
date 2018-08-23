package org.silcos.tictactoe;

public abstract class Player {
	
	GameController.Bridge gameBridge;
	Board gameSet;
	MoveType side;
	
	public Player(GameController.Bridge gameBridge, Board gameSet, MoveType assignedSide) {
		this.gameBridge = gameBridge;
		this.gameSet = gameSet;
		this.side = assignedSide;
	}
	
	protected int getBoardSize() {
		return (gameSet.getSide());
	}
	
	protected Board getGameSet() {
		return (gameSet);
	}
	
	protected void notifyPlay(int row, int col) {
		gameBridge.notifyMove(this, row, col);
	}
	
	public MoveType assignedSide() {
		return (side);
	}
	
	protected void play() {
		
	}
}
