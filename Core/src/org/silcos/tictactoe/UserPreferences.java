package org.silcos.tictactoe;

/**
 * Stores various settings that the user prefers to play with in
 * the game. A  <code>UserPreferences</code> object can be passed
 * while constructing a <code>GameController</code> via the
 * <code>GameControllerFactory</code>. If the front-end game does not
 * support user-settings, then the <code>defaultSettings</code> object
 * can be used instead.
 * 
 * @author Shukant Pal
 *
 */
public final class UserPreferences {

	/**
	 * The default board size is 3, when the user has not specified
	 * any size.
	 */
	private static int DEFAULT_BOARD = 3;

	/**
	 * If the front-end doesn't support setting user preferences, then the
	 * default object can be passed to <code>GameControllerFactory</code>
	 * by the <code>UserPreferences.getDefaultSettings()</code> method.
	 */
	private static UserPreferences defaultSettings = new UserPreferences();
	
	/**
	 * The user's preferred board size.
	 */
	private int boardSize;
	
	/**
	 * The side of the <code>HumanPlayer</code> while playing in
	 * single-player mode.
	 */
	private MoveType defaultSide;
	
	private UserPreferences() {
		this.boardSize = DEFAULT_BOARD;
		this.defaultSide = MoveType.O;
	}
	
	/**
	 * Returns the user's preferred game board size. If not set, the
	 * default (3x3) will be returned.
	 */
	public int getBoardSize() {
		return (boardSize);
	}
	
	/**
	 * Returns the user's preferred side, in single player mode. If not
	 * set, the default (MoveType.O) will be returned.
	 */
	public MoveType getDefaultSide() {
		return (defaultSide);
	}
	
	/**
	 * Sets the user's preferred board size. The game controller supports
	 * only odd sizes in the range [3, 11], and if the given size is out
	 * of this range, or even in this range, then an
	 * <code>IllegalArgumentException</code> is thrown.
	 * 
	 * @param boardSize - the board-size of the user's preference
	 */
	public void setBoardSize(int boardSize) {
		if(boardSize < 3 || boardSize > 11)
			throw new IllegalArgumentException(
					"The default board size can be any odd number between" +
					"3x3 and 11x11. The given settings cannot be enforced.");
		
		this.boardSize = boardSize;	
	}
	
	/**
	 * Sets the preferred side of the user in single player mode. This must
	 * be <code>MoveType.O</code> or <code>MoveType.X</code>, and
	 * <code>MoveType.EMPTY</code> must not be passed. An
	 * <code>IllegalArgumentException</code> is thrown when the
	 * <code>MoveType.EMPTY</code> side is selected.
	 * 
	 * @param defaultSide - the user's preferred side in single-player
	 * 			mode. The <code>GameControllerFactory</code> uses this
	 * 			property to assign the <code>HumanPlayer</code> a side.
	 */
	public void setDefaultSide(MoveType defaultSide) {
		if(defaultSide == MoveType.EMPTY)
			throw new IllegalArgumentException(
						"The default human player side must be O or X, the"
						+ "state EMPTY is not applicable in this context.");
		
		this.defaultSide = defaultSide;
	}
	
	/**
	 * Returns the default settings defined for the Tic-Tac-Toe
	 * game. A sample game may also use this instead of loading the
	 * preferences from a configuration file.
	 */
	public static UserPreferences getDefaultSettings() {
		return (defaultSettings);
	}
	
	/**
	 * Instantiates a new <code>UserPreferences</code> object with
	 * blank/default settings, as if copied from the default-settings.
	 * @return
	 */
	public static UserPreferences blankSettings() {
		return (new UserPreferences());
	}
}
