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

public class Level4 extends Level {
	private static final double SPEED = 18 * SCALE;
	private int ammo = 6, frame = 1, frame2 = 1;
	private int directionX = 1, direction2X = 1, directionY = -1, direction2Y = -1;
	private double duckX, duckY, duck2X, duck2Y, duckCount = 2;

	public Level4() {
		Text ammoText = GameGUI.ammoText(ammo);

		ImageView duckImageView = setDuckImageView();
		duckX = 0;
		duckY = 20 * SCALE;

		ImageView duck2ImageView = setDuckImageView();
		duck2X = 120 * SCALE;
		duck2Y = HEIGHT;

		Timeline animationTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame = (frame + 1) % 3 + 1;

					duckImageView.setImage(new Image("/assets/duck_red/" + frame + ".png"));

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

		Timeline animation2Timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.2), event -> {
					frame2 = (frame2 + 1) % 3 + 1;

					duck2ImageView.setImage(new Image("/assets/duck_blue/" + frame2 + ".png"));

					duck2X += SPEED * direction2X;
					duck2Y += SPEED * direction2Y;

					duck2ImageView.setTranslateX(duck2X);
					duck2ImageView.setTranslateY(duck2Y);

					if (duck2X <= 0 || duck2X >= WIDTH * SCALE - duck2ImageView.getFitWidth()) {
						direction2X *= -1;
						duck2ImageView.setScaleX(direction2X);
						duck2X = Math.max(0, duck2X);
						duck2X = Math.min(duck2X, WIDTH * SCALE - duckImageView.getLayoutBounds().getWidth());
					}
					if (duck2Y <= 0 || duck2Y >= HEIGHT * SCALE - duck2ImageView.getFitHeight()) {
						duck2ImageView.setScaleY(direction2Y);
						direction2Y *= -1;
						duck2Y = Math.max(0, duck2Y);
						duck2Y = Math.min(duck2Y, HEIGHT * SCALE - duck2ImageView.getLayoutBounds().getHeight());
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
					GameGUI.dieAnimation(duckImageView, animationTimeline, "red");
					duckCount--;
				}

				if (duck2ImageView.getBoundsInParent().contains(event.getX(), event.getY()) && animation2Timeline.getStatus() == Animation.Status.RUNNING) {
					GameGUI.dieAnimation(duck2ImageView, animation2Timeline, "blue");
					duckCount--;
				}

				if (duckCount == 0) {
					winAnimation();
					level4Scene.setOnKeyPressed(eventEnter -> {
						if (eventEnter.getCode() == KeyCode.ENTER) {
							duckFalls.stop();
							gunshot.stop();
							levelCompleted.stop();
							level5Scene = new Scene(new Level5().pane, WIDTH * SCALE, HEIGHT * SCALE);
							primaryStage.setScene(level5Scene);
						}
					});
				} else if (ammo == 0) {
					gameOverAnimation();
					level4Scene.setOnKeyPressed(GameGUI::playAgain);
				}
			}
		});

		pane.getChildren().addAll(Settings.backgroundImageView, duckImageView, duck2ImageView, Settings.foregroundImageView, levelText, ammoText, winText, winText2, gameOverText, gameOverText2, Settings.crosshairImageView);
	}
}