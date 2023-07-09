import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Level extends DuckHunt {
	Pane pane = new Pane();
	Text winText = GameGUI.createText("YOU WIN!", "up");
	Text winText2 = GameGUI.createText("Press ENTER to play next level", "down");
	Text gameOverText = GameGUI.createText("GAME OVER!", "up");
	Text gameOverText2 = GameGUI.createText("Press ENTER to play next again\nPress ESC to exit", "down2");
	Text endGameText = GameGUI.createText("You have completed the game!", "up");
	Text endGameText2 = GameGUI.createText("Press ENTER to play again\nPress ESC to exit", "down2");
	Text levelText = GameGUI.levelText(Character.toString(this.getClass().getSimpleName().charAt(5)));
	Image duckImage = new Image("assets/duck_black/1.png");

	public Level() {
		GameGUI.crosshair(pane);
	}

	/**
	 * Creates and configures an ImageView for a duck.
	 *
	 * @return The ImageView for the duck.
	 */
	public ImageView setDuckImageView() {
		ImageView duckImageView = new ImageView();
		duckImageView.setFitWidth(duckImage.getWidth() * SCALE);
		duckImageView.setFitHeight(duckImage.getHeight() * SCALE);
		return duckImageView;
	}

	/**
	 * Performs the win animation.
	 */
	public void winAnimation() {
		winText.setVisible(true);
		GameGUI.animation(winText2);
		levelCompleted.play();
		levelCompleted.seek(Duration.seconds(0));
	}

	/**
	 * Performs the game over animation.
	 */
	public void gameOverAnimation() {
		gameOverText.setVisible(true);
		GameGUI.animation(gameOverText2);
		gameOver.play();
		gameOver.seek(Duration.seconds(0));
	}
}

