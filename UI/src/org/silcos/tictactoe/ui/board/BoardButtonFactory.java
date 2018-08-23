package org.silcos.tictactoe.ui.board;

import org.silcos.tictactoe.GameController;

/**
 * Factory for instantiated new <tt>BoardButton</tt> objects serially,
 * while incrementing the square during each construction. This factory
 * <b>does not support</b> concurrent constructions.
 * 
 * @author Shukant Pal
 *
 */
public class BoardButtonFactory {

	private GameController target;
	private int curRow;
	private int curColumn;
	
	/**
	 * Instantiates a new <tt>BoardButtonFactory</tt> which starts
	 * creating <tt>BoardButton</tt> objects from <tt>(0, 0)</tt> square.
	 * 
	 * @param target - the <tt>GameController</tt> controller to which
	 * 				the <tt>BoardButton</tt> objects should be bound to
	 */
	public BoardButtonFactory(GameController target) {
		this.target = target;
		this.curRow = 0;
		this.curColumn = 0;
	}
	
	/**
	 * Instantiates a new <tt>BoardButtonFactory</tt> which starts
	 * creating <tt>BoardButton</tt> objects from the <tt>(startRow,
	 * startColumn)</tt> square.
	 * 
	 * @param target - the <tt>GameController</tt> controller to which
	 * 					the <tt>BoardButton</tt> objects should be bound to
	 * @param startRow - the row of the square on which the first button
	 * 					should be bound to.
	 * @param startColumn - the column of the square on which the first
	 * 					button should be bound to.
	 */
	public BoardButtonFactory(GameController target, int startRow, int startColumn) {
		this.target = target;
		this.curRow = startRow;
		this.curColumn = startColumn;
	}
	
	/**
	 * Returns the row on which the next <code>BoardButton</code>
	 * will be instantiated.
	 */
	public int getCurRow() {
		return (curRow);
	}
	
	/**
	 * Returns the column on which the next <code>BoardButton</code>
	 * will be instantiated.
	 */
	public int getCurColumn() {
		return (curColumn);
	}
	
	/**
	 * Returns the target to which this factory is bound.
	 */
	public GameController getTarget() {
		return (target);
	}
	
	/**
	 * Instantiates a new <tt>BoardButton</tt> which is bound to the
	 * current square. It is equivalent to <pre>
	 *   new BoardButton(getCurRow(), getCurColumn(), getTarget());
	 * </pre>
	 */
	public BoardButton newBoardButton() {
		if(curColumn + 1 == target.getBoardSize()) {
			curColumn = 0;

			if(curRow + 1 != target.getBoardSize())
				return (new BoardButton(curRow++, target.getBoardSize() - 1, target));
			else {
				curRow = 0;
				return (new BoardButton(target.getBoardSize() - 1, target.getBoardSize() - 1,
						target));
			}
		} else {
			return (new BoardButton(curRow, curColumn++, target));
		}
	}
	
	/**
	 * Resets the factory in the sense that new <tt>BoardButton</tt>
	 * objects will be instantiated from the origin again.
	 */
	public void resetFactory() {
		curRow = 0;
		curColumn = 0;
	}
}
