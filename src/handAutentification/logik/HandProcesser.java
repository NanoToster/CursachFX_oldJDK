package handAutentification.logik;

import handAutentification.logik.domains.BiometricalData;
import org.bytedeco.javacpp.PointerPointer;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import handAutentification.logik.domains.DetectedHand;
import handAutentification.logik.domains.Hand;
import org.opencv.core.Scalar;

import static org.opencv.imgproc.Imgproc.line;

public class HandProcesser {
    private Hand hand;
    private Params params;
    private Mat blackWhiteImg;
    private DetectedHand detectedHand;
    private BiometricalData biometricalData;

    public HandProcesser(Hand hand, Mat blackWhiteImg) {
        this.hand = hand;
        this.detectedHand = new DetectedHand();
        this.params = Params.getInstance();
        this.blackWhiteImg = blackWhiteImg;
        this.biometricalData = new BiometricalData();
    }

    public Mat init(Mat colorImg) {
        findMiddleFinger();
        findMiddlePereponksAndBiometrics();
        findLeftAndRightPereponks();
        findNoNameAndLittleFingers();
        findNoNameAndLittleBiometrics();

        colorImg = drawMiddleHeight(colorImg);
        return colorImg;
    }

    public BiometricalData getBiometricalData(){
        return biometricalData;
    }

    //Находим самый верхний палец, затем находим 2 ближние перепонки по Х, считаем
    //высоту до ближайшей из них по У

    // For Detected Hand

    //1
    private void findMiddleFinger() {
        Point middleFinger = new Point(params.getSearchRectWidth(),
                params.getSearchRectHeight());
        for (Point point : hand.getFingers()) {
            if (point.y < middleFinger.y) {
                middleFinger = point;
            }
        }
        detectedHand.setMiddleFinger(middleFinger);
    }

    //3
    private void findLeftAndRightPereponks() {
        if (hand.getPereponki().size() == 4) {
            if (detectedHand.getMiddlePereponkaRight() != null &&
                    detectedHand.getMiddlePereponkaLeft() != null) {
                Point mostLeftPereponka = new Point(0, 0);
                Point mostRightPereponka = new Point(0, 0);
                for (Point point : hand.getPereponki()) {
                    if (point.x > detectedHand.getMiddlePereponkaRight().x) {
                        mostRightPereponka = point;
                    }
                    if (point.x < detectedHand.getMiddlePereponkaLeft().x) {
                        mostLeftPereponka = point;
                    }
                }
                if (mostLeftPereponka.x != 0 && mostRightPereponka.x != 0) {
                    if (mostLeftPereponka.y < mostRightPereponka.y) {
                        //right hand
                        detectedHand.setLittlePereponka(mostLeftPereponka);
                        detectedHand.setBigPereponka(mostRightPereponka);
                        biometricalData.setWhichHand("right");
                    } else {
                        //left hand
                        detectedHand.setLittlePereponka(mostRightPereponka);
                        detectedHand.setBigPereponka(mostLeftPereponka);
                        biometricalData.setWhichHand("left");
                    }
                } else {
                    System.out.println("Cant find most left or right pereponks");
                }
            } else {
                System.out.println("We found 4 pereponks,but dont find middle pereponks");
            }
        } else {
            System.out.println("We founded not 4 pereponks!");
        }
    }

    //4
    private void findNoNameAndLittleFingers() {
        if (!biometricalData.getWhichHand().equals("")) {
            if (biometricalData.getWhichHand().equals("left")) {
                Point noNameFinger = new Point(0, params.getSearchRectHeight());
                Point littleFinger = new Point(0, params.getSearchRectHeight());
                for (Point point : hand.getFingers()) {
                    if (point.x > detectedHand.getMiddleFinger().x &&
                            Math.abs(detectedHand.getMiddleFinger().y - point.y) <
                                    Math.abs(detectedHand.getMiddleFinger().y - noNameFinger.y)) {
                        noNameFinger = point;
                    }
                }
                if (noNameFinger.x != 0) {
                    detectedHand.setNoNameFinger(noNameFinger);
                    for (Point point : hand.getFingers()) {
                        if (point.x > detectedHand.getMiddleFinger().x &&
                                Math.abs(detectedHand.getMiddleFinger().y - point.y) <
                                        Math.abs(detectedHand.getMiddleFinger().y - littleFinger.y) &&
                                point != detectedHand.getNoNameFinger()) {
                            littleFinger = point;
                        }
                    }
                    if (littleFinger.x != 0) {
                        detectedHand.setLittlefinger(littleFinger);
                    } else {
                        System.out.println("We cant find little finger");
                    }
                } else {
                    System.out.println("NoName fingerSearch fail");
                }
            } else if (biometricalData.getWhichHand().equals("right")) {
                Point noNameFinger = new Point(0, params.getSearchRectHeight());
                Point littleFinger = new Point(0, params.getSearchRectHeight());
                for (Point point : hand.getFingers()) {
                    if (point.x < detectedHand.getMiddleFinger().x &&
                            Math.abs(detectedHand.getMiddleFinger().y - point.y) <
                                    Math.abs(detectedHand.getMiddleFinger().y - noNameFinger.y)) {
                        noNameFinger = point;
                    }
                }
                if (noNameFinger.x != 0) {
                    detectedHand.setNoNameFinger(noNameFinger);
                    for (Point point : hand.getFingers()) {
                        if (point.x < detectedHand.getMiddleFinger().x &&
                                Math.abs(detectedHand.getMiddleFinger().y - point.y) <
                                        Math.abs(detectedHand.getMiddleFinger().y - littleFinger.y) &&
                                point != detectedHand.getNoNameFinger()) {
                            littleFinger = point;
                        }
                    }
                    if (littleFinger.x != 0) {
                        detectedHand.setLittlefinger(littleFinger);
                    } else {
                        System.out.println("We cant find little finger");
                    }
                } else {
                    System.out.println("NoName fingerSearch fail");
                }
            }
        } else {
            System.out.println("Cant find No Name and little biometrics!");
        }
    }

