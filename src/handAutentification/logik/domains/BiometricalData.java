package handAutentification.logik.domains;


import handAutentification.logik.Params;

import java.util.Objects;

public class BiometricalData {
    //средний
    private double middleFingerHeight = 0;
    private double middleFingerWidth = 0;
    //Указательный
    private double foreFingerHeight = 0;
    private double foreFingerWidth = 0;
    //безымянный
    private double noNameFingerHeight = 0;
    private double noNameFingerWidth = 0;
    //Мизинец
    private double littleFingerHeight = 0;
    private double littleFingerWidth = 0;
    //Большой
    private double bigFingerHeight = 0;
    private double bigFingerWidth = 0;

    private String whichHand = ""; // left or right

    public BiometricalData() {

    }

    public BiometricalData(double middleFingerHeight, double middleFingerWidth,
                           double foreFingerHeight, double foreFingerWidth,
                           double noNameFingerHeight, double noNameFingerWidth,
                           double littleFingerHeight, double littleFingerWidth,
                           double bigFingerHeight, double bigFingerWidth) {
        this.middleFingerHeight = middleFingerHeight;
        this.middleFingerWidth = middleFingerWidth;
        this.foreFingerHeight = foreFingerHeight;
        this.foreFingerWidth = foreFingerWidth;
        this.noNameFingerHeight = noNameFingerHeight;
        this.noNameFingerWidth = noNameFingerWidth;
        this.littleFingerHeight = littleFingerHeight;
        this.littleFingerWidth = littleFingerWidth;
        this.bigFingerHeight = bigFingerHeight;
        this.bigFingerWidth = bigFingerWidth;
    }

    @Override
    public String toString() {
        return "BiometricalData{" +
                "middleFingerHeight=" + middleFingerHeight +
                "\n, middleFingerWidth=" + middleFingerWidth +
                "\n, foreFingerHeight=" + foreFingerHeight +
                "\n, foreFingerWidth=" + foreFingerWidth +
                "\n, noNameFingerHeight=" + noNameFingerHeight +
                "\n, noNameFingerWidth=" + noNameFingerWidth +
                "\n, littleFingerHeight=" + littleFingerHeight +
                "\n, littleFingerWidth=" + littleFingerWidth +
                "\n, bigFingerHeight=" + bigFingerHeight +
                "\n, bigFingerWidth=" + bigFingerWidth +
                "\n, whichHand='" + whichHand + '\'' +
                '}';
    }

    public String getWhichHand() {
        return whichHand;
    }

    public void setWhichHand(String whichHand) {
        this.whichHand = whichHand;
    }

    public double getMiddleFingerHeight() {
        return middleFingerHeight;
    }

    public void setMiddleFingerHeight(double middleFingerHeight) {
        this.middleFingerHeight = middleFingerHeight;
    }

    public double getMiddleFingerWidth() {
        return middleFingerWidth;
    }

    public void setMiddleFingerWidth(double middleFingerWidth) {
        this.middleFingerWidth = middleFingerWidth;
    }

    public double getForeFingerHeight() {
        return foreFingerHeight;
    }

    public void setForeFingerHeight(double foreFingerHeight) {
        this.foreFingerHeight = foreFingerHeight;
    }

    public double getForeFingerWidth() {
        return foreFingerWidth;
    }

    public void setForeFingerWidth(double foreFingerWidth) {
        this.foreFingerWidth = foreFingerWidth;
    }

    public double getNoNameFingerHeight() {
        return noNameFingerHeight;
    }

    public void setNoNameFingerHeight(double noNameFingerHeight) {
        this.noNameFingerHeight = noNameFingerHeight;
    }

    public double getNoNameFingerWidth() {
        return noNameFingerWidth;
    }

    public void setNoNameFingerWidth(double noNameFingerWidth) {
        this.noNameFingerWidth = noNameFingerWidth;
    }

    public double getLittleFingerHeight() {
        return littleFingerHeight;
    }

    public void setLittleFingerHeight(double littleFingerHeight) {
        this.littleFingerHeight = littleFingerHeight;
    }

    public double getLittleFingerWidth() {
        return littleFingerWidth;
    }

    public void setLittleFingerWidth(double littleFingerWidth) {
        this.littleFingerWidth = littleFingerWidth;
    }

    public double getBigFingerHeight() {
        return bigFingerHeight;
    }

    public void setBigFingerHeight(double bigFingerHeight) {
        this.bigFingerHeight = bigFingerHeight;
    }

    public double getBigFingerWidth() {
        return bigFingerWidth;
    }

    public void setBigFingerWidth(double bigFingerWidth) {
        this.bigFingerWidth = bigFingerWidth;
    }

    //TODO переделать это дерьмо собачье
    @Override
    public boolean equals(Object o) {
        double maxDifference = Params.getInstance().getBiometricalMaxDifference();
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometricalData that = (BiometricalData) o;
        System.out.println("++++++++++++++++++THIS++++++++++++++++++++++++++");
        System.out.println(toString());
        System.out.println("++++++++++++++++++THAT++++++++++++++++++++++++++");
        System.out.println(that.toString());
        int biometricalCounter = 0;
        if (procentDifference(that.middleFingerHeight, middleFingerHeight) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.middleFingerWidth, middleFingerWidth) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.foreFingerHeight, foreFingerHeight) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.foreFingerWidth, foreFingerWidth) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.noNameFingerHeight, noNameFingerHeight) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.noNameFingerWidth, noNameFingerWidth) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.littleFingerHeight, littleFingerHeight) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.littleFingerWidth, littleFingerWidth) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.bigFingerHeight, bigFingerHeight) < maxDifference) {
            biometricalCounter++;
        }
        if (procentDifference(that.bigFingerWidth, bigFingerWidth) < maxDifference) {
            biometricalCounter++;
        }
        System.out.println("Biometrical counter: " + biometricalCounter);
        if (biometricalCounter > 5) {
            return true;
        } else {
            return false;
        }
    }

    private double procentDifference(double a, double b) {
//        Процентная разница = | (a — b) / [ (a + b) / 2 ] | * 100 %
        double result = Math.abs((a - b) / ((a + b) / 2)) * 100;
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(middleFingerHeight, middleFingerWidth, foreFingerHeight, foreFingerWidth, noNameFingerHeight, noNameFingerWidth, littleFingerHeight, littleFingerWidth, bigFingerHeight, bigFingerWidth);
    }
}
