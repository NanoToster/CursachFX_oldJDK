package handAutentification.logik;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import handAutentification.logik.domains.DetectedHand;
import handAutentification.logik.domains.Hand;

import java.util.List;

public class HandProcesser {
    private Hand hand;
    private Params params;
    private Mat blackWhiteImg;
    private DetectedHand detectedHand;

    public HandProcesser(Hand hand, Mat blackWhiteImg) {
        this.hand = hand;
        this.detectedHand = new DetectedHand();
        this.params = Params.getInstance();
        this.blackWhiteImg = blackWhiteImg;
    }

    public Mat init(Mat colorImg) {
//        findBigPereponka();
//        leftOrRightHand(detectedHand.getBigPereponka());
//        finAllOtherPereponka();
//        findAllOtherFingers();
        return colorImg;
    }


    // находим самую нижнюю перепонку, это будет перепонка большого пальца,
    // идём от нее к соседней, затем ищем между ними палец и повторить

    private void findBigPereponka() {
        Point bigPereponka = hand.getPereponki().get(0);
        for (Point pereponka : hand.getPereponki()) {
            if (pereponka.y > bigPereponka.y) {
                bigPereponka = pereponka;
            }
        }
        detectedHand.setBigPereponka(bigPereponka);
    }

    private void leftOrRightHand(Point bigPereponka) {
        double minRange = params.getSearchRectWidth();
        Point nearestPereponka = null;
        for (Point pereponka : hand.getPereponki()) {
            if (pereponka != bigPereponka) {
                if (Math.abs(pereponka.x - bigPereponka.x) < minRange) {
                    nearestPereponka = pereponka;
                    minRange = Math.abs(pereponka.x - bigPereponka.x);
                }
            }
        }
        if (bigPereponka.x - nearestPereponka.x < 0){
            // left Hand
            detectedHand.setWhichHand("left");
            System.out.println("left");
        } else {
            // right hand
            detectedHand.setWhichHand("right");
            System.out.println("right");
        }
    }

    // TODO переделать этот колхоз!
    // + сделать проверку, что пальцы выше перепонок
    private void findAllOtherFingers(){
        if (detectedHand.getWhichHand().equals("left")){
            Point bigFinger = findMostLeft(hand.getFingers());
            detectedHand.setBigFinger(bigFinger);
            hand.getFingers().remove(bigFinger);

            Point tmp = findMostLeft(hand.getFingers());
            detectedHand.setForeFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostLeft(hand.getFingers());
            detectedHand.setMiddleFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostLeft(hand.getFingers());
            detectedHand.setNoNameFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostLeft(hand.getFingers());
            detectedHand.setLittlefinger(tmp);
            hand.getFingers().remove(tmp);
        } else {
            Point bigFinger = findMostRight(hand.getFingers());
            detectedHand.setBigFinger(bigFinger);
            hand.getFingers().remove(bigFinger);

            Point tmp = findMostRight(hand.getFingers());
            detectedHand.setForeFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostRight(hand.getFingers());
            detectedHand.setMiddleFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostRight(hand.getFingers());
            detectedHand.setNoNameFinger(tmp);
            hand.getFingers().remove(tmp);

            tmp = findMostRight(hand.getFingers());
            detectedHand.setLittlefinger(tmp);
            hand.getFingers().remove(tmp);
        }
    }

    // TODO переделать этот колхоз! [2]
    private void finAllOtherPereponka() {
        hand.getPereponki().remove(detectedHand.getBigPereponka());
        if (detectedHand.getWhichHand().equals("left")){
            Point tmp = findMostLeft(hand.getFingers());
            detectedHand.setMiddlePereponkaLeft(tmp);
            hand.getPereponki().remove(tmp);

            tmp = findMostLeft(hand.getFingers());
            detectedHand.setMiddlePereponkaRight(tmp);
            hand.getPereponki().remove(tmp);

            tmp = findMostLeft(hand.getFingers());
            detectedHand.setLittlePereponka(tmp);
            hand.getPereponki().remove(tmp);
        } else {
            Point tmp = findMostRight(hand.getFingers());
            detectedHand.setMiddlePereponkaLeft(tmp);
            hand.getPereponki().remove(tmp);

            tmp = findMostRight(hand.getFingers());
            detectedHand.setMiddlePereponkaRight(tmp);
            hand.getPereponki().remove(tmp);

            tmp = findMostRight(hand.getFingers());
            detectedHand.setLittlePereponka(tmp);
            hand.getPereponki().remove(tmp);
        }

    }

    private Point findMostLeft(List <Point> list) {
        Point left = list.get(0);
        for (Point finger:list) {
            if (finger.x < left.x) {
                left = finger;
            }
        }
        return left;
    }

    private Point findMostRight(List <Point> list) {
        Point right = list.get(0);
        for (Point finger:list) {
            if (finger.x > right.x) {
                right = finger;
            }
        }
        return right;
    }
}
