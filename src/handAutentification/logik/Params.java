package handAutentification.logik;

public class Params {
    private static Params instance = null;
    // for HandDetector
    private int area;
    private int r;
    private int step;
    private double cosThreshold;
    private double equalThreshold;
    // for PicShowController
    private int searchRectWidth;
    private int searchRectHeight;

    private double biometricalMaxDifference;

    private Params() {
        this.area=1000;
        this.cosThreshold=0.5;
        this.equalThreshold=1e-7;
        this.r=40;
        this.step=16;
        this.searchRectHeight = 400;
        this.searchRectWidth = 300;
        this.biometricalMaxDifference = 5;
    }

    public static Params getInstance(){
        if (instance == null){
            instance = new Params();
        }
        return instance;
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

    public int getSearchRectWidth() {
        return searchRectWidth;
    }

    public int getSearchRectHeight() {
        return searchRectHeight;
    }

    public double getBiometricalMaxDifference() {
        return biometricalMaxDifference;
    }
}
