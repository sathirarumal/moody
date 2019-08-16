package lk.sliit.moodypp;

import android.view.View;

public class DepressionAlgo{

    private double userHappyPerDay;     //p(H)
    private double userAngryPerDay;     //p(A)
    private double userSadPerDay;       //p(S)
    private double userNaturalPerDay;   //p(N)

    private double meanHappyInKB;  //p(H/D)
    private double meanAngryInKB;  //p(A/D)
    private double meanSadInKB;    //p(S/D)
    private double meanNaturalInKB;//p(N/D)

    private double depressionPerSample; //p(D)
    private double probaToHavDepWhenHappy;  //p(D/H)= (P(D)*(H/D))/P(H)
    private double probaToHavDepWhenAngry;  //p(D/A)= (P(D)*(A/D))/P(A)
    private double probaToHavDepWhenSad;    //p(D/S)= (P(D)*(S/D))/P(S)
    private double probaToHavDepWhenNatural;//p(D/N)= (P(D)*(N/D))/P(N)

    //using BAY'S theorem
    //P(D)*=P(D/A).P(A)+P(D/H).P(H)+P(D/N).P(N)+P(D/S).P(S)

    public void applyMultiplicationLow(){

        probaToHavDepWhenHappy=(depressionPerSample*meanHappyInKB)/userHappyPerDay;
        probaToHavDepWhenAngry=(depressionPerSample*meanAngryInKB)/userAngryPerDay;
        probaToHavDepWhenNatural=(depressionPerSample*meanNaturalInKB)/userNaturalPerDay;
        probaToHavDepWhenSad=(depressionPerSample*meanSadInKB)/userSadPerDay;

    }

    public double getProbalityToHasDepretionUsingEmotions(){

        this.applyMultiplicationLow();
        double PHD=(probaToHavDepWhenHappy*userHappyPerDay)+(probaToHavDepWhenAngry*userAngryPerDay)+(probaToHavDepWhenNatural*userNaturalPerDay)+(probaToHavDepWhenSad*userSadPerDay);
        return PHD;

    }

    public void setUserHappyPerDay(double userHappyPerDay) {
        this.userHappyPerDay = userHappyPerDay;
    }

    public void setUserAngryPerDay(double userAngryPerDay) {
        this.userAngryPerDay = userAngryPerDay;
    }

    public void setUserSadPerDay(double userSadPerDay) {
        this.userSadPerDay = userSadPerDay;
    }

    public void setUserNaturalPerDay(double userNaturalPerDay) {
        this.userNaturalPerDay = userNaturalPerDay;
    }

    public void setMeanHappyInKB(double meanHappyInKB) {
        this.meanHappyInKB = meanHappyInKB;
    }

    public void setMeanAngryInKB(double meanAngryInKB) {
        this.meanAngryInKB = meanAngryInKB;
    }

    public void setMeanSadInKB(double meanSadInKB) {
        this.meanSadInKB = meanSadInKB;
    }

    public void setMeanNaturalInKB(double meanNaturalInKB) {
        this.meanNaturalInKB = meanNaturalInKB;
    }

    public void setDepressionPerSample(double depressionPerSample) {
        this.depressionPerSample = depressionPerSample;
    }

}
