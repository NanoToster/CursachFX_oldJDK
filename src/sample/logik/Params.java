package sample.logik;

public class Params {
    private int area;
    private int r;
    private int step;
    private double cosThreshold;
    private double equalThreshold;

    private int rectSearchAreaY;
    private int rectSearchAreaX;
    private int whereSearchX;
    private int whereSearchY;

    public Params() {
        this.area=1000;
        this.cosThreshold=0.5;
        this.equalThreshold=1e-7;
        this.r=40;
        this.step=16;
        this.rectSearchAreaX = 20;
        this.rectSearchAreaY = 30;
        this.whereSearchX = 0;
        this.whereSearchY = 0;
    }

    public Params(int area, int r, int step, double cosThreshold, double equalThreshold) {
        this.area = area;
        this.r = r;
        this.step = step;
        this.cosThreshold = cosThreshold;
        this.equalThreshold = equalThreshold;
    }

    public int getArea() {
        return area;
    }

    public int getR() {
        return r;
    }

    public int getStep() {
        return step;
    }

    public double getCosThreshold() {
        return cosThreshold;
    }

    public double getEqualThreshold() {
        return equalThreshold;
    }

    public int getRectSearchAreaY() {
        return rectSearchAreaY;
    }

    public int getRectSearchAreaX() {
        return rectSearchAreaX;
    }

    public int getWhereSearchX() {
        return whereSearchX;
    }

    public void setWhereSearchX(int whereSearchX) {
        this.whereSearchX = whereSearchX;
    }

    public int getWhereSearchY() {
        return whereSearchY;
    }

    public void setWhereSearchY(int whereSearchY) {
        this.whereSearchY = whereSearchY;
    }
}
