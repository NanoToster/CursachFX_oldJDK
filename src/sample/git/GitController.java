//package sample.git;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import javafx.event.EventHandler;
//import javafx.scene.control.Slider;
//import javafx.scene.image.PixelReader;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.BorderPane;
//import org.bytedeco.javacpp.opencv_core;
//import org.opencv.core.*;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.videoio.VideoCapture;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//import static org.opencv.core.CvType.CV_16UC1;
//import static org.opencv.core.CvType.CV_32FC1;
//
//public class GitController {
//    // the FXML button
//    @FXML
//    private Button button;
//    // the FXML image view
//    @FXML
//    private ImageView currentFrame;
//
//    @FXML
//    private Slider treshold;
//
//    @FXML
//    private BorderPane borderPane;
//
//    // a timer for acquiring the video stream
//    private ScheduledExecutorService timer;
//    // the OpenCV object that realizes the video capture
//    private VideoCapture capture = new VideoCapture();
//    // a flag to change the button behavior
//    private boolean cameraActive = false;
//    // the id of the camera to be used
//    private static int cameraId = 0;
//
//
////    @FXML
////    protected void startCamera(ActionEvent event) {
////        if (!this.cameraActive) {
////            // start the video capture
////            this.capture.open(cameraId);
////
////            // is the video stream available?
////            if (this.capture.isOpened()) {
////                this.cameraActive = true;
////
////                // grab a frame every 33 ms (30 frames/sec)
////                Runnable frameGrabber = new Runnable() {
////
////                    @Override
////                    public void run() {
//////                         effectively grab and process a single frame
////                        Mat frame = grabFrame();
//////                         convert and show the frame
////                        Image imageToShow = Utils.mat2Image(frame);
//////                        experiments(frame, imageToShow);
////                        updateImageView(currentFrame, imageToShow);
////                    }
////                };
////
////                this.timer = Executors.newSingleThreadScheduledExecutor();
////                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
////
////                // update the button content
////                this.button.setText("Stop Camera");
////            } else {
////                // log the error
////                System.err.println("Impossible to open the camera connection...");
////            }
////        } else {
////            // the camera is not active at this point
////            this.cameraActive = false;
////            // update again the button content
////            this.button.setText("Start Camera");
////
////            // stop the timer
////            this.stopAcquisition();
////        }
////    }
//
//    @FXML
//    protected void startCamera(ActionEvent event) {
//        if (!this.cameraActive) {
//            this.capture.open(cameraId);
//
//            if (this.capture.isOpened()) {
//                this.cameraActive = true;
//
//                // grab a frame every 33 ms (30 frames/sec)
//                Runnable frameGrabber = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Mat frame = grabFrame();
//
//                        Image imageToShow = Utils.mat2Image(frame);
//                        updateImageView(currentFrame, imageToShow);
//                    }
//                };
//
//                this.timer = Executors.newSingleThreadScheduledExecutor();
//                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
//
//                this.button.setText("Stop Camera");
//            } else {
//                System.err.println("Impossible to open the camera connection...");
//            }
//        } else {
//            this.cameraActive = false;
//            this.button.setText("Start Camera");
//            this.stopAcquisition();
//        }
//    }
//
//    @FXML
//    void initialize() {
//
////        treshold.setOnKeyPressed(new EventHandler<KeyEvent>() {
////            @Override
////            public void handle(KeyEvent event) {
////                if (event.getCode() == KeyCode.SPACE) {
////                    System.out.println("Space");
////                    setClosed();
////                    Mat frame = detectCircles();
////                    Image imageToShow = Utils.mat2Image(frame);
////                    updateImageView(currentFrame, imageToShow);
////
////                }
////            }
////        });
//
//    }
//
//    private Mat detectCircles() {
//        Mat frame = grabFrame();
//        Mat houghCircles = new Mat();
//
////        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
//        Mat circles = new Mat();
////        Imgproc.blur(frame, frame, new Size(3, 3));
////        Imgproc.Canny(frame, frame, this.treshold.getValue(), this.treshold.getValue(), 3);
//
//        Imgproc.HoughCircles(frame, circles, Imgproc.CV_HOUGH_GRADIENT, 1, frame.rows() / 15);//, grayMat.rows() / 8);
//
//        houghCircles.create(frame.rows(), frame.cols(), CvType.CV_8UC1);
//        for (int i = 0; i < circles.cols(); i++) {
//            double[] parameters = circles.get(0, i);
//            double x, y;
//            int r;
//
//            x = parameters[0];
//            y = parameters[1];
//            r = (int) parameters[2];
//
//            Point center = new Point(x, y);
//
//            //Drawing circles on an image
//            Imgproc.circle(houghCircles, center, r, new Scalar(255, 0, 0), 1);
//        }
//        return houghCircles;
//    }
//
//    private void experiments(Mat frame, Image imageToShow) {
//        System.out.println("Channels: " + frame.channels());
//        double[] doubles = frame.get(5, 5);
//        for (Double d : doubles) {
//            System.out.println(d);
//        }
//        System.out.println("Array: " + doubles);
//        PixelReader pixelReader = imageToShow.getPixelReader();
//        System.out.println("Argb: " + pixelReader.getArgb(5, 5));
//        System.out.println("Color: " + pixelReader.getColor(5, 5).toString());
//
//
//    }
//
//    /**
//     * Get a frame from the opened video stream (if any)
//     *
//     * @return the {@link Mat} to show
//     */
//    private Mat grabFrame() {
//        // init everything
//        Mat frame = new Mat();
//        // check if the capture is open
//        if (this.capture.isOpened()) {
//            try {
//                // read the current frame
//                this.capture.read(frame);
//
//                // if the frame is not empty, process it
//                if (!frame.empty()) {
//                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
//                    Imgproc.blur(frame, frame, new Size(3, 3));
//                    Imgproc.Canny(frame, frame, this.treshold.getValue(), this.treshold.getValue(), 3);
//                }
//
//            } catch (Exception e) {
//                // log the error
//                System.err.println("Exception during the image elaboration: " + e);
//            }
//        }
//
//        return frame;
//    }
//
//    /**
//     * Stop the acquisition from the camera and release all the resources
//     */
//    private void stopAcquisition() {
//        if (this.timer != null && !this.timer.isShutdown()) {
//            try {
//                // stop the timer
//                this.timer.shutdown();
//                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
//            } catch (InterruptedException e) {
//                // log any exception
//                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
//            }
//        }
//
//        if (this.capture.isOpened()) {
//            // release the camera
//            this.capture.release();
//        }
//    }
//
//    private void updateImageView(ImageView view, Image image) {
//        Utils.onFXThread(view.imageProperty(), image);
//    }
//
//    protected void setClosed() {
//        this.stopAcquisition();
//    }
//}
