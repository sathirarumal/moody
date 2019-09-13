package lk.sliit.moodypp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Calculate extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference userDetailRef;
    public DatabaseReference userEmotionRef;
    public DatabaseReference meanEmotionRef;
    public DatabaseReference depCountRef;
    public DatabaseReference UserchildRef;
    public DatabaseReference UserEmotionchildRef;
    public DatabaseReference EmotionUserIDchildRef;
    public DatabaseReference MeanChildRef;
    private static final String TAG=Calculate.class.getSimpleName();
    private String userId;
    private FirebaseAuth mAuth;
    private String status;
    private String type;
    private double meanHappyInKB;  //p(H/D)
    private double meanAngryInKB;  //p(A/D)
    private double meanSadInKB;    //p(S/D)
    private double meanNaturalInKB;//p(N/D)

    private double TempmeanHappyInKB;  //p(H/D)
    private double TempmeanAngryInKB;  //p(A/D)
    private double TempmeanSadInKB;    //p(S/D)
    private double TempmeanNaturalInKB;//p(N/D)

    private EmotionFirstDay emotionFirstDay;
    private double depressionPerSample; //p(D)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebaseDatabase=FirebaseDatabase.getInstance();
        userDetailRef=firebaseDatabase.getReference("Users");
        userEmotionRef=firebaseDatabase.getReference("UserEmotionsFistDay");
        meanEmotionRef=firebaseDatabase.getReference("MeanEmotionsDepressionUsers");
        depCountRef=firebaseDatabase.getReference("DepressionCount");

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        UserchildRef=userDetailRef.child(userId);


        /*userEmotionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                depressionPerSample=(double)dataSnapshot.getChildrenCount();
                depCountRef.setValue(depressionPerSample);
                meanAngryInKB=(TempmeanAngryInKB+emotionFirstDay.Angry)/depressionPerSample;
                meanHappyInKB=(TempmeanHappyInKB+emotionFirstDay.Happy)/depressionPerSample;
                meanNaturalInKB=(TempmeanNaturalInKB+emotionFirstDay.Natural)/depressionPerSample;
                meanSadInKB=(TempmeanSadInKB+emotionFirstDay.Sad)/depressionPerSample;

                Toast.makeText(Calculate.this, ""+emotionFirstDay.Angry,Toast.LENGTH_SHORT).show();
                MeanEmotionsDepressionUsers medu=new MeanEmotionsDepressionUsers(meanHappyInKB,meanAngryInKB,meanSadInKB,meanNaturalInKB);
                meanEmotionRef.setValue(medu);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    protected void onStart() {
        super.onStart();
        UserchildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userObj=dataSnapshot.getValue(user.class);
                status=userObj.getStatus();
                if(status.equals("I take/taken medicine for Depression") || status.equals("I take/taken medicine for Both") ){
                    type="Depression";
                }else if(status.equals("I take/taken medicine for Anxiety")){
                    type="Anxiety";
                }else {
                    type="Unknown";
                }

                insertDumyData();
                //SetMeanEmotionsDepressionUsers();
                //addEmotionsToKnowledgeBase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void insertDumyData(){

        emotionFirstDay =new EmotionFirstDay(0.80,0.1,0.5,0.5);
        UserEmotionchildRef=userEmotionRef.child(type);
        EmotionUserIDchildRef=UserEmotionchildRef.child(userId);
        EmotionUserIDchildRef.setValue(emotionFirstDay);
    }


    public void SetMeanEmotionsDepressionUsers(){
                meanEmotionRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MeanEmotionsDepressionUsers meanEmotionsDepressionUsers = dataSnapshot.getValue(MeanEmotionsDepressionUsers.class);

                        TempmeanAngryInKB = meanEmotionsDepressionUsers.getMeanAngryInKB();
                        TempmeanHappyInKB = meanEmotionsDepressionUsers.getMeanHappyInKB();
                        TempmeanNaturalInKB = meanEmotionsDepressionUsers.getMeanNaturalInKB();
                        TempmeanSadInKB = meanEmotionsDepressionUsers.getMeanSadInKB();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public double proccessDepressionPerDay(double happy,double angry,double sad,double natural){

        DepressionAlgo day=new DepressionAlgo();

        day.setMeanAngryInKB(meanAngryInKB);
        day.setMeanHappyInKB(meanHappyInKB);
        day.setMeanNaturalInKB(meanNaturalInKB);
        day.setMeanSadInKB(meanSadInKB);
        day.setDepressionPerSample(depressionPerSample);
        day.setUserAngryPerDay(angry);
        day.setUserHappyPerDay(happy);
        day.setUserSadPerDay(sad);
        day.setUserNaturalPerDay(natural);

        double depressionPoint=day.getProbalityToHasDepretionUsingEmotions();
        return  depressionPoint;
    }

    public void calculate(View view){

        //double result=proccessDepressionPerDay(0.3,0.1,0.5,0.1);

        EditText editText=findViewById(R.id.editText);
        EditText editText2=findViewById(R.id.editText2);
        EditText editText3=findViewById(R.id.editText3);
        EditText editText4=findViewById(R.id.editText4);
        EditText editText5=findViewById(R.id.editText5);
        EditText resultBox=findViewById(R.id.editText6);

        Double uh=Double.parseDouble(editText.getText().toString());
        Double ua=Double.parseDouble(editText2.getText().toString());
        Double us=Double.parseDouble(editText3.getText().toString());
        Double un=Double.parseDouble(editText4.getText().toString());
        Double Dep=Double.parseDouble(editText5.getText().toString());

        DepressionAlgo day=new DepressionAlgo();

        day.setMeanAngryInKB(0.3);
        day.setMeanHappyInKB(0.5);
        day.setMeanNaturalInKB(0.15);
        day.setMeanSadInKB(0.5);
        day.setDepressionPerSample(Dep);
        day.setUserAngryPerDay(ua);
        day.setUserHappyPerDay(uh);
        day.setUserSadPerDay(us);
        day.setUserNaturalPerDay(un);

        double depressionPoint=day.getProbalityToHasDepretionUsingEmotions();

        resultBox.setText(""+depressionPoint);
    }

}
