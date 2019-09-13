package lk.sliit.moodypp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class BotActivity extends AppCompatActivity {
    public FirebaseDatabase database;
    public DatabaseReference De_Database;
    public DatabaseReference Fde_Database;
    public DatabaseReference An_Database;

    dqSession dqSessionObj;
    fdqSession fdqSessionObj;
    aqSession aqSessionObj;

    private static final String TAG = BotActivity.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;

    private String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;
    private Spinner replySpinner;

    // Java V2
    private SessionsClient sessionsClient;
    private SessionName session;

    public String replyState;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database=FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);

        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this::userSendMessage);

        replySpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.replyArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        replySpinner.setAdapter(adapter);
        replySpinner.setVisibility(View.GONE);
        //replySpinner.setOnItemClickListener();


        queryEditText = findViewById(R.id.queryEditText);

        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        userSendMessage(sendBtn);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
        // Java V2
        initV2Chatbot();


        replySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String spinnerMsg=replySpinner.getSelectedItem().toString();
                queryEditText.setText(spinnerMsg);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("hi").setLanguageCode("en-US")).build();
        new RequestJavaV2Task(BotActivity.this, session, sessionsClient, queryInput).execute();
    }



    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.test_agent_credentialss);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);

        } catch (Exception e) {
            e.printStackTrace();
        } }




    private void userSendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(BotActivity.this, "Please enter your message!", Toast.LENGTH_LONG).show();
        } else {
            showTextView(msg, USER);
            queryEditText.setText("");

           String msgText=checkCode(replyState,msg);
           replyState=null;

            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msgText).setLanguageCode("en-US")).build();
            new RequestJavaV2Task(BotActivity.this, session, sessionsClient, queryInput).execute();

            reset();
        }
    }




    public void botSendMessage(DetectIntentResponse response) {
        if (response != null) {
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            showTextView(botReply, BOT);
            //this function set the code for reply state
            setCode(botReply);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Check your internet connection and please Try again!", BOT);
        }
    }




    private void showTextView(String message, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(BotActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(BotActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }




    public void setCode(String botMsg){

        if (botMsg.equals("oh, sorry to hear that. What made you feel not good today?")){

            replyState="ignoreReply1";
        }
        else if (botMsg.equals("Do you often feel hopelessness or guilty ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="dq1";
        }
        else if (botMsg.equals("Do you feel trouble concentrating or remembering things?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="dq2";
        }
        else if (botMsg.equals("Do you feel trouble making decisions ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="dq3";
        }
        else if (botMsg.equals("Do you loss of interest in things once pleasurable ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="dq4";
        }
        else if (botMsg.equals("Do you have suicidal thoughts ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="dq5";
        }

        //first time depression questions
        else if (botMsg.equals("While you have depression, do you often feel hopelessness or guilty ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="fdq1";
        }
        else if (botMsg.equals("While you have depression, do you feel trouble concentrating or remembering things?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="fdq2";
        }
        else if (botMsg.equals("While you have depression, do you feel trouble making decisions ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="fdq3";
        }
        else if (botMsg.equals("While you have depression, do you loss of interest in things once pleasurable ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="fdq4";
        }
        else if (botMsg.equals("While you have depression, do you have suicidal thoughts ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState ="fdq5";
        }

        //ANXIETY QUESTIONS
        else if (botMsg.equals("Do you experience restlessness on the majority of days ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq1";
        }
        else if (botMsg.equals("Do you waking up in the middle of the night or having trouble falling asleep ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq2";
        }
        else if (botMsg.equals("Do you having tense muscles on most days ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq3";
        }
        else if (botMsg.equals("Do you fearful of being embarrassed or humiliated in front of others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq4";
        }
        else if (botMsg.equals("Do you avoiding certain social events because of these fears ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq5";
        }
        else if (botMsg.equals("Do you worried that you may be judged or scrutinized by others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq6";
        }
        else if (botMsg.equals("Do you worried that you may be judged or scrutinized by others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq7";
        }
        else if (botMsg.equals("Do you feel Agitate in following situations.\n " +
                "using public transportation\n" +
                "Being in open spaces\n" +
                "Being in enclosed spaces\n" +
                "Standing in line or being in a crowd\n" +
                "Being outside of the home alone "))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="aq8";
        }



        //After dq choosing questions
        else if (botMsg.equals("Thanks! I saved your answers.What would you like to do next?"))
        {

            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chooseArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
        }


         //Awsome reson
        else if (botMsg.equals("Wow. I'm so happy to hear that.What make you feel this much happy?"))
        {
            replyState ="awsome_reason";
        }
        }




    public String checkCode(String reply,String msg){

    if(reply != null) {

        if (reply.equals("ignoreReply"))
        {
            return "ignoreReply";
        }
        else if (reply.equals("awsome_reason"))
        {
            return "awsome_reason";
        }
        else if (reply.equals("ignoreReply1"))
        {
            return "ignoreReply1";
        }

//depression reply state checker
        else if (reply.equals("dq1"))
        {
            dqSessionObj= new dqSession();
            dqSessionObj.setDq1(ans_points(msg));
           /* String aa = "" +dqSessionObj.getDq1();
            Toast.makeText(BotActivity.this, aa, Toast.LENGTH_LONG).show(); */
            return "dq1";
        }
        else if (reply.equals("dq2"))
        {
            dqSessionObj.setDq2(ans_points(msg));
            return "dq2";
        }
        else if (reply.equals("dq3"))
        {
            dqSessionObj.setDq3(ans_points(msg));
            return "dq3";
        }
        else if (reply.equals("dq4"))
        {
            dqSessionObj.setDq4(ans_points(msg));
            return "dq4";
        }
        else if (reply.equals("dq5"))
        {
            dqSessionObj.setDq5(ans_points(msg));

            double dqpoint = dqSessionObj.GetDqPointTotal();
            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
            Date date=new Date();
            String today=""+  date  ; //dateFormat.format(date);

            De_Database = FirebaseDatabase.getInstance().getReference("DTR");
            De_Database.child(today).setValue(dqpoint);
            //dqSessionObj.setDq1(msg);
            return "dq5";
        }


//first depression reply state checker
        else if (reply.equals("fdq1"))
        {
            fdqSessionObj= new fdqSession();
            fdqSessionObj.setFdq1(ans_points(msg));
           /* String aa = "" +dqSessionObj.getDq1();
            Toast.makeText(BotActivity.this, aa, Toast.LENGTH_LONG).show(); */
            return "fdq1";
        }
        else if (reply.equals("fdq2"))
        {
            fdqSessionObj.setFdq2(ans_points(msg));
            return "fdq2";
        }
        else if (reply.equals("fdq3"))
        {
            fdqSessionObj.setFdq3(ans_points(msg));
            return "fdq3";
        }
        else if (reply.equals("fdq4"))
        {
            fdqSessionObj.setFdq4(ans_points(msg));
            return "fdq4";
        }
        else if (reply.equals("fdq5"))
        {
            fdqSessionObj.setFdq5(ans_points(msg));

            Fde_Database = FirebaseDatabase.getInstance().getReference("FirstTestResult");
            Fde_Database.child("Depression").setValue(fdqSessionObj);

            return "fdq5";
        }

        //Anxiety reply state checker
        else if (reply.equals("aq1"))
        {
            aqSessionObj = new aqSession();
            aqSessionObj.setAq1(ans_points(msg));
           /* String aa = "" +dqSessionObj.getDq1();
            Toast.makeText(BotActivity.this, aa, Toast.LENGTH_LONG).show(); */
            return "aq1";
        }
        else if (reply.equals("aq2"))
        {
            aqSessionObj.setAq2(ans_points(msg));
            return "aq2";
        }
        else if (reply.equals("aq3"))
        {
            aqSessionObj.setAq3(ans_points(msg));
            return "aq3";
        }
        else if (reply.equals("aq4"))
        {
            aqSessionObj.setAq4(ans_points(msg));
            return "aq4";
        }
        else if (reply.equals("aq5"))
        {
            aqSessionObj.setAq5(ans_points(msg));
            return "aq5";
        }
        else if (reply.equals("aq6"))
        {
            aqSessionObj.setAq6(ans_points(msg));
            return "aq6";
        }
        else if (reply.equals("aq7"))
        {
            aqSessionObj.setAq7(ans_points(msg));

            double aqpoint = aqSessionObj.getAqPointTotal();
            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
            Date date=new Date();
            String today=""+  date  ; //dateFormat.format(date);

            An_Database = FirebaseDatabase.getInstance().getReference("ATR");
            An_Database.child(today).setValue(aqpoint);
            //dqSessionObj.setDq1(msg);
            return "aq7";
        }
        else if (reply.equals("aq8"))
        {
            aqSessionObj.setAq5(ans_points(msg));
            return "aq8";
        }



    }
        return msg;


    }

    public void reset()
    {
        queryEditText.setVisibility(View.VISIBLE);
        replySpinner.setVisibility(View.GONE);
        queryEditText.setText(" ");
    }

    public double ans_points(String msg)
    {
        if (msg.equals("Not at all"))
        {
            return  0;
        }
        else if(msg.equals("Several days"))
        {
            return  1;

        }
        else if(msg.equals("More than half of the days"))
        {
            return  2;

        }
        else if(msg.equals("Nearly everyday"))
        {
            return  5;

        }

        return 10;
    }


}