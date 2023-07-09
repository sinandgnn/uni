import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Level1 extends Level {
	private static final double SPEED = 12 * SCALE;
	private int ammo = 3, frame = 1;
	private double direction = 1;
	private double duckX;

	public Level1() {
		Text ammoText = GameGUI.ammoText(ammo);

		ImageView duckImageView = setDuckImageView();
		duckX = 0;
		duckImageView.setY(30 * SCALE);

		Timeline animationTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame = (frame + 1) % 3 + 4;

					duckImageView.setImage(new Image("/assets/duck_blue/" + frame + ".png"));

					duckX += SPEED * direction;
					duckImageView.setTranslateX(duckX);

					if (duckX <= 0 || duckX >= WIDTH * SCALE - duckImageView.getLayoutBounds().getWidth()) {
						direction *= -1;
						duckImageView.setScaleX(direction);
					}
				})
		);
		animationTimeline.setCycleCount(Animation.INDEFINITE);
		animationTimeline.play();

		pane.setOnMouseClicked(event -> {
			if (ammo != 0 && event.getButton() == MouseButton.PRIMARY && animationTimeline.getStatus() == Animation.Status.RUNNING) {
				gunshot.play();
				gunshot.seek(Duration.seconds(0));
				ammo--;
				ammoText.setText("AMMO: " + ammo);

				if (duckImageView.getBoundsInParent().contains(event.getX(), event.getY())) {
					GameGUI.dieAnimation(duckImageView, animationTimeline, "blue");

					if (ammo >= 0) {
						winAnimation();
						level1Scene.setOnKeyPressed(eventEnter -> {
							if (eventEnter.getCode() == KeyCode.ENTER) {
								duckFalls.stop();
								gunshot.stop();
								levelCompleted.stop();
								level2Scene = new Scene(new Level2().pane, WIDTH * SCALE, HEIGHT * SCALE);
								primaryStage.setScene(level2Scene);
							}
						});
					}
				} else if (ammo == 0) {
					gameOverAnimation();
					level1Scene.setOnKeyPressed(GameGUI::playAgain);
				}
			}
		});

		pane.getChildren().addAll(Settings.backgroundImageView, duckImageView, Settings.foregroundImageView, levelText, ammoText, winText, winText2, gameOverText, gameOverText2, Settings.crosshairImageView);
	}
}