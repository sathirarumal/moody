package lk.sliit.moodypp;

public class daProbabilityAlgorithm {

    private double userHappyPerDay;     //p(H)
    private double userAngryPerDay;     //p(A)
    private double userSadPerDay;       //p(S)
    private double userNaturalPerDay;   //p(N)
    private double userFearPerDay;      //p(F)

    private double meanHappyInKB;  //p(H/D)
    private double meanAngryInKB;  //p(A/D)
    private double meanSadInKB;    //p(S/D)
    private double meanNaturalInKB;//p(N/D)
    private double meanFearInKB;   //p(F/D)

    private double disoderPerSample; //p(D)

    //using BAY'S theorem
    private double probaToHaveDisorderWhenHappy;  //p(D/H)= (P(D)*(H/D))/P(H)
    private double probaToHaveDisorderWhenAngry;  //p(D/A)= (P(D)*(A/D))/P(A)
    private double probaToHaveDisorderWhenSad;    //p(D/S)= (P(D)*(S/D))/P(S)
    private double probaToHaveDisorderWhenNatural;//p(D/N)= (P(D)*(N/D))/P(N)
    private double probaToHaveDisorderWhenFear;   //p(D/F)= (P(D)*(F/D))/P(F)

    //using total probability
    //P(D)*=P(D/A).P(A)+P(D/H).P(H)+P(D/N).P(N)+P(D/S).P(S)

    public void applyMultiplicationLow(){

        probaToHaveDisorderWhenHappy =(disoderPerSample*userHappyPerDay)/meanHappyInKB; //p(D/H) =(P(D)*P(H))/p(H/D)
        probaToHaveDisorderWhenAngry =(disoderPerSample*userAngryPerDay)/meanAngryInKB;
        probaToHaveDisorderWhenNatural =(disoderPerSample*userNaturalPerDay)/meanNaturalInKB;
        probaToHaveDisorderWhenSad =(disoderPerSample*userSadPerDay)/meanSadInKB;
        probaToHaveDisorderWhenFear =(disoderPerSample*userFearPerDay)/meanFearInKB;


    }

    public double getProbalityToHavingDisoderUsingEmotions(){

        this.applyMultiplicationLow();
        double PHD=(probaToHaveDisorderWhenHappy *userHappyPerDay)+(probaToHaveDisorderWhenAngry *userAngryPerDay)+(probaToHaveDisorderWhenNatural *userNaturalPerDay)+(probaToHaveDisorderWhenSad *userSadPerDay) +(probaToHaveDisorderWhenFear * userFearPerDay);
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

    public void setUserFearPerDay(double userFearPerDay) {
        this.userFearPerDay = userFearPerDay;
    }

    public void setMeanFearInKB(double meanFearInKB) {
        this.meanFearInKB = meanFearInKB;
    }

    public void setDisoderPerSample(double disoderPerSample) {
        this.disoderPerSample = disoderPerSample;
    }
}
