import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DuckHunt extends Application {
	static final double SCALE = 3, VOLUME = 0.025;
	static double WIDTH, HEIGHT;
	static Stage primaryStage;
	static Scene entryScene, settingScene, level1Scene, level2Scene, level3Scene, level4Scene, level5Scene, level6Scene;
	static MediaPlayer gunshot, duckFalls, levelCompleted, gameCompleted, gameOver, title, intro;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		new GameGUI();

		DuckHunt.primaryStage = primaryStage;
		entryScene = new Scene(new Entry().pane, WIDTH * SCALE, HEIGHT * SCALE);
		GameGUI.entrySceneKey(primaryStage);

		primaryStage.setTitle("HUBBM Duck Hunt");
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.getIcons().add(new Image("assets/favicon/1.png"));
		primaryStage.setScene(entryScene);
		primaryStage.show();
	}
}