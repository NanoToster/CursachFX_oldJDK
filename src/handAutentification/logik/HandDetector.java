package handAutentification.logik;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Moments;
import handAutentification.logik.domains.Hand;

import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.*;


public class HandDetector {
    Params param;

    public HandDetector() {
        param = Params.getInstance();
    }

    public List<Hand> detect(Mat depthMap, List<Hand> hands) {
        hands.clear();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        //CV_CHAIN_APPROX_NONE //CHAIN_APPROX_TC89_KCOS
        findContours(depthMap.clone(), contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE, new Point(0, 0));

        if (!contours.isEmpty()) {
            for (int i = 0; i < contours.size(); i++) {
                if (contourArea(contours.get(i)) > param.getArea()) {
                    Hand tmp = new Hand();
                    Moments m = moments(contours.get(i));
                    tmp.setCenter(new Point(m.m10 / m.m00, m.m01 / m.m00));
                    Point[] pntContour = contours.get(i).toArray();

                    for (int j = 0; j < pntContour.length; j += param.getStep()) {
                        circle(depthMap, pntContour[j], 5, new Scalar(128, 128, 128), 2);

                        double cos0 = angle(pntContour, j, param.getR());

                        if ((cos0 > 0.5) && (j + param.getStep() < pntContour.length)) {
                            double cos1 = angle(pntContour, j - param.getStep(), param.getR());
                            double cos2 = angle(pntContour, j + param.getStep(), param.getR());
                            double maxCos = Math.max(Math.max(cos0, cos1), cos2);
                            boolean equal = isEqual(maxCos, cos0);
                            double z = rotation(pntContour, j, param.getR());
                            if (equal && z < 0) {
                                tmp.getFingers().add(pntContour[j]);
                            } else if (equal && z > 0){ // елсе иф полностью самодеятельность
                                tmp.getPereponki().add(pntContour[j]);
                            }
                        }
                    }
                    tmp.setContour(contours.get(i).toList());
                    hands.add(tmp);
                }
            }
        }
        return hands;
    }

    private boolean isEqual(double a, double b) {
        return Math.abs(a - b) <= param.getEqualThreshold();
    }

    private double rotation(Point[] pntContour, int pt, int r) {
        int size = pntContour.length;
        Point p0 = (pt > 0) ? pntContour[pt % size] : pntContour[size - 1 + pt];
        Point p1 = pntContour[(pt + r) % size];
        Point p2 = (pt > r) ? pntContour[pt - r] : pntContour[size - 1 - r];

        double ux = p0.x - p1.x;
        double uy = p0.y - p1.y;
        double vx = p0.x - p2.x;
        double vy = p0.y - p2.y;
        return (ux * vy - vx * uy);
    }

    private double angle(Point[] pntContour, int pt, int r) {
        int size = pntContour.length;
        Point p0 = (pt > 0) ? pntContour[pt % size] : pntContour[size - 1 + pt];
        Point p1 = pntContour[(pt + r) % size];
        Point p2 = (pt > r) ? pntContour[pt - r] : pntContour[size - 1 - r];

        double ux = p0.x - p1.x;
        double uy = p0.y - p1.y;
        double vx = p0.x - p2.x;
        double vy = p0.y - p2.y;
        return (ux * vx + uy * vy) / Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
    }

    public Mat drawHands(Mat image, List<Hand> hands) {
        int size = hands.size();
        for (int i = 0; i < size; i++) {
            int fingersSize = hands.get(i).getFingers().size();
            circle(image, hands.get(i).getCenter(), 20, new Scalar(128, 128, 128), 2);

            for (int j = 0; j < fingersSize; j++) {
                circle(image, hands.get(i).getFingers().get(j), 10, new Scalar(255, 255, 255), 2);
                line(image, hands.get(i).getCenter(), hands.get(i).getFingers().get(j), new Scalar(255, 255, 255), 4);
            }
            System.out.println(fingersSize);
        }
        return image;
    }

    public Mat drawHandsColor(Mat image, List<Hand> hands) {
        int size = hands.size();
        for (int i = 0; i < size; i++) {
            int fingersSize = hands.get(i).getFingers().size();
            circle(image, hands.get(i).getCenter(), 20, new Scalar(128, 128, 128), 2);
            for (int j = 0; j < fingersSize; j++) {
                circle(image, hands.get(i).getFingers().get(j), 10, new Scalar(0, 0, 255), 2);
                line(image, hands.get(i).getCenter(), hands.get(i).getFingers().get(j), new Scalar(255, 0, 0), 4);
            }
            int pereponkiSize = hands.get(i).getPereponki().size();
            for (int j = 0; j < pereponkiSize; j++) {
                circle(image, hands.get(i).getPereponki().get(j), 10, new Scalar(0, 255, 0), 2);
            }

            System.out.println(fingersSize);
        }
        return image;
    }
}
