package org.silcos.tictactoe.ui;

import org.silcos.tictactoe.GameController;
import org.silcos.tictactoe.GameController.GameEvent;
import org.silcos.tictactoe.GameController.GameWonEvent;
import org.silcos.tictactoe.MoveType;
import org.silcos.tictactoe.ui.board.BoardButton;
import org.silcos.tictactoe.ui.board.BoardButtonFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

/**
 * Controls a <tt>GameController</tt> instance and allows the user
 * to interact with it & play the game. Each <tt>TicTacToeWindow</tt>
 * handles only one instance of a game initiated by a <tt>TicTacToeApp</tt>
 * and exposes it on the screen.
 * 
 * @author Shukant Pal
 *
 */
public class TicTacToeWindow {

	private TicTacToeApp appContext;
	private GameController gameController;
	private BoardButtonFactory gameButtonFactory;
	private BoardButton.PlayerMoveHandler moveHandler;
	private BoardButton buttonMatrix[][];
	
	private BorderPane layout;
	
	@FXML
	private GridPane boardGrid;
	
	public static final String amateurBoard = "3x3 | Amateur";
	public static final String mediumBoard = "5x5 | Medium";
	public static final String expertBoard = "7x7 | Expert";
	public static final String gmBoard = "9x9 | Grandmaster";
	
	private static final RowConstraints boardRowConstraints = new RowConstraints();
	private static final ColumnConstraints boardColumnConstraints = new ColumnConstraints(); 
	
	static {
		boardRowConstraints.setPercentHeight(100);
		boardColumnConstraints.setPercentWidth(100);
	}
	
	static Font defaultBoardGridFont = new Font(36);
	
	static Font defaultBoardGridFonts[] = {
			new Font(36),
			new Font(24),
			new Font(14),
			new Font(11)
	};
	
	@FXML
	private void handleSizeChangerAction(ActionEvent e) {
		if(game().isSinglePlayer()) {
			int newBoardSize;
			
			switch(((MenuItem) e.getSource()).getText()) {
			case amateurBoard:
				System.out.println("New amateur board");
				newBoardSize = 3;
				break;
			case mediumBoard:
				newBoardSize = 5;
				break;
			case expertBoard:
				newBoardSize = 7;
				break;
			case gmBoard:
				newBoardSize = 9;
				break;
			default:
				newBoardSize = 3;
				break;
			}
			
			gameController = appContext.getControllerFactory()
					.newSinglePlayerGameController(newBoardSize);
			buildBoard(this);
		}
	}
	
	public TicTacToeWindow(TicTacToeApp appContext, GameController gameInstance) {
		this.appContext = appContext;
		this.gameController = gameInstance;
		this.moveHandler = new BoardButton.PlayerMoveHandler();
	}
	
	public GameController game() {
		return (gameController);
	}
	
	/**
	 * Instantiates a new <tt>TicTacToeWindow</tt> using the
	 * <tt>TicTacToeWindow.fxml</tt> FXML file, populating its board-grid
	 * fully with new <tt>BoardButton</tt> objects. It also binds the board
	 * buttons with a <tt>BoardButton.PlayerMoveHandler</tt> mouse-click
	 * handler so that the window "works".
	 * 
	 * @param appContext - the app-context in which this window will work in
	 * @param gameInstance - the instance of the game which this window will operate
	 * 						on! It must get a unique instance otherwise corruption will
	 * 						occur!
	 */
	public static TicTacToeWindow newWindow(TicTacToeApp appContext, GameController gameInstance) {
		TicTacToeWindow baseUI = new TicTacToeWindow(appContext, gameInstance);
		
		FXMLLoader layoutLoader = new FXMLLoader();
		layoutLoader.setLocation(TicTacToeWindow.class.getResource("TicTacToeWindow.fxml"));
		layoutLoader.setController(baseUI);
	
		try {
			baseUI.layout = layoutLoader.load();
		} catch(Exception e) {
			e.printStackTrace();
			return (null);
		}
		
		buildBoard(baseUI);
		
		return (baseUI);
	}
	
	private static void buildBoard(TicTacToeWindow baseUI) {
		GridPane oldBoardGrid = baseUI.boardGrid;
		GridPane newBoardGrid = new GridPane();
		
		for(int constIndex = 0; constIndex < baseUI.game().getBoardSize(); constIndex++) {
			newBoardGrid.getRowConstraints().add(boardRowConstraints);
			newBoardGrid.getColumnConstraints().add(boardColumnConstraints);
		}
		
		((BorderPane) oldBoardGrid.getParent()).setCenter(newBoardGrid);
		baseUI.boardGrid = newBoardGrid;
		baseUI.gameButtonFactory = new BoardButtonFactory(baseUI.game());
		baseUI.buttonMatrix = new BoardButton
				[baseUI.game().getBoardSize()][baseUI.game().getBoardSize()];
		
		for(int rowIndex = 0; rowIndex < baseUI.game().getBoardSize(); rowIndex++) {
			for(int colIndex = 0; colIndex < baseUI.game().getBoardSize(); colIndex++) {
				BoardButton nextBtn = baseUI.gameButtonFactory.newBoardButton();
				nextBtn.setMaxHeight(Double.MAX_VALUE);
				nextBtn.setMaxWidth(Double.MAX_VALUE);
				nextBtn.setMinHeight(100 * 3 / baseUI.game().getBoardSize());
				nextBtn.setMinWidth(100 * 3 / baseUI.game().getBoardSize());
				nextBtn.setFont(defaultBoardGridFonts[(baseUI.game().getBoardSize() - 3)/2]);
				nextBtn.setOnMouseClicked(baseUI.moveHandler);
				
				baseUI.buttonMatrix[rowIndex][colIndex] = nextBtn;
				baseUI.boardGrid.add(nextBtn, nextBtn.getColumn(), nextBtn.getRow());
			}
		}
		
		baseUI.gameController.addMoveHandler(
				(MoveType m, int rowIdx, int colIdx) -> {
					System.out.println("Handling move...");
					Platform.runLater(() -> {
						baseUI.buttonMatrix[rowIdx][colIdx].setText(m.toString());
					});
				}
			);
	
		baseUI.gameController.addGameListener(
				(GameEvent change) -> {
					if(change instanceof GameWonEvent) {
						GameWonEvent wonEvent = (GameWonEvent) change;
						System.out.println("Game over!");
					}
				}
			);
	}
	
	public Scene newHostScene() {
		return (new Scene(layout));
	}
}
