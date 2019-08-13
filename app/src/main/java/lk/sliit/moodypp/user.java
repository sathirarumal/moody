package lk.sliit.moodypp;

public class user {



    public String email;
    public String callName;
    public String age;
    public String gender;
    public String status;

    public user(){

    }

    public user(String email, String callName, String age, String gender, String status) {
        this.email = email;
        this.callName = callName;
        this.age = age;
        this.gender = gender;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
