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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public DatabaseReference DTR_Reference;
    public DatabaseReference ATR_Reference;


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
    private double pointFromChatTest;

    public SeekBar hBar;
    public SeekBar nBar;
    public SeekBar sBar;
    public SeekBar fBar;
    public SeekBar aBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_emotion);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        aVal=1;
        sVal=1;
        nVal=1;
        fVal=1;
        hVal=1;

        firebaseDatabase=FirebaseDatabase.getInstance();
        userDetailRef=firebaseDatabase.getReference("Users");

        /*userEmotionFDayRef=firebaseDatabase.getReference("UserEmotionsFistDay");
        depressionFDayRef=meanEmotionDepRef.child("Depression");
        anxietyFDayRef=meanEmotionANXRef.child("Anxiety");*/

        meanEmotionDepRef=firebaseDatabase.getReference("MeanEmotionsOfDepressionUsers");
        meanEmotionANXRef=firebaseDatabase.getReference("MeanEmotionsOfAnxietyUsers");

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        UserchildRef=userDetailRef.child(userId);

        setEmotion();

    }

    protected void onStart() {
        super.onStart();

        SharedPreferences sharePref2= PreferenceManager.getDefaultSharedPreferences(this);
        status = sharePref2.getString("status",null);
        meanFromKB = sharePref2.getString("kb",null);
        float point = sharePref2.getFloat("point",0);

        pointFromChatTest=(double)point;

      if(status.equals("nDep")) {
          meanEmotionDepRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                  MeanEmotionsOfUsers Mean = dataSnapshot.getValue(MeanEmotionsOfUsers.class);
                  if (Mean != null) {

                        meanHappyInKB =Mean.meanHappyInKB;
                        meanAngryInKB =Mean.meanAngryInKB;
                        meanSadInKB =Mean.meanSadInKB;
                        meanNaturalInKB =Mean.meanNaturalInKB;
                        meanFearInKB =Mean.meanFearInKB;

                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }else if(status.equals("nAnx")) {
          meanEmotionANXRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                  MeanEmotionsOfUsers Mean = dataSnapshot.getValue(MeanEmotionsOfUsers.class);
                  if (Mean != null) {

                      meanHappyInKB =Mean.meanHappyInKB;
                      meanAngryInKB =Mean.meanAngryInKB;
                      meanSadInKB =Mean.meanSadInKB;
                      meanNaturalInKB =Mean.meanNaturalInKB;
                      meanFearInKB =Mean.meanFearInKB;
                  }

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }
    }


    public void proccessDataIntoKnowledgeBase(){

        if(status.equals("fDep")) { //when first time depression


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

                        meanEmotionDepRef.setValue(new MeanEmotionsOfUsers(TempmeanHappyInKB,TempmeanAngryInKB,TempmeanSadInKB,TempmeanNaturalInKB,TempmeanFearInKB));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else if(status.equals("fAnx")){ //when first time anxiety


            meanEmotionANXRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    MeanEmotionsOfUsers Mean=dataSnapshot.getValue(MeanEmotionsOfUsers.class);
                    if(Mean == null){
                        meanEmotionANXRef.setValue(new MeanEmotionsOfUsers(UserHappy,UserAngry,UserSad,UserNatural,UserFear));
                    }else{
                        TempmeanAngryInKB=(Mean.getMeanAngryInKB() + UserHappy)/2;
                        TempmeanFearInKB=(Mean.getMeanFearInKB() + UserFear)/2;
                        TempmeanHappyInKB=(Mean.getMeanHappyInKB() + UserHappy)/2;
                        TempmeanNaturalInKB=(Mean.getMeanNaturalInKB() + UserNatural)/2;
                        TempmeanSadInKB=(Mean.getMeanSadInKB() + UserSad)/2;

                        meanEmotionANXRef.setValue(new MeanEmotionsOfUsers(TempmeanHappyInKB,TempmeanAngryInKB,TempmeanSadInKB,TempmeanNaturalInKB,TempmeanFearInKB));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        finish();
    }



    public void setEmotion(){

         hBar= findViewById(R.id.hbar);
         nBar= findViewById(R.id.nbar);
         sBar= findViewById(R.id.sbar);
         fBar= findViewById(R.id.fbar);
         aBar= findViewById(R.id.abar);

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

         if (meanFromKB.equals("off")) {

             if(status.equals("nDep")) {

                 day.setMeanAngryInKB(0.054);
                 day.setMeanHappyInKB(0.05);
                 day.setMeanNaturalInKB(0.1);
                 day.setMeanSadInKB(0.5);
                 day.setMeanFearInKB(0.05);

                 day.setDisoderPerSample(0.35);

             }else if (status.equals("nAnx")){

                 day.setMeanAngryInKB(0.2);
                 day.setMeanHappyInKB(0.05);
                 day.setMeanNaturalInKB(0.1);
                 day.setMeanSadInKB(0.4);
                 day.setMeanFearInKB(0.05);

                 day.setDisoderPerSample(0.35);
             }

         } else {

             day.setMeanAngryInKB(meanAngryInKB);
             day.setMeanHappyInKB(meanHappyInKB);
             day.setMeanNaturalInKB(meanNaturalInKB);
             day.setMeanSadInKB(meanSadInKB);
             day.setMeanFearInKB(meanFearInKB);

             day.setDisoderPerSample(pointFromChatTest);
         }

    //process Probability and send values to fire base

         if(status.equals("nDep")) {
             double dqResult = day.getProbalityToHavingDisoderUsingEmotions() * 2;
             double result = (dqResult+pointFromChatTest)/2;

             DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
             Date date=new Date();
             String today= dateFormat.format(date);

             DTR_Reference = FirebaseDatabase.getInstance().getReference("DTR");
             DTR_Reference.child(userId).child(today).setValue(result);

             /*Toast.makeText(Calculate.this, x,
                     Toast.LENGTH_SHORT).show();*/
         }else if(status.equals("nAnx")){

             double aqResult = day.getProbalityToHavingDisoderUsingEmotions() * 2;
             double result = (aqResult+pointFromChatTest)/2;

             Toast.makeText(Calculate.this, ""+aqResult,
                     Toast.LENGTH_SHORT).show();

             DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
             Date date=new Date();
             String today= dateFormat.format(date);

             ATR_Reference = FirebaseDatabase.getInstance().getReference("ATR");
             ATR_Reference.child(userId).child(today).setValue(result);
         }

        finish();

     }else{
         proccessDataIntoKnowledgeBase();

     }


    }

}
