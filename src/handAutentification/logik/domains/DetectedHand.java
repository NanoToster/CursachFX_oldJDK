package handAutentification.logik.domains;

import org.opencv.core.Point;

public class DetectedHand {
    private Point bigFinger; // большой
    private Point foreFinger; // указательный
    private Point middleFinger; // средний
    private Point noNameFinger; // Безымянный
    private Point littlefinger; // Мизинец

    private Point bigPereponka;
    private Point middlePereponkaLeft; // Относительно расположения на экране
    private Point middlePereponkaRight;
    private Point littlePereponka;

    public DetectedHand() {

    }

    public Point getBigFinger() {
        return bigFinger;
    }

    public void setBigFinger(Point bigFinger) {
        this.bigFinger = bigFinger;
    }

    public Point getForeFinger() {
        return foreFinger;
    }

    public void setForeFinger(Point foreFinger) {
        this.foreFinger = foreFinger;
    }

    public Point getMiddleFinger() {
        return middleFinger;
    }

    public void setMiddleFinger(Point middleFinger) {
        this.middleFinger = middleFinger;
    }

    public Point getNoNameFinger() {
        return noNameFinger;
    }

    public void setNoNameFinger(Point noNameFinger) {
        this.noNameFinger = noNameFinger;
    }

    public Point getLittlefinger() {
        return littlefinger;
    }

    public void setLittlefinger(Point littlefinger) {
        this.littlefinger = littlefinger;
    }

    public Point getBigPereponka() {
        return bigPereponka;
    }

    public void setBigPereponka(Point bigPereponka) {
        this.bigPereponka = bigPereponka;
    }

    public Point getMiddlePereponkaLeft() {
        return middlePereponkaLeft;
    }

    public void setMiddlePereponkaLeft(Point middlePereponkaLeft) {
        this.middlePereponkaLeft = middlePereponkaLeft;
    }

    public Point getMiddlePereponkaRight() {
        return middlePereponkaRight;
    }

    public void setMiddlePereponkaRight(Point middlePereponkaRight) {
        this.middlePereponkaRight = middlePereponkaRight;
    }

    public Point getLittlePereponka() {
        return littlePereponka;
    }

    public void setLittlePereponka(Point littlePereponka) {
        this.littlePereponka = littlePereponka;
    }
}
