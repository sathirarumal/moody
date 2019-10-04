package lk.sliit.moodypp;

import android.telephony.SmsManager;

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

    public void sendSMS(String name){
        String Message = "This is an auto generated message from MOODY(depression and anxiety detector) "+name+"need some help from "+this.trustedname+" ";
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(trustednumber,null,Message,null,null);
    }
}
