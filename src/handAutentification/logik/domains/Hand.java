package handAutentification.logik.domains;

import org.opencv.core.Point;

import java.util.List;
import java.util.ArrayList;

public class Hand {
    private List<Point> fingers;
    private List<Point> pereponki;
    private Point center;
    private List<Point> contour;

    public Hand() {
        fingers = new ArrayList<>();
        pereponki = new ArrayList<>();
        contour = new ArrayList<>();
    }

    public List<Point> getFingers() {
        return fingers;
    }

    public void setFingers(List<Point> fingers) {
        this.fingers = fingers;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public List<Point> getContour() {
        return contour;
    }

    public void setContour(List<Point> contour) {
        this.contour = contour;
    }

    public List<Point> getPereponki() {
        return pereponki;
    }
}
