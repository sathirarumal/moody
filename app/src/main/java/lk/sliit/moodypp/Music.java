package lk.sliit.moodypp;

public class Music {

    public String Mname;
    public String downloadLink;
    public String img;

    public Music(String mname, String downloadLink, String img) {
        Mname = mname;
        this.downloadLink = downloadLink;
        this.img = img;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String mname) {
        Mname = mname;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

