package org.silcos.tictactoe;

/**
 * <p>
 * Holds the grid of squares in which each player can make moves. Once a
 * state is written on a square, it cannot be replaced. Both players can
 * query previously made moves on the board by checking the state of a
 * square by its coordinates.
 * 
 * <p>
 * Only two types of moves can be played on the board, hence, only two
 * <code>Player</code> instances should be linked with a <code>Board</code>
 * instance.
 *
 * <p>
 * A <code>Board</code> can be customized by setting its area, although it
 * will be a square.
 * 
 * @author Shukant Pal
 *
 */
public class Board {
	
	private class LineState {
		
		int oFilled, xFilled;
		
		public LineState() {
			this.oFilled = 0;
			this.xFilled = 0;
		}
		
		public boolean isCaptured() {
			return (oFilled == getSide() || xFilled == getSide());
		}
		
		public MoveType getWinner() {
			return ((oFilled > xFilled) ? MoveType.O : MoveType.X);
		}
		
		public int getOFilled() {
			return (oFilled);
		}
		
		public int getXFilled() {
			return (xFilled);
		}
		
		boolean isPure() {
			return (xFilled == 0 || oFilled == 0);
		}
		
		public void fillAsO() {
			++(oFilled);
		}
		
		public void fillAsX() {
			++(xFilled);
		}
		
		public void fillAs(MoveType newState) {
			switch(newState) {
			case O:
				fillAsO();
				break;
			case X:
				fillAsX();
				break;
			default:
				return;
			}
		}
	}
	
	/**
	 * <p>
	 * <tt>CellIterator</tt> is a iterator which operates on a 2-D board. It
	 * allows the user to navigate through the board in a row, column, or the
	 * two diagonals by fixing indices using <tt>setFixRow(true)</tt> and
	 * <tt>setFixColumn(true)</tt>.
	 * 
	 * <p>
	 * The user can go by diagonals using the <tt>negate</tt> switches passed to
	 * the <tt>next(boolean, boolean)</tt> method. To go next in the ULBR diagonal,
	 * use <tt>next(true, false)</tt> and to go back use <tt>next(false, true)</tt>.
	 * To go next in the URBL diagonal, use <tt>next(true, true)</tt> and to go
	 * back use <tt>next(false, false)</tt>. To ensure proper navigation in diagonals,
	 * both <tt>fixRow</tt> and <tt>fixColumn</tt> properties should be set to
	 * <tt>false</tt>.
	 * 
	 * @author Shukant Pal
	 *
	 */
	public class CellIterator {
		
		private int curRow;
		private int curColumn;
		
		private boolean fixRow;
		private boolean fixColumn;
		
		/**
		 * Instantiates a new <tt>CellIterator</tt> that starts from the
		 * origin (upper-left corner). The <tt>fixRow</tt> and <tt>fixColumn
		 * </tt> properties are set to <tt>false</tt>.
		 */
		public CellIterator() {
			this.curRow = 0;
			this.curColumn = 0;
			this.fixRow = false;
			this.fixColumn = false;
		}
		
		/**
		 * Instantiates a new <tt>CellIterator</tt> that starts from the
		 * the square at <tt>(rowIndex, columnIndex)</tt> given coordinates.
		 * The <tt>fixRow</tt> and <tt>fixColumn</tt> properties are set to
		 * <tt>false</tt>.
		 * 
		 * @param rowIndex - the <b>y-coordinate</b> from the upper-left corner
		 * 					of the initial square.
		 * @param columnIndex - the <b>x-coordinate</b> from the upper-left
		 * 					corner of the initial square.
		 */
		public CellIterator(int rowIndex, int columnIndex) {
			this.curRow = rowIndex;
			this.curColumn = columnIndex;
			this.fixRow = false;
			this.fixColumn = false;
		}
		
		public CellIterator(int rowIndex, int columnIndex,
				boolean fixRow, boolean fixColumn) {
			this.curRow = rowIndex;
			this.curColumn = columnIndex;
			this.fixRow = fixRow;
			this.fixColumn = fixColumn;
		}
		
