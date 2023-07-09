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

public class Level3 extends Level {
	private static final double SPEED = 16 * SCALE;
	private int ammo = 3, frame = 1;
	private int directionX = 1, directionY = -1;
	private double duckX, duckY;

	public Level3() {
		Text ammoText = GameGUI.ammoText(ammo);

		ImageView duckImageView = setDuckImageView();
		duckX = 80 * SCALE;
		duckY = 60 * SCALE;

		Timeline animationTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame = (frame + 1) % 3 + 1;

					duckImageView.setImage(new Image("/assets/duck_black/" + frame + ".png"));

					duckX += SPEED * directionX;
					duckY += SPEED * directionY;

					duckImageView.setTranslateX(duckX);
					duckImageView.setTranslateY(duckY);

					if (duckX <= 0 || duckX >= WIDTH * SCALE - duckImageView.getFitWidth()) {
						directionX *= -1;
						duckImageView.setScaleX(directionX);
						duckX = Math.max(0, duckX);
						duckX = Math.min(duckX, WIDTH * SCALE - duckImageView.getLayoutBounds().getWidth());
					}
					if (duckY <= 0 || duckY >= HEIGHT * SCALE - duckImageView.getFitHeight()) {
						duckImageView.setScaleY(directionY);
						directionY *= -1;
						duckY = Math.max(0, duckY);
						duckY = Math.min(duckY, HEIGHT * SCALE - duckImageView.getLayoutBounds().getHeight());
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
					GameGUI.dieAnimation(duckImageView, animationTimeline, "black");

					if (ammo >= 0) {
						winAnimation();
						level3Scene.setOnKeyPressed(eventEnter -> {
							if (eventEnter.getCode() == KeyCode.ENTER) {
								duckFalls.stop();
								gunshot.stop();
								levelCompleted.stop();
								level4Scene = new Scene(new Level4().pane, WIDTH * SCALE, HEIGHT * SCALE);
								primaryStage.setScene(level4Scene);
							}
						});
					}
				} else if (ammo == 0) {
					gameOverAnimation();
					level3Scene.setOnKeyPressed(GameGUI::playAgain);
				}
			}
		});

		pane.getChildren().addAll(Settings.backgroundImageView, duckImageView, Settings.foregroundImageView, levelText, ammoText, winText, winText2, gameOverText, gameOverText2, Settings.crosshairImageView);
	}
}