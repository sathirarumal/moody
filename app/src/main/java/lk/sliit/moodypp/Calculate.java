package lk.sliit.moodypp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
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

    public DatabaseReference userEmotionFDayRef;
    public DatabaseReference anxietyFDayRef;
    public DatabaseReference depressionFDayRef;

    public DatabaseReference meanEmotionDepRef;
    public DatabaseReference meanEmotionANXRef;

    public DatabaseReference CountRef;
    public DatabaseReference depCountRef;
    public DatabaseReference anxCountRef;

    public DatabaseReference UserchildRef;
    public DatabaseReference UserEmotionchildRef;
    public DatabaseReference EmotionUserIDchildRef;
    public DatabaseReference MeanChildRef;

    private static final String TAG=Calculate.class.getSimpleName();
    private String userId;
    private FirebaseAuth mAuth;
    private String status;
    private double meanHappyInKB;  //p(H/D)
    private double meanAngryInKB;  //p(A/D)
    private double meanSadInKB;    //p(S/D)
    private double meanNaturalInKB;//p(N/D)
    private double meanFearInKB;   //p(F/D)

    private double TempmeanHappyInKB;  //p(H/D)
    private double TempmeanAngryInKB;  //p(A/D)
    private double TempmeanSadInKB;    //p(S/D)
    private double TempmeanNaturalInKB;//p(N/D)
    private double TempmeanFearInKB;   //p(F/D)


    private EmotionFirstDay emotionFirstDay;
    private double depressionPerSample; //p(D)

    public int hVal;
    public int nVal;
    public int sVal;
    public int fVal;
    public int aVal;

    private double UserHappy;
    private double UserAngry;
    private double UserNatural;
    private double UserFear;
    private double UserSad;

    private String meanFromKB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_emotion);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //depressionPerSample=(double)dataSnapshot.getChildrenCount();


        firebaseDatabase=FirebaseDatabase.getInstance();
        userDetailRef=firebaseDatabase.getReference("Users");

        /*userEmotionFDayRef=firebaseDatabase.getReference("UserEmotionsFistDay");
        depressionFDayRef=meanEmotionDepRef.child("Depression");
        anxietyFDayRef=meanEmotionANXRef.child("Anxiety");*/

        meanEmotionDepRef=firebaseDatabase.getReference("MeanEmotionsOfUsers");
        meanEmotionANXRef=firebaseDatabase.getReference("MeanEmotionsAnxietyUsers");

        CountRef=firebaseDatabase.getReference("PatientCount");
        depCountRef=CountRef.child("DepressionPatientCount");
        anxCountRef=CountRef.child("AnxietyPatientCount");

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        UserchildRef=userDetailRef.child(userId);

        meanFromKB="False";



        setEmotion();
    }

    protected void onStart() {
        super.onStart();

        SharedPreferences sharePref2= PreferenceManager.getDefaultSharedPreferences(this);
        //status = sharePref2.getString("status",null);

        status="nDep";
    }


    public void proccessDataIntoKnowledgeBase(){

        if(status.equals("fDep")) { //when first time depression

            depCountRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int tempCount=dataSnapshot.getValue(Integer.class);
                    int total=tempCount+1;
                    depCountRef.setValue(total);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


            meanEmotionDepRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                MeanEmotionsOfUsers Mean=dataSnapshot.getValue(MeanEmotionsOfUsers.class);
                    if(Mean == null){
                        meanEmotionDepRef.setValue(new MeanEmotionsOfUsers(UserHappy,UserAngry,UserSad,UserNatural,UserFear));
                    }else{
                        TempmeanAngryInKB=(Mean.getMeanAngryInKB() + UserHappy)/2;
                        TempmeanFearInKB=(Mean.getMeanFearInKB() + UserFear)/2;
                        TempmeanHappyInKB=(Mean.getMeanHappyInKB() + UserHappy)/2;
                        TempmeanNaturalInKB=(Mean.getMeanNaturalInKB() + UserNatural)/2;
                        TempmeanSadInKB=(Mean.getMeanSadInKB() + UserSad)/2;

                        meanEmotionDepRef.setValue(new MeanEmotionsOfUsers(TempmeanAngryInKB,TempmeanAngryInKB,TempmeanSadInKB,TempmeanNaturalInKB,TempmeanFearInKB));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else if(status.equals("fAnx")){ //when first time anxiety

            anxCountRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int tempCount=dataSnapshot.getValue(Integer.class);
                    int total=tempCount+1;
                    anxCountRef.setValue(total);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            meanEmotionANXRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    MeanEmotionsOfUsers Mean=dataSnapshot.getValue(MeanEmotionsOfUsers.class);
                    if(Mean == null){
                        meanEmotionDepRef.setValue(new MeanEmotionsOfUsers(UserHappy,UserAngry,UserSad,UserNatural,UserFear));
                    }else{
                        TempmeanAngryInKB=(Mean.getMeanAngryInKB() + UserHappy)/2;
                        TempmeanFearInKB=(Mean.getMeanFearInKB() + UserFear)/2;
                        TempmeanHappyInKB=(Mean.getMeanHappyInKB() + UserHappy)/2;
                        TempmeanNaturalInKB=(Mean.getMeanNaturalInKB() + UserNatural)/2;
                        TempmeanSadInKB=(Mean.getMeanSadInKB() + UserSad)/2;

                        meanEmotionDepRef.setValue(new MeanEmotionsOfUsers(TempmeanAngryInKB,TempmeanAngryInKB,TempmeanSadInKB,TempmeanNaturalInKB,TempmeanFearInKB));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

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
        Double dis=Double.parseDouble(editText5.getText().toString());

        daProbabilityAlgorithm day=new daProbabilityAlgorithm();

        day.setDisoderPerSample(dis);
        day.setUserAngryPerDay(ua);
        day.setUserHappyPerDay(uh);
        day.setUserSadPerDay(us);
        day.setUserNaturalPerDay(un);

        double disoderPoint=day.getProbalityToHavingDisoderUsingEmotions();

        resultBox.setText(""+disoderPoint);
    }

    public void setEmotion(){

        SeekBar hBar= findViewById(R.id.hbar);
        SeekBar nBar= findViewById(R.id.nbar);
        SeekBar sBar= findViewById(R.id.sbar);
        SeekBar fBar= findViewById(R.id.fbar);
        SeekBar aBar= findViewById(R.id.abar);

        hBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hVal=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        nBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 nVal=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 sVal=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        fBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 fVal=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        aBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 aVal=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void cal(View view){

        double total=aVal+hVal+nVal+sVal+fVal;

        UserHappy=hVal/total;
        UserAngry=aVal/total;
        UserFear=fVal/total;
        UserNatural=nVal/total;
        UserSad=sVal/total;


     if(status.equals("nDep") || status.equals("nAnx") ) {

         daProbabilityAlgorithm day = new daProbabilityAlgorithm();

         day.setUserAngryPerDay(UserAngry);
         day.setUserFearPerDay(UserFear);
         day.setUserHappyPerDay(UserHappy);
         day.setUserNaturalPerDay(UserNatural);
         day.setUserSadPerDay(UserSad);

         if (meanFromKB.equals("False")) {
             day.setMeanAngryInKB(0.3);
             day.setMeanHappyInKB(0.05);
             day.setMeanNaturalInKB(0.15);
             day.setMeanSadInKB(0.5);

             if (status.equals("nDep")) {
                 day.setDisoderPerSample(0.8);//get From rashini
             } else if (status.equals("nAnx")) {
                 day.setDisoderPerSample(0.4);
             }

         } else {
             day.setMeanAngryInKB(meanAngryInKB);
             day.setMeanHappyInKB(meanHappyInKB);
             day.setMeanNaturalInKB(meanNaturalInKB);
             day.setMeanSadInKB(meanSadInKB);

             if (status.equals("nDep")) {
                 day.setDisoderPerSample(0.5);
             } else if (status.equals("nAnx")) {
                 day.setDisoderPerSample(0.4);
             }
         }

       double dqResult=day.getProbalityToHavingDisoderUsingEmotions();
       String x=""+dqResult;

         Toast.makeText(Calculate.this, x,
                 Toast.LENGTH_SHORT).show();
     }else{
         proccessDataIntoKnowledgeBase();
     }


    }

}
