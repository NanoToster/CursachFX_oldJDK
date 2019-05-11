package sample.myFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.opencv.videoio.Videoio;
import sample.git.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import sample.logik.Hand;
import sample.logik.HandDetector;
import sample.logik.Params;
import sample.myFx.picShow.PicShowController;

import static org.opencv.core.CvType.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.videoio.Videoio.CV_CAP_OPENNI;

public class MainController {
    @FXML
    private Button button;

    @FXML
    private ImageView currentFrame;

    @FXML
    private Slider treshold;

    @FXML
    private Slider thresh;

    private ScheduledExecutorService timer;
    private VideoCapture capture = new VideoCapture(CV_CAP_OPENNI);
    private int width = 640, height = 480;
    private boolean cameraActive = false;
    private static int cameraId = 0;

    @FXML
    void initialize() {
        treshold.setValue(50);
        thresh.setValue(50);

        capture.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, width);
        capture.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, height);

        button.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.SPACE)) {
                Mat frame = grabFrame();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "picShow/picShow.fxml"));
                    Parent parent = loader.load();

                    PicShowController controller = loader.getController();
                    controller.initMethod(frame);

                    Stage stage = new Stage();
                    stage.setTitle("Hand image");
                    stage.setScene(new Scene(parent, 1000, 700));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
//        button.fire();
    }

    @FXML
    protected void startCamera(ActionEvent event) {
        if (!this.cameraActive) {
            this.capture.open(cameraId);
            if (this.capture.isOpened()) {
                this.cameraActive = true;
                Runnable frameGrabber = () -> {
                    Mat frame = grabFrame();
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(currentFrame, imageToShow);
                };
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                this.button.setText("Stop Camera");
            } else {
                System.err.println("Impossible to open the camera connection...");
            }
        } else {
            this.cameraActive = false;
            this.button.setText("Start Camera");
            this.stopAcquisition();
        }
    }

    private Mat grabFrame() {
        Mat frame = new Mat();

        if (this.capture.isOpened()) {
            try {
                this.capture.read(frame);
                Mat depthMap = new Mat();

                this.capture.retrieve(depthMap, CV_16UC1);

                cvtColor(frame, frame, COLOR_RGB2BGR);
                depthMap = frame.clone();
                cvtColor(depthMap, depthMap, COLOR_BGR2GRAY);
                return depthMap;

            } catch (Exception e) {
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;
    }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            this.capture.release();
        }
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    protected void setClosed() {
        this.stopAcquisition();
    }
}
