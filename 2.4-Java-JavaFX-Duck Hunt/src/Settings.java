import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Settings extends DuckHunt {
	static int crosshair = 1, background = 1;
	static Image crosshairImage = new Image("/assets/crosshair/" + crosshair + ".png");
	static ImageView crosshairImageView = new ImageView(crosshairImage);
	static Image backgroundImage = new Image("/assets/background/" + background + ".png");
	static Image foregroundImage = new Image("/assets/foreground/" + background + ".png");
	static ImageView backgroundImageView = new ImageView(backgroundImage);
	static ImageView foregroundImageView = new ImageView(foregroundImage);
	Pane pane = new Pane();

	public Settings() {
		Text navigateText = new Text("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");

		backgroundImageView.setFitWidth(WIDTH * SCALE);
		backgroundImageView.setFitHeight(HEIGHT * SCALE);

		foregroundImageView.setFitWidth(WIDTH * SCALE);
		foregroundImageView.setFitHeight(HEIGHT * SCALE);

		GameGUI.text(navigateText, 12);
		navigateText.setX(WIDTH * SCALE / 2.0 - navigateText.getLayoutBounds().getWidth() / 2);
		navigateText.setY(navigateText.getLayoutBounds().getHeight() / 2);

		crosshairImageView.setVisible(true);
		crosshairImageView.setX((WIDTH / 2.0 - crosshairImage.getWidth() / 2) * SCALE);
		crosshairImageView.setY((HEIGHT / 2.0 - crosshairImage.getHeight() / 2) * SCALE);

		pane.getChildren().addAll(backgroundImageView, navigateText, foregroundImageView, crosshairImageView);
	}
}
