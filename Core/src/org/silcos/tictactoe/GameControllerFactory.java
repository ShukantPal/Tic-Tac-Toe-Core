package org.silcos.tictactoe;

/**
 * Factory for instantiating <code>GameController</code> objects from
 * the application's <code>UserPreferences</code> objects.
 * 
 * @author Shukant Pal
 *
 */
public class GameControllerFactory {
	
	private UserPreferences lastSettings = null;
	
	public GameController newSinglePlayerGameController() {
		return (new GameController(lastSettings.getBoardSize(), lastSettings.getDefaultSide()));
	}
	
	public GameController newSinglePlayerGameController(int boardSize) {
		lastSettings.setBoardSize(boardSize);
		return (new GameController(boardSize, lastSettings.getDefaultSide()));
	}
	
	/**
	 * Instantiates a new single-player <tt>GameController</tt> in
	 * which the human player gets the <tt>settings.getDefaultSide()</tt>
	 * side. The other is a <tt>ComputerPlayer</tt> based on the level.
	 * 
	 * @param settings - the user's preferences while playing the game in
	 * 					this context.
	 */
	public GameController newSinglePlayerGameController(UserPreferences settings) {
		lastSettings = settings;
		return (new GameController(settings.getBoardSize(), settings.getDefaultSide()));
	}
	
	/**
	 * Instantiates a new two-player <tt>GameController</tt> where both
	 * players are a <tt>HumanPlayer</tt>. The side which they get is
	 * ambiguous for the application.
	 * 
	 * @param settings - the user's preferences while playing the game in
	 * 					this context.
	 */
	public GameController newTwoPlayerGameController(UserPreferences settings) {
		return (new GameController(settings.getBoardSize()));
	}
}