		/**
		 * Returns whether <tt>var</tt> is safe to increment as the index
		 * of a row or column, and that it will stay in-bounds on doing so.
		 */
		private boolean isSafeToInc(int var) {
			return (var != getSide() - 1);
		}
		
		/**
		 * Returns whether <tt>var</tt> is safe to decrement as the index
		 * of a row or column, and that it will stay in-bounds on doing so.
		 */
		private boolean isSafeToDec(int var) {
			return (var != 0);
		}
		
		/**
		 * Returns whether on calling <tt>next()</tt> the <tt>curRow</tt>
		 * property <b>may</b> be modified or not. 
		 */
		public boolean getFixRow() {
			return (fixRow);
		}
		
		/**
		 * Returns whether on calling <tt>next()</tt> the <tt>curColumn</tt>
		 * property <b>may</b> be modified or not.
		 */
		public boolean getFixColumn() {
			return (fixColumn);
		}
		
		/**
		 * Returns the row of the current square on which the iterator is
		 * standing.
		 */
		public int getCurRow() {
			return (curRow);
		}
		
		/**
		 * Returns the column of the current square on which the iterator is
		 * standing.
		 */
		public int getCurColumn() {
			return (curColumn);
		}
		
		/**
		 * <p>
		 * Sets the <tt>fixRow</tt> property to the desired value. On
		 * setting it to <tt>true</tt>, the row of the current square will
		 * not be changed on calling <tt>next()</tt> allowing the user to
		 * iterate in a row.
		 * 
		 * <p>
		 * But the <tt>fixRow</tt> and <tt>fixColumn</tt> properties may not
		 * be set to <tt>true</tt> simultaneously, and hence, if <tt>fixColumn
		 * </tt> is already set to <tt>true</tt> it will be changed back to
		 * <tt>false</tt> on calling this setter.
		 * 
		 * @param fixRow - the new value of the <tt>fixRow</tt> property
		 * @return - whether the <tt>fixColumn</tt> property was changed from
		 * 			<tt>true</tt> to <tt>false</tt> in this process.
		 */
		public boolean setFixRow(boolean fixRow) {
			this.fixRow = fixRow;
			
			if(getFixColumn()) {
				fixColumn = false;
				return (true);
			} else {
				return (false);
			}
		}
		
		/**
		 * <p>
		 * Sets the <tt>fixColumn</tt> property to the desired value. On
		 * setting it to <tt>true</tt>, the column of the current square will
		 * not be changed on calling <tt>next()</tt> allowing the user to
		 * iterate in a column.
		 * 
		 * <p>
		 * But the <tt>fixColumn</tt> & <tt>fixRow</tt> properties may not be
		 * set to <tt>true</tt> simultaneously, and hence, if <tt>fixRow</tt>
		 * is already set to <tt>true</tt> it will be changed back to <tt>
		 * false</tt> on calling this setter.
		 * 
		 * @param fixColumn - the new value of the <tt>fixColumn</tt> property
		 * @return - whether the <tt>fixRow</tt> property was changed from
		 * 			<tt>true</tt> to <tt>false</tt> in this process.
		 */
		public boolean setFixColumn(boolean fixColumn) {
			this.fixColumn = fixColumn;
			
			if(getFixRow()) {
				fixRow = false;
				return (true);
			} else {
				return (false);
			}
		}
		
		public void setCurRow(int curRow) {
			if(checkBounds(curRow)) {
				this.curRow = curRow;
			}
		}
		
		public void setCurColumn(int curColumn) {
			if(checkBounds(curColumn)) {
				this.curColumn = curColumn;
			}
		}
		
		
		public MoveType getSquareState() {
			return (getState(curRow, curColumn));
		}
		
