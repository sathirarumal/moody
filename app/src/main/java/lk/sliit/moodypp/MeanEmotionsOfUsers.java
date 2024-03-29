package lk.sliit.moodypp;

public class MeanEmotionsOfUsers {

    public double meanHappyInKB;  //p(H/D)
    public double meanAngryInKB;  //p(A/D)
    public double meanSadInKB;    //p(S/D)
    public double meanNaturalInKB;//p(N/D)
    public double meanFearInKB;//p(N/D)

    public double getMeanFearInKB() {
        return meanFearInKB;
    }

    public MeanEmotionsOfUsers(){}

    public MeanEmotionsOfUsers(double meanHappyInKB, double meanAngryInKB, double meanSadInKB, double meanNaturalInKB, double meanFearInKB) {
        this.meanHappyInKB = meanHappyInKB;
        this.meanAngryInKB = meanAngryInKB;
        this.meanSadInKB = meanSadInKB;
        this.meanNaturalInKB = meanNaturalInKB;
        this.meanFearInKB = meanFearInKB;
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

    public double getMeanHappyInKB() {
        return meanHappyInKB;
    }

    public double getMeanAngryInKB() {
        return meanAngryInKB;
    }

    public double getMeanSadInKB() {
        return meanSadInKB;
    }

    public double getMeanNaturalInKB() {
        return meanNaturalInKB;
    }
}
