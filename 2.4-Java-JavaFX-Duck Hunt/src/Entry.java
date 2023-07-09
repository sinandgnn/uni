import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

public class Entry extends DuckHunt {
	Pane pane = new Pane();
	Text welcomeText = new Text("PRESS ENTER TO START\nPRESS ESC TO EXIT");
	Image backgroundImage = new Image("/assets/welcome/1.png");
	ImageView backgroundImageView = new ImageView(backgroundImage);

	public Entry() {
		title.play();
		title.setCycleCount(MediaPlayer.INDEFINITE);
		GameGUI.text(welcomeText, 16);
		welcomeText.setX(WIDTH * SCALE / 2.0 - welcomeText.getLayoutBounds().getWidth() / 2);
		welcomeText.setY((HEIGHT - 75) * SCALE);

		backgroundImageView.setFitWidth(WIDTH * SCALE);
		backgroundImageView.setFitHeight(HEIGHT * SCALE);

		GameGUI.animation(welcomeText);

		pane.getChildren().addAll(backgroundImageView, welcomeText);
	}
}
