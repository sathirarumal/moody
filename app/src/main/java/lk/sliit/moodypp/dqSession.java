package lk.sliit.moodypp;

public class dqSession {
    private double dq1;
    private double dq2;
    private double dq3;
    private double dq4;
    private double dq5;
    private final double max_value=35;

    public void setDq1(double dq1)
    {
        this.dq1 = dq1;
    }

    public void setDq2(double dq2) {

        this.dq2 = dq2;
    }

    public void setDq3(double dq3) {
        this.dq3 = dq3;
    }

    public void setDq4(double dq4) {
        this.dq4 = dq4;
    }

    public void setDq5(double dq5) {
        this.dq5 = dq5;
    }

    public double getDq1() {
        return dq1;
    }

    public double getDq2() {
        return dq2;
    }

    public double getDq3() {
        return dq3;
    }

    public double getDq4() {
        return dq4;
    }

    public double getDq5() {
        return dq5;
    }

    public double GetDqPointPercentage(){
        double dqPoint=this.dq1+this.dq2+this.dq3+this.dq4+this.dq5;
        double dPercentage = dqPoint/max_value ;
        return dPercentage;

    }
}
