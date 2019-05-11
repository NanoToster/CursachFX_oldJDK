package sample.logik;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import sample.git.Utils;

import java.util.List;

import static org.opencv.imgproc.Imgproc.line;

public class HandProcesser {
    private Hand hand;
    private Params params;
    private Mat thrImg;

    public HandProcesser(Hand hand, Mat thrImg) {
        this.hand = hand;
        this.params = Params.getInstance();
        this.thrImg = thrImg;
    }

    public Mat init(Mat colorImg) {
        Point mediumFinger = findMediumFinger();
        int mediumFingerHeight = findVerticalFingerHeight(mediumFinger);
        return drawFoundedHeight(colorImg, mediumFinger, mediumFingerHeight);
    }

    private Point findMediumFinger() {
        List<Point> fingers = hand.getFingers();
        Point mediumFinger = fingers.get(0);
        for (Point point : fingers) {
            if (point.y < mediumFinger.y) {
                mediumFinger = point;
            }
        }
        return mediumFinger;
    }

    // Новая идея! Идти вниз от точки пальца,
    // и искать центральные белые точки между двумя черными
    // областями

    private int findVerticalFingerHeight(Point fingerStart) {
        Point fingerTmp = new Point(fingerStart.x, fingerStart.y);
        int mediumWidth = 0;
        int height = 0;
        int step = 10;
        int stepsCount = 0;

        for (; ; ) {
            stepsCount++;
            fingerTmp.y = fingerTmp.y + step;

            int center = findFingerCenter(fingerTmp, fingerStart);
            if (center == -1) {
                return step * stepsCount;
            } else {
                fingerTmp.x = center;
            }
        }
    }

    private int findFingerCenter(Point whereCheck, Point fingerStart) {
        // если полуширина больше чем расстояние до ближайшего пальца по оси Х
        int rightWidth = 1;
        int leftWidth = 1;
        for(; rightWidth < params.getSearchRectWidth() - whereCheck.x; rightWidth++){
            if (thrImg.get((int)whereCheck.y, (int)(whereCheck.x + rightWidth))[0] == 0){
                break;
            }
        }
        for(; leftWidth < whereCheck.x; leftWidth++){
            if (thrImg.get((int)whereCheck.y, (int)(whereCheck.x - leftWidth))[0] == 0){
                break;
            }
        }
        int nearestFingerRange = findNearestFingerRange(fingerStart);

        if (leftWidth > nearestFingerRange || rightWidth > nearestFingerRange) {
            return -1; //if finger ends
        }

        return (int)(whereCheck.x + ((rightWidth - leftWidth) / 2));
    }

    private int findNearestFingerRange(Point startFinger){
        List<Point> handFingers = hand.getFingers();
        int minRange = 1000;
        for (Point point:handFingers){
            int range = (int)Math.abs(startFinger.x - point.x);
            if (range != 0 && range < minRange){
                minRange = range;
            }
        }
        return minRange;
    }

    private Mat drawFoundedHeight(Mat colorImg, Point finger, int fingerHeight) {
        line(colorImg, new Point(finger.x, finger.y + fingerHeight),
                new Point(finger.x + 10, finger.y + fingerHeight), new Scalar(255, 0, 0), 4);
        return colorImg;
    }
}
