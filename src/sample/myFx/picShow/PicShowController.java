package sample.myFx.picShow;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import sample.git.Utils;
import sample.logik.Hand;
import sample.logik.HandDetector;
import sample.logik.HandProcesser;
import sample.logik.Params;

import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
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
//        findFuckingContours();
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

    private void findFuckingContours() {
        Mat tmp = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        threshold(original, tmp, threshSlider.getValue(), 255, THRESH_BINARY);

        findContours(tmp, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE, new Point(0, 0));
        for( int i=0; i<contours.size();i++) {
            Point[] pntContour = contours.get(i).toArray();

            for (int j = 0; j < pntContour.length; j ++) {
                circle(tmp, pntContour[j], 5, new Scalar(128, 128, 128), 2);
            }
        }
        Image image = Utils.mat2Image(tmp);
        imageViewLeft.setImage(image);
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
