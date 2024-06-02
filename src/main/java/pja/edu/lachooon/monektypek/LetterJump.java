package pja.edu.lachooon.monektypek;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.animation.RotateTransition;

public class LetterJump {
    private final Text text;
    private final Timeline timeline;

    public LetterJump(Text text) {
        this.text = text;

        timeline = new Timeline();
        timeline.setCycleCount(1);
    }

    public void waveAnimation() {
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));

        KeyValue startKeyValue = new KeyValue(text.translateYProperty(), 0.0);
        KeyFrame startFrame = new KeyFrame(Duration.ZERO, startKeyValue);

        KeyValue middleKeyValue = new KeyValue(text.translateYProperty(), -10.0);
        KeyFrame middleFrame = new KeyFrame(Duration.seconds(0.5), middleKeyValue);

        KeyValue endKeyValue = new KeyValue(text.translateYProperty(), 0.0);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1), endKeyValue);

        timeline.getKeyFrames().addAll(startFrame, middleFrame, endFrame);
        timeline.play();
    }

    public void errorAnimation() {
        RotateTransition transition = new RotateTransition(Duration.seconds(0.25), text);
        transition.setByAngle(360);

        transition.play();
    }
}
