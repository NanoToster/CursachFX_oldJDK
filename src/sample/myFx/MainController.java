package sample.myFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.opencv.bgsegm.BackgroundSubtractorMOG;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.Videoio;
import sample.git.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import sample.logik.Hand;
import sample.logik.HandDetector;
import sample.logik.Params;

import javax.swing.text.html.HTMLDocument;

import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2YCrCb;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GRAY2BGR;
import static org.opencv.bgsegm.Bgsegm.createBackgroundSubtractorMOG;
import static org.opencv.core.CvType.*;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.video.Video.createBackgroundSubtractorMOG2;
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

    @FXML
    private BorderPane borderPane;

    private ScheduledExecutorService timer;
    private VideoCapture capture = new VideoCapture(CV_CAP_OPENNI);
    private int width = 640, height = 480;
    private boolean cameraActive = false;
    private static int cameraId = 0;

    private HandDetector handDetector = new HandDetector();
    private List<Hand> hands = new ArrayList<>();
    private Params params = new Params();


    @FXML
    void initialize() {
        treshold.setValue(50);
        thresh.setValue(50);

        capture.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, width);
        capture.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, height);

    }

    @FXML
    protected void startCamera(ActionEvent event) {
        if (!this.cameraActive) {
            this.capture.open(cameraId);

            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        Mat frame = grabFrame();
                        Image imageToShow = Utils.mat2Image(frame, params);
                        updateImageView(currentFrame, imageToShow);
                    }
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
                if (params.getRectSearchAreaX() == 0) {
                    params.setWhereSearchX(frame.width() / 2);
                    params.setWhereSearchY(frame.height() / 3);
                }
                Mat depthMap = new Mat();

                this.capture.retrieve(depthMap, CV_16UC1);

                cvtColor(frame, frame, COLOR_RGB2BGR);
                depthMap = frame.clone();
                cvtColor(depthMap, depthMap, COLOR_BGR2GRAY);
//                threshold(depthMap, depthMap, this.thresh.getValue(), this.treshold.getValue(), THRESH_BINARY);
                blur(depthMap, depthMap, new Size(3, 3));
                Canny(depthMap, depthMap, this.treshold.getValue(), 2 * this.treshold.getValue(), 3);

//                hands = handDetector.detect(depthMap, hands);
                hands = handDetector.detect(depthMap, hands, params);

                if (!hands.isEmpty()) {
                    depthMap = handDetector.drawHands(depthMap, hands);
                }

                return depthMap;

            } catch (Exception e) {
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
//        frame = Utils.drawRect(frame, whereSearchX, whereSearchY);
//        rectangle(frame,
//                new Rect(whereSearchX, whereSearchY,
//                        40,
//                        60), new Scalar(255,255,255), 10, 8, 0);
        return frame;
    }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
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
