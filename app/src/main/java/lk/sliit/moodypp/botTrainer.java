package lk.sliit.moodypp;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class botTrainer {

    public FirebaseDatabase database;
    public DatabaseReference userDetailRef;
    public DatabaseReference userIdRef;
    public DatabaseReference An_Database;

    private FirebaseAuth mAuth;
    private String userId;
    public  String status;
    public String type;

    private int NAA;
    private int SD;
    private int HD;
    private int ED;

    public String replyState;

    dqSession dqSessionObj;
    fdqSession fdqSessionObj;
    aqSession aqSessionObj;

    public botTrainer() {

    }

    public void setReplyState(String replyState) {
        this.replyState = replyState;
    }


public double ansPoint(String msg, String state)
{
    if(state.equals("low"))
    {
        return ans_points_low(msg);
    }
    else if (state.equals("high"))
    {
        return ans_points_high(msg);
    }
    return 0;
}



    public double ans_points_low(String msg)
    {
        if (msg.equals("Not at all"))
        {
            return  0;
        }
        else if(msg.equals("Several days"))
        {
            return  4;
        }
        else if(msg.equals("More than half of the days"))
        {
            return  6;
        }
        else if(msg.equals("Nearly everyday"))
        {
            return  10;
        }

        return 0;
    }



    public double ans_points_high(String msg)
    {
        if (msg.equals("Not at all"))
        {
            return  0;
        }
        else if(msg.equals("Several days"))
        {
            return  5;
        }
        else if(msg.equals("More than half of the days"))
        {
            return  20;
        }
        return 0;
    }




    public String first_time_ansPoint(String msg) {
        if (msg.equals("Not at all")) {
            return "first";
        } else if (msg.equals("Several days")) {
            return "second";
        } else if (msg.equals("More than half of the days")) {
            return "third";
        } else if (msg.equals("Nearly everyday")) {
            return "fourth";
        }
        return null;
    }

    public void proccessKnowledgeBase(){

    }

}
