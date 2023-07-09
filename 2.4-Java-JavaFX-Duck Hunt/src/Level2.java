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

public class Level2 extends Level {
	private static final double SPEED = 14 * SCALE;
	private int ammo = 6, frame = 1, frame2 = 1;
	private int directionX = 1, direction2X = 1;
	private double duckX, duck2X, duckCount = 2;

	public Level2() {
		Text ammoText = GameGUI.ammoText(ammo);

		ImageView duckImageView = setDuckImageView();
		duckX = WIDTH * SCALE;
		duckImageView.setY(30 * SCALE);

		ImageView duck2ImageView = setDuckImageView();
		duck2X = 0;
		duck2ImageView.setY(80 * SCALE);

		Timeline animationTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame = (frame + 1) % 3 + 4;

					duckImageView.setImage(new Image("/assets/duck_black/" + frame + ".png"));

					duckX += SPEED * directionX;
					duckImageView.setTranslateX(duckX);

					if (duckX <= 0 || duckX >= WIDTH * SCALE - duckImageView.getLayoutBounds().getWidth()) {
						duckX = Math.max(0, duckX);
						duckX = Math.min(duckX, WIDTH * SCALE - duckImageView.getLayoutBounds().getWidth());
						directionX *= -1;
						duckImageView.setScaleX(directionX);
					}
				})
		);

		Timeline animation2Timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame2 = (frame2 + 1) % 3 + 4;

					duck2ImageView.setImage(new Image("/assets/duck_red/" + frame2 + ".png"));

					duck2X += SPEED * direction2X;
					duck2ImageView.setTranslateX(duck2X);

					if (duck2X <= 0 || duck2X >= WIDTH * SCALE - duck2ImageView.getFitWidth()) {
						duck2X = Math.max(0, duck2X);
						duck2X = Math.min(duck2X, WIDTH * SCALE - duck2ImageView.getLayoutBounds().getWidth());
						direction2X *= -1;
						duck2ImageView.setScaleX(direction2X);
					}
				})
		);

		animationTimeline.setCycleCount(Animation.INDEFINITE);
		animationTimeline.play();
		animation2Timeline.setCycleCount(Animation.INDEFINITE);
		animation2Timeline.play();

		pane.setOnMouseClicked(event -> {
			if (duckCount != 0 && ammo != 0 && event.getButton() == MouseButton.PRIMARY) {
				gunshot.play();
				gunshot.seek(Duration.seconds(0));
				ammo--;
				ammoText.setText("AMMO: " + ammo);

				if (duckImageView.getBoundsInParent().contains(event.getX(), event.getY()) && animationTimeline.getStatus() == Animation.Status.RUNNING) {
					GameGUI.dieAnimation(duckImageView, animationTimeline, "black");
					duckCount--;
				}

				if (duck2ImageView.getBoundsInParent().contains(event.getX(), event.getY()) && animation2Timeline.getStatus() == Animation.Status.RUNNING) {
					GameGUI.dieAnimation(duck2ImageView, animation2Timeline, "red");
					duckCount--;
				}

				if (duckCount == 0) {
					winAnimation();

					level2Scene.setOnKeyPressed(eventEnter -> {
						if (eventEnter.getCode() == KeyCode.ENTER) {
							duckFalls.stop();
							gunshot.stop();
							levelCompleted.stop();
							level3Scene = new Scene(new Level3().pane, WIDTH * SCALE, HEIGHT * SCALE);
							primaryStage.setScene(level3Scene);
						}
					});
				} else if (ammo == 0) {
					gameOverAnimation();
					level2Scene.setOnKeyPressed(GameGUI::playAgain);
				}


			}
		});

		pane.getChildren().addAll(Settings.backgroundImageView, duckImageView, duck2ImageView, Settings.foregroundImageView, levelText, ammoText, winText, winText2, gameOverText, gameOverText2, Settings.crosshairImageView);
	}
}