		/**
		 * Allows the user to move to an adjacent square by incrementing
		 * the <tt>curRow, curColumn</tt> properties (unless the <tt>
		 * fixRow, fixColumn</tt> properties are <tt>true</tt>).
		 * 
		 * @return - the no. of times a property could not be changed due
		 * 			to becoming out-of-bounds after doing so:
		 * 	<ul>
		 * 		<li> If <tt>next</tt> returns 0, then no change failed.
		 * 		<li> If <tt>next</tt> returns 1, either <tt>curRow</tt> or
		 * 			<tt>curColumn</tt> could not be changed.
		 * 		<li> If <tt>next</tt> returns 2, <tt>curRow</tt> & <tt>
		 * 			curColumn</tt> both could not be changed.
		 *  </ul>
		 *  
		 *  Note that if <tt>fixRow</tt> or <tt>fixColumn</tt> were set, that
		 *  will not count to the returned value.
		 */
		public int next() {
			int changeFail = 0;
			
			if(!getFixRow()) {
				if(isSafeToInc(curRow))
					++(curRow);
				else
					++(changeFail);
			}
			
			if(!getFixColumn()) {
				if(isSafeToInc(curColumn))
					++(curColumn);
				else
					++(changeFail);
			}
			
			return (changeFail);
		}
		
		/**
		 * Allows the user to move to an adjacent square by adding or
		 * subtracting one (based on the <tt>negateRow, negateColumn</tt>
		 * switches) to or from the <tt>curRow, curColumn</tt> properties.
		 * 
		 * @param negateRow - whether to subtract one instead of add one
		 * 					from the <tt>curRow</tt> property
		 * @param negateColumn - whether to subtract one instead of add one
		 * 					from the <tt>curColumn</tt> property
		 * @return - the no. of times a property could not be changed due
		 * 			to becoming out-of-bounds after doing so:
		 * 	<ul>
		 * 		<li> If <tt>next</tt> returns 0, then no change failed.
		 * 		<li> If <tt>next</tt> returns 1, either <tt>curRow</tt> or
		 * 			<tt>curColumn</tt> could not be changed.
		 * 		<li> If <tt>next</tt> returns 2, <tt>curRow</tt> & <tt>
		 * 			curColumn</tt> both could not be changed.
		 *  </ul>
		 */
		public int next(boolean negateRow, boolean negateColumn) {
			int changeFail = 0;
			
			if(!getFixRow()) {
				if(!negateRow && isSafeToInc(curRow)) {
					++(curRow);
				} else if(negateRow && isSafeToDec(curRow)) {
					--(curRow);
				} else {
					++(changeFail);
				}
			}
			
			if(!getFixColumn()) {
				if(!negateColumn && isSafeToInc(curColumn)) {
					++(curColumn);
				} else if(negateColumn && isSafeToDec(curColumn)){
					--(curColumn);
				} else {
					++(changeFail);
				}
			}
			
			return (changeFail);
		}
		
		public CellIterator closestEmpty() {
			if(getSquareState() == MoveType.EMPTY)
				return (this);
			
			do {
				if(getSquareState() == MoveType.EMPTY)
					break;
			} while(next() == 0);
			
			return (this);
		}

	}
	
	/**
	 * Provides an automatic way to pass the negation arguments to the
	 * <tt>CellIterator.next</tt> method.
	 * 
	 * @author Shukant Pal
	 */
	public class NegatableCellIterator extends CellIterator {
		
		boolean negateRow;
		boolean negateColumn;
		
		NegatableCellIterator() {
			super();
			this.negateRow = false;
			this.negateColumn = false;
		}
		
		NegatableCellIterator(int row, int column) {
			super(row, column);
			this.negateRow = false;
			this.negateColumn = false;
		}
		
		NegatableCellIterator(int row, int column, boolean fixRow, boolean fixColumn) {
			super(row, column, fixRow, fixColumn);
		}
		
		NegatableCellIterator(int row, int column, boolean fixRow,
				boolean fixColumn, boolean negateRow, boolean negateColumn) {
			super(row, column, fixRow, fixColumn);
			this.negateRow = negateRow;
			this.negateColumn = negateColumn;
		}
		