    // Biometrical Data

    //2
    private void findMiddlePereponksAndBiometrics() {
        Point nearestPereponka = new Point(0, params.getSearchRectHeight());
        Point nearestPereponka2 = new Point(0, params.getSearchRectHeight());

        if (detectedHand.getMiddleFinger() != null) {
            for (Point point : hand.getPereponki()) {
                if (Math.abs(point.x - detectedHand.getMiddleFinger().x) <
                        Math.abs(nearestPereponka.x - detectedHand.getMiddleFinger().x)) {
                    nearestPereponka = point;
                }
            }
            for (Point point : hand.getPereponki()) {
                if (point != nearestPereponka) {
                    if (Math.abs(point.x - detectedHand.getMiddleFinger().x) <
                            Math.abs(nearestPereponka2.x - detectedHand.getMiddleFinger().x)) {
                        nearestPereponka2 = point;
                    }
                }
            }
            // which right and which left
            if (nearestPereponka.x > nearestPereponka2.x) {
                detectedHand.setMiddlePereponkaRight(nearestPereponka);
                detectedHand.setMiddlePereponkaLeft(nearestPereponka2);
            } else {
                detectedHand.setMiddlePereponkaRight(nearestPereponka2);
                detectedHand.setMiddlePereponkaLeft(nearestPereponka);
            }

            biometricalData.setMiddleFingerWidth(Math.abs(detectedHand.getMiddlePereponkaLeft().x -
                    detectedHand.getMiddlePereponkaRight().x));

            // set middleFinger height
            if (detectedHand.getMiddlePereponkaRight().y <
                    detectedHand.getMiddlePereponkaLeft().y) {
                biometricalData.setMiddleFingerHeight(detectedHand.getMiddlePereponkaRight().y -
                        detectedHand.getMiddleFinger().y);
            } else {
                biometricalData.setMiddleFingerHeight(detectedHand.getMiddlePereponkaLeft().y -
                        detectedHand.getMiddleFinger().y);
            }
        } else {
            System.out.println("Cant find middle pereponks!");
        }
    }

    //5
    private void findNoNameAndLittleBiometrics() {
        if (detectedHand.getLittlefinger() != null &&
                detectedHand.getNoNameFinger() != null) {
            if (biometricalData.getWhichHand().equals("left")) {
                biometricalData.setNoNameFingerWidth(Math.abs(detectedHand.getMiddlePereponkaRight().x -
                        detectedHand.getLittlePereponka().x));
                biometricalData.setNoNameFingerHeight(Math.abs(detectedHand.getNoNameFinger().y -
                        detectedHand.getMiddlePereponkaRight().y));
            } else {
                biometricalData.setNoNameFingerWidth(Math.abs(detectedHand.getMiddlePereponkaLeft().x -
                        detectedHand.getLittlePereponka().x));
                biometricalData.setNoNameFingerHeight(Math.abs(detectedHand.getNoNameFinger().y -
                        detectedHand.getMiddlePereponkaLeft().y));
            }
            biometricalData.setLittleFingerHeight(Math.abs(detectedHand.getLittlefinger().y -
                    detectedHand.getLittlePereponka().y));
            biometricalData.setLittleFingerWidth(2 * Math.abs(detectedHand.getLittlefinger().x -
                    detectedHand.getLittlePereponka().x));
        } else {
            System.out.println("We cand find NoName and little Biometrics!");
        }
    }

    // Draw results
    private Mat drawMiddleHeight(Mat original) {
        if (biometricalData.getMiddleFingerHeight() != 0 && biometricalData.getMiddleFingerWidth() != 0) {
            line(original, new Point(detectedHand.getMiddlePereponkaLeft().x,
                            detectedHand.getMiddleFinger().y +
                                    biometricalData.getMiddleFingerHeight()),
                    new Point(detectedHand.getMiddlePereponkaLeft().x + biometricalData.getMiddleFingerWidth(),
                            detectedHand.getMiddleFinger().y +
                                    biometricalData.getMiddleFingerHeight()),
                    new Scalar(255, 0, 0), 4);

        } else {
            System.out.println("Cant draw middle elements!");
        }
        return original;
    }
}
