package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imageView;

    @FXML
    private Slider slider1;

    @FXML
    private Slider slider2;

    @FXML
    private Button updateButton;

    @FXML
    public void onClickMethod() {
        updateButton.setText("Thanks!");
    }

    @FXML
    void initialize() {
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'sample.fxml'.";
        assert slider1 != null : "fx:id=\"slider1\" was not injected: check your FXML file 'sample.fxml'.";
        assert slider2 != null : "fx:id=\"slider2\" was not injected: check your FXML file 'sample.fxml'.";

    }

}