		public boolean getNegateRow() {
			return (negateRow);
		}
		
		public boolean getNegateColumn() {
			return (negateColumn);
		}
		
		public void setNegateRow(boolean negateRow) {
			this.negateRow = negateRow;
		}
		
		public void setNegateColumn(boolean negateColumn) {
			this.negateColumn = negateColumn;
		}
		
		public int next() {
			return (super.next(this.negateRow, this.negateColumn));
		}
	
	}
	
	/**
	 * <p>
	 * Iterates over a series of lines (rows, columns or diagonals) given
	 * to it during construction as an array. It provides a read-only
	 * interface to allow <tt>Player</tt> player objects to access the
	 * linear data of <tt>this</tt> board safely.
	 * 
	 * <p>
	 * This iterator also supports jump from one line to another, until the
	 * given index becomes "out-of-bounds".
	 * 
	 * @author Shukant Pal
	 *
	 */
	public class LineIterator {
		
		private LineState[] stateData;
		private int lineIndex;
		
		/**
		 * Instantiates a <tt>LineIterator</tt> from the given series
		 * of lines, starting from the <tt>0th</tt> index.
		 * 
		 * @param stateData - the series of lines on which the iterator
		 * 					will iterate.
		 */
		LineIterator(LineState[] stateData) {
			this.stateData = stateData;
			this.lineIndex = 0;
		}
		
		/**
		 * Instantiates a <tt>LineIterator</tt> from the given series
		 * of lines, starting from the <tt>startIndex</tt> index.
		 * 
		 * @param stateData - the series of lines on which the iterator
		 * 					will iterate.
		 * @param startIndex - the index from which the iterator will
		 * 					start.
		 */
		LineIterator(LineState[] stateData, int startIndex) {
			this.stateData = stateData;
			this.lineIndex = startIndex;
		}
		
		/**
		 * Returns the index of the current line in the given series.
		 */
		public int getLineIndex() {
			return (lineIndex);
		}
		
		/**
		 * Returns the number of squares filled by player <b>O</b> in
		 * this line.
		 */
		public int getOFilled() {
			return (stateData[lineIndex].getOFilled());
		}
		
		/**
		 * Returns the number of squares filled by player <b>X</b> in
		 * this line.
		 */
		public int getXFilled() {
			return (stateData[lineIndex].getXFilled());
		}
		
		public int getFilled() {
			return (getXFilled() + getOFilled());
		}
		
		public int getFilled(MoveType assignedSide) {
			if(assignedSide == MoveType.O) {
				return (getOFilled());
			} else {
				return (getXFilled());
			}
		}

		/**
		 * Returns whether the line is dirty w.r.t given observer, i.e.
		 * if the line is filled by squares of the opponent player.
		 * 
		 * @param observer - the player who is checking this line
		 */
		public boolean isLineDirty(MoveType observer) {
			if(observer == MoveType.O && getXFilled() > 0) {
				return (true);
			} else if(observer == MoveType.X && getOFilled() > 0) {
				return (true);
			} else {
				return (false);
			}
		}
		
		/**
		 * Sets the current line index in <tt>stateData</tt> allowing the
		 * user to technically jump. The given <tt>newIndex</tt> index must
		 * be "in-bounds", which can be checked by
		 * 		<pre>newIndex >= 0 && newIndex < board.getSide()</pre>
		 * 
		 * @param newIndex - the index of the line to which this iterator
		 * 					should jump to.s
		 */
		public void setLineIndex(int newIndex) {
			if(newIndex >= 0 && newIndex < stateData.length) {
				lineIndex = newIndex;
			} else {
				throw new IllegalArgumentException("Index out of bounds");
			}
		}
		
		/**
		 * Goes to the next consecutive line in this series, if it is in
		 * bounds.
		 * 
		 * @return - <tt>true</tt>, if the next line was fetched; <tt>
		 * 			false</tt>, if the current line was already the last one.
		 */
		public boolean next() {
			if(lineIndex == stateData.length - 1)
				return (false);
			
			++(lineIndex);
			return (true);
		}
		
