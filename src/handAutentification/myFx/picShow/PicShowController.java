package handAutentification.myFx.picShow;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import handAutentification.services.Utils;
import handAutentification.logik.domains.Hand;
import handAutentification.logik.HandDetector;
import handAutentification.logik.HandProcesser;
import handAutentification.logik.Params;

import static org.opencv.imgproc.Imgproc.*;

public class PicShowController {
    @FXML
    private ImageView imageViewLeft;
    @FXML
    private ImageView imageViewRight;
    @FXML
    private Slider threshSlider;

    private Mat original;
    private Params params;

    @FXML
    void initialize() {
        params = Params.getInstance();

        threshSlider.valueChangingProperty().addListener((observableValue, wasChanging, changing) -> {
            if (wasChanging) {
                processPhoto();
            }
        });
    }

    private void processPhoto(){
        findFingers();
    }


    public void initMethod(Mat frame) {
        int rectWidth = params.getSearchRectWidth();
        int rectHeight = params.getSearchRectHeight();
        int startX = frame.width() / 2 - rectWidth / 2;
        int startY = frame.height() / 2 - rectHeight / 2;
        Mat submat = frame.submat(startY, startY + rectHeight,
                startX, startX + rectWidth);
        this.original = submat;
        processPhoto();
    }

    private void findFingers() {
        List<Hand> hands = new ArrayList<>();
        HandDetector handDetector = new HandDetector();
        Mat tmp = new Mat();
        Mat colorImage = new Mat();

        threshold(original, tmp, threshSlider.getValue(), 255, THRESH_BINARY);
        cvtColor(original, colorImage, COLOR_GRAY2BGR);

        hands = handDetector.detect(tmp, hands);
        if (!hands.isEmpty()) {
            tmp = handDetector.drawHands(tmp, hands);
            colorImage = handDetector.drawHandsColor(colorImage, hands);

            HandProcesser handProcesser = new HandProcesser(hands.get(0), tmp);
            colorImage = handProcesser.init(colorImage);
        }
        Image image = Utils.mat2Image(tmp);
        imageViewLeft.setImage(image);

        image = Utils.mat2Image(colorImage);
        imageViewRight.setImage(image);
    }
}
