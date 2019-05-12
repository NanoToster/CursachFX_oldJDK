package handAutentification.services;

import org.opencv.core.Point;

import java.util.Comparator;

public class FingersXComparators implements Comparator<Point> {

    @Override
    public int compare(Point o1, Point o2) {
        if (o1.x > o2.x){
            return 1;
        } else if (o1.x == o1.x){
            return 0;
        } else {
            return -1;
        }
    }
}