		/**
		 * Goes to the last consecutive line in this series, if it is
		 * bounds.
		 * 
		 * @return - <tt>true</tt>, if the last line was fetched; <tt>
		 * 			false</tt>, if the current line was the first one, .i.e.
		 * 			the <tt>0th</tt> one.
		 */
		public boolean last() {
			if(lineIndex == 0)
				return (false);
			
			--(lineIndex);
			return (true);
		}
		
		public int lineSize() {
			return (getSide());
		}
	}
	
	private int side;
	private int dirtyCount;
	private int[] hotspot;
	private Cell grid[][];
	private MoveType nextState;
	
	private LineState rowStates[];
	private LineState columnStates[];
	private LineState diagonalStates[];
	
	private LineIterator rowItr;
	private LineIterator colItr;
	private LineIterator diaItr;
	
	private int winCacheIndex;
	private GameController.LineIdentifier winCacheIdentifier;
	
	private void initAll(LineState lineStatesArray[]) {
		for (int idx = 0; idx < lineStatesArray.length; idx++) {
			lineStatesArray[idx] = new LineState();
		}
	}
	
	private boolean checkBounds(int var) {
		if(var < 0 || var > side) {
			return (false);
		} else {
			return (true);
		}
	}
	
	private boolean checkBounds(int row, int column) {
		if(row < 0 || column < 0
				|| row >= side || column >= side) {
			return (false);
		} else {
			return (true);
		}
	}
	
	public Board(int side) {
		this.side = side;
		this.dirtyCount = 0;
		this.hotspot = new int[2];
		this.grid = new Cell[side][side];
		this.nextState = MoveType.O;
		
		rowStates = new LineState[getSide()];
		columnStates = new LineState[getSide()];
		diagonalStates = new LineState[2];
		
		rowItr = rowIterator();
		colItr = columnIterator();
		diaItr = diagonalIterator();
	
		initAll(rowStates);
		initAll(columnStates);
		initAll(diagonalStates);
		
		hotspot[0] = hotspot[1] = 0;
		
		for(int i=0; i<side; i++) {
			for(int j=0; j<side; j++) {
				grid[i][j] = new Cell();
			}
		}
	}
	
	public int getSide() {
		return (side);
	}
	
	public int getArea() {
		return (getSide() * getSide());
	}
	
	public int getEmptyArea() {
		return (getArea() - dirtyCount);
	}
	
	public int[] getHotspot() {
		return (hotspot);
	}
	
	public MoveType getState(int row, int column) {
		return (grid[row][column].getState());
	}
	
	public MoveType getNextState() {
		return (nextState);
	}
	
	public int getWinCacheIndex() {
		return (winCacheIndex);
	}
	
	public GameController.LineIdentifier getWinCacheIdentifier() {
		return (winCacheIdentifier);
	}
	
	public boolean setState(MoveType newState,
			int row, int column) {
		if(!checkBounds(row, column))
			throw new IllegalArgumentException(
					"(Row, Column) out of bounds for" + side + "x" + side + "board.");
		
		hotspot[0] = row;
		hotspot[1] = column;
		
		if (newState != MoveType.EMPTY) {
			if(grid[row][column].getState() != MoveType.EMPTY)
				return (false);
			
			++(dirtyCount);
			
			if(newState == MoveType.O)
				nextState = MoveType.X;
			else
				nextState = MoveType.O;
		}
		
		boolean wasFilled = grid[row][column].setState(newState);
		
		if (wasFilled) {
			rowStates[row].fillAs(newState);
			columnStates[column].fillAs(newState);
			
			if(row == column)
				diagonalStates[0].fillAs(newState);
			
			if(row + column == getSide() - 1)
				diagonalStates[1].fillAs(newState);
		} else {
			System.out.println("Warning *:* The (" + row + "," + column + ") square could" +
								"not be set, because it was already set.");
		}
		
		return (wasFilled);
	}
	
