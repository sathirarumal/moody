package lk.sliit.moodypp;

public class SOS {

    String myNo;
    String trustedname;
    String trustednumber;
    String method;

    public SOS(){

    }

    public SOS(String myNo, String trustedname, String trustednumber, String method) {
        this.myNo = myNo;
        this.trustedname = trustedname;
        this.trustednumber = trustednumber;
        this.method = method;
    }

    public String getMyNo() {
        return myNo;
    }

    public String getTrustedname() {
        return trustedname;
    }

    public String getTrustednumber() {
        return trustednumber;
    }

    public String getMethod() {
        return method;
    }
}
