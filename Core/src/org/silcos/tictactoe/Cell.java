package org.silcos.tictactoe;

public class Cell {
	
	MoveType heldBy;
	
	Cell() {
		this.heldBy = MoveType.EMPTY;
	}
	
	boolean isEmpty() {
		return (heldBy == MoveType.EMPTY);
	}
	
	MoveType getState() {
		return (heldBy);
	}
	
	boolean setState(MoveType newState) {
		if(isEmpty()) {
			this.heldBy = newState;
			return (true);
		} else {
			return (false);
		}
	}

}
