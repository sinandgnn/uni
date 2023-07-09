import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameGUI extends DuckHunt {

	public GameGUI() {
		gunshot = new MediaPlayer(new Media(getClass().getResource("/assets/effects/Gunshot.mp3").toString()));
		duckFalls = new MediaPlayer(new Media(getClass().getResource("/assets/effects/DuckFalls.mp3").toString()));
		levelCompleted = new MediaPlayer(new Media(getClass().getResource("/assets/effects/LevelCompleted.mp3").toString()));
		gameCompleted = new MediaPlayer(new Media(getClass().getResource("/assets/effects/GameCompleted.mp3").toString()));
		gameOver = new MediaPlayer(new Media(getClass().getResource("/assets/effects/GameOver.mp3").toString()));
		title = new MediaPlayer(new Media(getClass().getResource("/assets/effects/Title.mp3").toString()));
		intro = new MediaPlayer(new Media(getClass().getResource("/assets/effects/Intro.mp3").toString()));
		gunshot.setVolume(VOLUME);
		duckFalls.setVolume(VOLUME);
		levelCompleted.setVolume(VOLUME);
		gameCompleted.setVolume(VOLUME);
		gameOver.setVolume(VOLUME);
		title.setVolume(VOLUME);
		intro.setVolume(VOLUME);

		WIDTH = Settings.backgroundImageView.getLayoutBounds().getWidth();
		HEIGHT = Settings.backgroundImageView.getLayoutBounds().getHeight();

		Settings.crosshairImageView.setX((WIDTH / 2.0 - Settings.crosshairImage.getWidth() / 2) * SCALE);
		Settings.crosshairImageView.setY((HEIGHT / 2.0 - Settings.crosshairImage.getHeight() / 2) * SCALE);
		Settings.crosshairImageView.setFitWidth(Settings.crosshairImage.getWidth() * SCALE);
		Settings.crosshairImageView.setFitHeight(Settings.crosshairImage.getHeight() * SCALE);
	}

	/**
	 * To make the text a flash text.
	 *
	 * @param text The text which is the flashing.
	 */
	public static void animation(Text text) {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.ZERO, e -> text.setVisible(true)),
				new KeyFrame(Duration.seconds(1), e -> text.setVisible(false)),
				new KeyFrame(Duration.seconds(2), e -> text.setVisible(true))
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	/**
	 * To apply writing styles.
	 *
	 * @param text Text to be edited.
	 * @param size Font size of text.
	 */
	public static void text(Text text, int size) {
		text.setFont(Font.font("Arial", FontWeight.BOLD, size * SCALE));
		text.setFill(Color.ORANGE);
		text.setTextAlignment(TextAlignment.CENTER);
	}

	/**
	 * Creates a text element displaying the current level
	 *
	 * @param level The current level.
	 * @return The text element displaying the level.
	 */
	static Text levelText(String level) {
		Text levelText = new Text("Level " + level + "/6");
		text(levelText, 10);
		levelText.setX(WIDTH * SCALE / 2.0 - levelText.getLayoutBounds().getWidth() / 2);
		levelText.setY(1 + levelText.getLayoutBounds().getHeight());
		return levelText;
	}

	/**
	 * Creates a text element displaying the current ammo count.
	 *
	 * @param ammo The current ammo count.
	 * @return The text element displaying the ammo count.
	 */
	static Text ammoText(int ammo) {
		Text ammoText = new Text("AMMO: " + ammo);
		text(ammoText, 10);
		ammoText.setX(WIDTH * SCALE - ammoText.getLayoutBounds().getWidth() - 1);
		ammoText.setY(1 + ammoText.getLayoutBounds().getHeight());
		return ammoText;
	}

	/**
	 * Creates a text element with the specified string and alignment.
	 *
	 * @param str       The string to be displayed.
	 * @param alignment The alignment of the text element. Possible values are "up", "down", or "down2".
	 * @return The text element with the specified string and alignment.
	 */
	public static Text createText(String str, String alignment) {
		Text returnText = new Text(str);
		text(returnText, 16);
		returnText.setX(WIDTH * SCALE / 2.0 - returnText.getLayoutBounds().getWidth() / 2);
		returnText.setVisible(false);

		switch (alignment) {
			case ("up"):
				returnText.setY((HEIGHT / 2.0) * SCALE - returnText.getLayoutBounds().getHeight() / 2);
				break;
			case ("down"):
				returnText.setY((HEIGHT / 2.0) * SCALE + returnText.getLayoutBounds().getHeight() / 2);
				break;
			case ("down2"):
				returnText.setY((HEIGHT / 2.0) * SCALE + returnText.getLayoutBounds().getHeight() / 4);
				break;
			default:
				break;
		}
		return returnText;
	}

	/**
	 * Executes the death animation for a duck.
	 *
	 * @param duckImageView     The ImageView representing the duck.
	 * @param animationTimeline The Timeline controlling the duck's animation.
	 * @param color             The color of the duck.
	 */
	static void dieAnimation(ImageView duckImageView, Timeline animationTimeline, String color) {
		animationTimeline.pause();
		duckImageView.setImage(new Image("/assets/duck_" + color + "/7.png"));
		Timeline waitTimeline = new Timeline(new KeyFrame(Duration.millis(600), e -> {
			duckImageView.setScaleY(1);
			duckImageView.setImage(new Image("/assets/duck_" + color + "/8.png"));
			TranslateTransition duckDieTransition = new TranslateTransition(Duration.millis(1000), duckImageView);
			duckDieTransition.setToY(((HEIGHT) * SCALE));
			duckDieTransition.play();
		}));
		duckFalls.play();
		duckFalls.seek(Duration.seconds(0));
		waitTimeline.play();
	}

	/**
	 * Handles the action of playing the game again based on the provided KeyEvent.
	 *
	 * @param eventKey The KeyEvent representing the key press event.
	 */
	static void playAgain(KeyEvent eventKey) {
		duckFalls.stop();
		gunshot.stop();
		gameCompleted.stop();
		levelCompleted.stop();
		if (eventKey.getCode() == KeyCode.ENTER) {
			level1Scene = new Scene(new Level1().pane, WIDTH * SCALE, HEIGHT * SCALE);
			primaryStage.setScene(level1Scene);
		} else if (eventKey.getCode() == KeyCode.ESCAPE) {
			Settings.background = 1;
			Settings.crosshair = 1;
			setCrosshair();
			entryScene = new Scene(new Entry().pane, WIDTH * SCALE, HEIGHT * SCALE);
			entrySceneKey(primaryStage);
			primaryStage.setScene(entryScene);
		}
	}

	/**
	 * Sets the crosshair image and updates the background and foreground images based on the current settings.
	 */
	private static void setCrosshair() {
		Settings.crosshairImage = new Image("/assets/crosshair/" + Settings.crosshair + ".png");
		Settings.crosshairImageView.setImage(Settings.crosshairImage);
		Settings.backgroundImage = new Image("/assets/background/" + Settings.background + ".png");
		Settings.backgroundImageView.setImage(Settings.backgroundImage);
		Settings.foregroundImage = new Image("/assets/foreground/" + Settings.background + ".png");
		Settings.foregroundImageView.setImage(Settings.foregroundImage);
	}

	/**
	 * Configures the mouse events for the pane.
	 *
	 * @param pane The pane.
	 */
	static void crosshair(Pane pane) {
		pane.setCursor(Cursor.NONE);

		pane.setOnMouseMoved(event -> {
			double mouseX = event.getX();
			double mouseY = event.getY();

			Settings.crosshairImageView.setX(mouseX - Settings.crosshairImageView.getFitWidth() / 2);
			Settings.crosshairImageView.setY(mouseY - Settings.crosshairImageView.getFitHeight() / 2);
		});

		pane.setOnMouseDragged(event -> {
			if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) {
				Settings.crosshairImageView.setX(event.getX() - Settings.crosshairImageView.getFitWidth() / 2);
				Settings.crosshairImageView.setY(event.getY() - Settings.crosshairImageView.getFitHeight() / 2);
			}
		});

		pane.setOnMouseExited(event -> Settings.crosshairImageView.setVisible(false));
		pane.setOnMouseEntered(event -> Settings.crosshairImageView.setVisible(true));
	}

	/**
	 * Configures the key events for the entry scene.
	 *
	 * @param primaryStage The stage.
	 */
	static void entrySceneKey(Stage primaryStage) {
		entryScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				settingScene = new Scene(new Settings().pane, WIDTH * SCALE, HEIGHT * SCALE);
				primaryStage.setScene(settingScene);
				eventController(settingScene);
			} else if (event.getCode() == KeyCode.ESCAPE) {
				primaryStage.close();
			}
		});
	}


	/**
	 * Handles the key events for the specified setting scene.
	 *
	 * @param settingScene The setting scene for which the key events are handled.
	 */
	public static void eventController(Scene settingScene) {
		settingScene.setOnKeyPressed(eventKey -> {
			if (eventKey.getCode() == KeyCode.ENTER) {
				title.stop();
				title.seek(Duration.seconds(0));
				intro.play();
				intro.setOnEndOfMedia(() -> {
					intro.stop();
					intro.seek(Duration.seconds(0));
					level1Scene = new Scene(new Level1().pane, WIDTH * SCALE, HEIGHT * SCALE);
					primaryStage.setScene(DuckHunt.level1Scene);
				});
			} else if (eventKey.getCode() == KeyCode.ESCAPE) {
				Settings.background = 1;
				Settings.crosshair = 1;
				primaryStage.setScene(entryScene);
			} else if (eventKey.getCode() == KeyCode.LEFT) {
				if (Settings.crosshair == 1) {
					Settings.crosshair = 7;
				} else {
					Settings.crosshair--;
				}
			} else if (eventKey.getCode() == KeyCode.RIGHT) {
				if (Settings.crosshair == 7) {
					Settings.crosshair = 1;
				} else {
					Settings.crosshair++;
				}
			} else if (eventKey.getCode() == KeyCode.UP) {
				if (Settings.background == 6) {
					Settings.background = 1;
				} else {
					Settings.background++;
				}
			} else if (eventKey.getCode() == KeyCode.DOWN) {
				if (Settings.background == 1) {
					Settings.background = 6;
				} else {
					Settings.background--;
				}
			}
			setCrosshair();
		});
	}
}