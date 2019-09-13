package lk.sliit.moodypp;

public class aqSession {
    private double aq1;
    private double aq2;
    private double aq3;
    private double aq4;
    private double aq5;
    private double aq6;
    private double aq7;
    private double aq8;

    public double getAq1() {
        return aq1;
    }

    public double getAq2() {
        return aq2;
    }

    public double getAq3() {
        return aq3;
    }

    public double getAq4() {
        return aq4;
    }

    public double getAq5() {
        return aq5;
    }

    public double getAq6() {
        return aq6;
    }

    public double getAq7() {
        return aq7;
    }

    public double getAq8() {
        return aq8;
    }

    public void setAq1(double aq1) {
        this.aq1 = aq1;
    }

    public void setAq2(double aq2) {
        this.aq2 = aq2;
    }

    public void setAq3(double aq3) {
        this.aq3 = aq3;
    }

    public void setAq4(double aq4) {
        this.aq4 = aq4;
    }

    public void setAq5(double aq5) {
        this.aq5 = aq5;
    }

    public void setAq6(double aq6) {
        this.aq6 = aq6;
    }

    public void setAq7(double aq7) {
        this.aq7 = aq7;
    }

    public void setAq8(double aq8) {
        this.aq8 = aq8;
    }
    public  double getAqPointTotal()
    {
        double aqPoint = this.aq1 + this.aq2+this.aq3+this.aq4+this.aq5+this.aq6+this.aq7+this.aq8;
        return aqPoint;
    }
}
