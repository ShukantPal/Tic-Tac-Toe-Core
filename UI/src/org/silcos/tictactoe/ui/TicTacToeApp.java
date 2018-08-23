package org.silcos.tictactoe.ui;

import java.net.URL;

import org.silcos.tictactoe.Board;
import org.silcos.tictactoe.GameControllerFactory;
import org.silcos.tictactoe.UserPreferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Launches the JavaFX TicTakToe UI and constructs
 * <code>primaryStage</code>. The only instance can be accessed using the
 * <code>TicTakToeApp.defaultInstance</code> field.
 * 
 * @since TicTakToe 1.0
 * @author Shukant Pal
 */
public class TicTacToeApp extends Application {

	Stage primaryStage;
	Scene gameUI;
	Board gameSet;
	
	public static TicTacToeApp defaultInstance;
	
	private GameControllerFactory gcf = new GameControllerFactory();
	private TicTacToeWindow win;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.gameSet = new Board(3);
		defaultInstance = this;
		
		win = TicTacToeWindow.newWindow(this,
				gcf.newSinglePlayerGameController(
						UserPreferences.getDefaultSettings()));

		primaryStage.setScene(win.newHostScene());
		primaryStage.show();
	
		primaryStage.sizeToScene();
	}
	
	public GameControllerFactory getControllerFactory() {
		return (gcf);
	}
	
	public static void main(String[] args) {
		System.out.println("Launching Application \"TicTakToe\"...");
		launch(args);
	}

	@Override
	public void stop() {
		win.game().close();
	}
}
