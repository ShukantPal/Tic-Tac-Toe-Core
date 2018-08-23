package org.silcos.tictactoe;

import java.util.LinkedList;

import org.silcos.tictactoe.Board.LineIterator;

/**
 * Exposes utility methods for 
 * 
 * @author Shukant Pal
 */
public class BoardAnalyzer {

	/**
	 * Holds data on empty squares on the board, relating to their
	 * relation with clean rows, column, diagonals, chances of winning,
	 * & forced moves.
	 * 
	 * @author Shukant Pal
	 */
	public class CellRelations {
		
		private int row, column;
		private boolean rowClean, columnClean;
		private int diagonalsClean, diagonalsCount;
		
		/**
		 * Establishes the relation for the <tt>Cell</tt> with diagonal
		 * lines.
		 */
		private void putDiagonalRelations() {
			diagonalsCount = 0;
			diagonalsClean = 0;
			
			if(row == column) {
				diaItr.setLineIndex(0);
				++(diagonalsCount);
				
				if(!diaItr.isLineDirty(observer))
					++(diagonalsClean);
			}
			
			if(row + column == getLinearSize() - 1) {
				diaItr.setLineIndex(1);
				++(diagonalsCount);
				
				if(!diaItr.isLineDirty(observer))
					++(diagonalsClean);
			}
		}
		
		CellRelations() {
			row = rowItr.getLineIndex();
			column = colItr.getLineIndex();
			
			rowClean = !rowItr.isLineDirty(observer);
			columnClean = !colItr.isLineDirty(observer);
			putDiagonalRelations();
		}
		
		/**
		 * Returns whether the row on which this cell lies on, is clean
		 * for the observer or not.
		 */
		public boolean isRowClean() {
			return (rowClean);
		}
		
		/**
		 * Returns whether the column on which this cell lies on, is celan
		 * for the observer or not.
		 */
		public boolean isColumnClean() {
			return (columnClean);
		}
		
		/**
		 * Returns the no. of diagonals on which this cell lies on & that
		 * are clean for the observer.
		 */
		public int getDiagonalsClean() {
			return (diagonalsClean);
		}
		
		/**
		 * Returns the total no. of capturable lines on which the cell lies
		 * on & that are clean for the observer.
		 */
		public int getTotalClean() {
			return ((rowClean ? 1 : 0) + 
					(columnClean ? 1 : 0) +
					diagonalsClean);
		}
		
	}
	
	Board sampleSpace;
	MoveType observer;
	
	LineIterator rowItr;
	LineIterator colItr;
	LineIterator diaItr;
	
	LinkedList<CellRelations> relationTable[];
	
	private void addCell(CellRelations moveReport) {
		relationTable[moveReport.getTotalClean()].add(moveReport);
	}
	
	@SuppressWarnings("unchecked")
	private BoardAnalyzer(Board sampleSpace, MoveType observer, LineIterator rowItr,
			LineIterator colItr, LineIterator diaItr) {
		this.sampleSpace = sampleSpace;
		this.observer = observer;

		this.rowItr = rowItr;
		this.colItr = colItr;
		this.diaItr = diaItr;
		
		this.relationTable = new LinkedList[5];	
	}

	public int getLinearSize() {
		return (sampleSpace.getSide());
	}
	
	/**
	 * Instantiates a <tt>BoardAnalyzer</tt> holding a <tt>CellRelations</tt>
	 * table built. The <tt>observer</tt> can then extract the best possible
	 * move based on the current state of the board.
	 * 
	 * @param sampleSpace - The <tt>Board</tt> on which the <tt>BoardAnalyzer</tt>
	 * 					will hold a <tt>CellRelation</tt> table, about its empty
	 * 					squares & their linear relations.
	 * @param observer - The <tt>Player</tt> side which is analyzing the table
	 * @param rowItr - A <tt>Board.LineIterator</tt> configured for row iteration.
	 * @param colItr - A <tt>Board.LineIterator</tt> configured for column iteration.
	 * @param diaItr - A <tt>Board.LineIterator</tt> configured for diagonal iteration.
	 */
	public static BoardAnalyzer newAnalyzer(Board sampleSpace, MoveType observer,
			LineIterator rowItr, LineIterator colItr, LineIterator diaItr) {
		
		BoardAnalyzer analyzer = new BoardAnalyzer(sampleSpace, observer,
				rowItr, colItr, diaItr);
		
		for(int i = 0; i < sampleSpace.getSide(); i++) {
			rowItr.setLineIndex(i);
			
			for(int j = 0; j < sampleSpace.getSide(); j++) {
				colItr.setLineIndex(j);
				
				if(sampleSpace.getState(i, j) == MoveType.EMPTY) {
					CellRelations pmoveReport = analyzer.new CellRelations();
					analyzer.addCell(pmoveReport);
				}
			}
		}
		
		return (analyzer);
	}
	
	/**
	 * <p>
	 * Runs the <tt>Board.LineIterator</tt> until a line is found which can be
	 * used to immediately win the game, i.e. is clean for <tt>observer</tt> &
	 * has only one <tt>EMPTY</tt> square.
	 * 
	 * <p>
	 * The <tt>observer</tt> is should be <tt>MoveType.O</tt> or <tt>MoveType.X
	 * </tt> depending on the <tt>Player</tt> expecting a victory. The state
	 * <tt>MoveType.EMPTY</tt> will cause an <tt>IllegalArgumentException</tt> to
	 * be thrown.
	 * 
	 * @param lineSet - an iterator by which the lines in the given set can be
	 * 					accessed serially
	 * @param observer - the player for which a capturable line is to be searched
	 * @return - the iterator in a state referring to the winning line; null, if
	 * 			no line is capturable at the moment.
	 */
	public static Board.LineIterator getCapturable(Board.LineIterator lineSet, MoveType observer) {
		if(observer == MoveType.EMPTY)
			throw new IllegalArgumentException("The observer cannot be of type EMPTY");
		
		do {
			if(!lineSet.isLineDirty(observer) &&
					lineSet.getFilled(observer) == lineSet.lineSize() - 1) {
				return (lineSet);
			}
 		} while(lineSet.next());
		
		return (null);
	}
	
	/**
	 * Instantiates a <tt>Board.CellIterator</tt> that finds cells inside a
	 * row.
	 * 
	 * @param row - the index of the row on the board
	 * @param board - the <tt>Board</tt> of which the iterator is required
	 */
	public static Board.CellIterator newRowFinder(int row, Board board) {
		return (board.new CellIterator(row, 0, true, false));
	}
	
	/**
	 * Instantiates a <tt>Board.CellIterator</tt> that finds cells inside a
	 * column.
	 * 
	 * @param column - the index of the column on the board
	 * @param board - the <tt>Board</tt> of which the iterator is required
	 */
	public static Board.CellIterator newColumnFinder(int column, Board board) {
		return (board.new CellIterator(0, column, false, true));
	}
	
	/**
	 * Instantiates a <tt>Board.CellIterator</tt> that finds squares on
	 * a diagonal of the board.
	 * 
	 * @param diagonalIndex - 0, for the ULBR diagonal & 1, for the URBL diagonal
	 * @param board - the <tt>Board</tt> of which the iterator is required
	 * @return - A <tt>Board.CellIterator</tt> that iterates on a diagonal
	 */
	public static Board.CellIterator newDiagonalFinder(int diagonalIndex, Board board) {
		if(diagonalIndex == 0) {
			return (board.new CellIterator(0, 0, false, false));
		} else {
			return (board.new NegatableCellIterator(0, board.getSide() - 1,
					false, false, false, true));
		}
	}
	
}
