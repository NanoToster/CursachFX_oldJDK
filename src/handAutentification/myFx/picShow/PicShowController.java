package handAutentification.myFx.picShow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import handAutentification.logik.DAO.DAO;
import handAutentification.logik.domains.BiometricalData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
    @FXML
    private Button addToDbButton;
    @FXML
    private TextArea infoArea;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;

    private Mat original;
    private Params params;
    private BiometricalData biometricalData = null;

    @FXML
    void initialize() {
        params = Params.getInstance();

        threshSlider.valueChangingProperty().addListener((observableValue, wasChanging, changing) -> {
            if (wasChanging) {
                processPhoto();
            }
        });
    }

    @FXML
    void addToDbAction(ActionEvent event) {
        if (!nameTextField.getText().isEmpty() && !surnameTextField.getText().isEmpty()) {
            int index = DAO.getInstance().addPersonToDB(nameTextField.getText(),
                    surnameTextField.getText(), biometricalData);
        }
    }

    private void processPhoto() {
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
            biometricalData = handProcesser.getBiometricalData();
            if (biometricalData != null) {
                biometricalProcess(biometricalData);
            }
        }
        Image image = Utils.mat2Image(tmp);
        imageViewLeft.setImage(image);

        image = Utils.mat2Image(colorImage);
        imageViewRight.setImage(image);
    }

    private void biometricalProcess(BiometricalData biometricalData) {
        infoArea.setText(biometricalData.toString());
        DAO db = DAO.getInstance();
        infoArea.setText(db.findPerson(biometricalData));
    }
}
