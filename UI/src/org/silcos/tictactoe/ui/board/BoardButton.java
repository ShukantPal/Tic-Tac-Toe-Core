package org.silcos.tictactoe.ui.board;

import org.silcos.tictactoe.GameController;
import org.silcos.tictactoe.player.HumanPlayer;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * A custom button for the Tic/Tac/Toe/FX application, which is bound to
 * a <tt>GameController</tt> at a specific row & column. Once the button
 * is instantiated, the <tt>row</tt>, <tt>column</tt> and <tt>target</tt>
 * properties cannot be modified. This allows the button to be reliably
 * used to handling moves dynamically.
 * 
 * @author Shukant Pal
 *
 */
public class BoardButton extends Button {

	public static class PlayerMoveHandler implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent playerClickEvent) {
			if(playerClickEvent.getSource() instanceof BoardButton) {
				BoardButton src = (BoardButton) playerClickEvent.getSource();
				GameController target = src.getTarget();
				HumanPlayer mover = target.getNextMover();
				
				if(mover != null) {
					mover.playAt(src.getRow(), src.getColumn());
				} else {
					System.out.println("Warning *:* Human player tried moving while it was " +
										"computer's turn, .i.e. mover = null");
					// TODO: Store move, and play once computer player is done!
				}
			} else {
				throw new UnsupportedOperationException("Attached source to"
						+ "MouseEvent is not a button.");
			}
		}
		
	}

	private int row;
	private int column;
	private GameController target;
	
	public BoardButton(int row, int column, GameController target) {
		super();
		this.row = row;
		this.column = column;
		this.target = target;
	}
	
	public int getRow() {
		return (row);
	}
	
	public int getColumn() {
		return (column);
	}
	
	public GameController getTarget() {
		return (target);
	}
	
	public BoardButton newBoardButton(BoardButtonFactory bbf) {
		return (bbf.newBoardButton());
	}
}