	/**
	 * Instantiates a new <tt>Board.LineIterator</tt> on the columns
	 * of <tt>this</tt> board, that starts from the <tt>0th</tt>
	 * column.
	 */
	public LineIterator columnIterator() {
		return (new LineIterator(columnStates));
	}
	
	/**
	 * Instantiates a new <tt>Board.LineIterator</tt> on the columns
	 * of <tt>this</tt> board, that starts from the given column-index
	 * <tt>columnIndex</tt>.
	 * 
	 * @param columnIndex - the starting column for the iterator
	 */
	public LineIterator columnIterator(int columnIndex) {
		return (new LineIterator(columnStates, columnIndex));
	}
	
	/**
	 * Instantiates a new <tt>Board.LineIterator</tt> on the two diagonals
	 * of <tt>this</tt> board, that starts from the diagonal <tt>DIAGONAL_ULBR</tt>
	 * and ends at diagonal <tt>DIAGONAL_URBL</tt>.
	 */
	public LineIterator diagonalIterator() {
		return (new LineIterator(diagonalStates));
	}
	
	/**
	 * Instantiates a new <tt>Board.LineIterator</tt> on the rows of
	 * <tt>this</tt> board, that starts from the <tt>0th</tt> row.
	 */
	public LineIterator rowIterator() {
		return (new LineIterator(rowStates));
	}
	
	/**
	 * Instantiates a new <tt>Board.LineIterator</tt> on the rows of
	 * <tt>this</tt> board, that starts from the given row-index
	 * <tt>rowIndex</tt>.
	 *  
	 * @param rowIndex - the starting row for the iterator
	 */
	public LineIterator rowIterator(int rowIndex) {
		return (new LineIterator(rowStates, rowIndex));
	}
	
	/**
	 * <p>
	 * Checks all capturable lines on this <code>Board</code> object, and if
	 * any is fully filled with only one type of <code>SquareState</code>, it
	 * declares the game winner! Once the game is won, further moves are
	 * supported, but the UI shall end the game.
	 * 
	 * <p>
	 * This method uses <code>LineState</code> objects to keep track of the no.
	 * of X's and O's set on a line. These heuristics are updated while using
	 * the <code>setState</code> method.
	 * 
	 * @return The <code>SquareState</code> of the <code>Player</code> which
	 * 			has won the game; <code>SquareState.EMPTY</code>, if the game
	 * 			is to be continued, and no one has won!
	 */
	public MoveType findWinner() {
		rowItr.setLineIndex(0);
		
		do {
			if(rowItr.getFilled() == getSide()
					&& rowStates[rowItr.getLineIndex()].isPure()) {
				winCacheIndex = rowItr.getLineIndex();
				winCacheIdentifier = GameController.LineIdentifier.ROW;
				return (rowItr.getOFilled() == 0 ? MoveType.X : MoveType.O);
			}
		} while(rowItr.next());
		
		colItr.setLineIndex(0);
		
		do {
			if(colItr.getFilled() == getSide()
					&& columnStates[colItr.getLineIndex()].isPure()) {
				winCacheIndex = colItr.getLineIndex();
				winCacheIdentifier = GameController.LineIdentifier.COLUMN;
				return (colItr.getOFilled() == 0 ? MoveType.X : MoveType.O);
			}
		} while(colItr.next());
		
		diaItr.setLineIndex(0);
		
		do {
			if(diaItr.getFilled() == getSide()
					&& diagonalStates[diaItr.getLineIndex()].isPure()) {
				winCacheIndex = diaItr.getLineIndex();
				
				if(winCacheIndex == 0) {
					winCacheIdentifier = GameController.LineIdentifier.DIAGONAL_ULBR;
				} else {
					winCacheIdentifier = GameController.LineIdentifier.DIAGONAL_URBL;
				}
				return (diaItr.getOFilled() == 0 ? MoveType.X : MoveType.O);
			}
		} while(diaItr.next());
		
		return (MoveType.EMPTY);
	}
	
}
