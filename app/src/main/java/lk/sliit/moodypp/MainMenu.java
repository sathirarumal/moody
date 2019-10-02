package lk.sliit.moodypp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String userId;

    //rashini

    public FirebaseDatabase database;
    public DatabaseReference De_Database;
    public DatabaseReference Fde_Database;
    public DatabaseReference An_Database;
    public DatabaseReference Fan_Database;
    public DatabaseReference F_deChild;
    public DatabaseReference F_anChild;
    public DatabaseReference userDetailRef;
    public DatabaseReference userIdRef;

    dqSession dqSessionObj;
    fdqSession fdqSessionObj;
    aqSession aqSessionObj;

    private static final String TAG = MainMenu.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;

    private String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;
    private Spinner replySpinner;
    private ScrollView scrollview;

    // Java V2
    private SessionsClient sessionsClient;
    private SessionName session;

    public String replyState;
    public botTrainer bt;
    public String type;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //fireBase auth
        mAuth= FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        //using SharedPreference
        SharedPreferences sharePref2= PreferenceManager.getDefaultSharedPreferences(this);
        type= sharePref2.getString("userType",null);

        //Log.i("child",type);



     /////////////////////////////////////////////////////////////ONCREATE Beg//////////////////////////////////////////////////////////////////rashini's code

        scrollview = findViewById(R.id.chatScrollView);
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
                        break;   }
            }
            return false;
        });


        initV2Chatbot();
        onStartingPoint();
    }


    protected void onStartingPoint() {


        Log.i("child","onstart Start");

        Fde_Database = FirebaseDatabase.getInstance().getReference("FirstDEPRESSIONTestResult");
        F_deChild=Fde_Database.child(userId);
        Fan_Database = FirebaseDatabase.getInstance().getReference("FirstANXIETYTestResult");
        F_anChild=Fan_Database.child(userId);

        if(type.equals("depression")) {
            F_deChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fdqSession fdqObj = dataSnapshot.getValue(fdqSession.class);
                    if(fdqObj == null){
                        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("FirstHiDep").setLanguageCode("en-US")).build();
                        new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();

                    }else {
                        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("hi").setLanguageCode("en-US")).build();
                        new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(type.equals("anxiety")){

            Log.i("child","anxiety checked");
            F_anChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    faqSession faqObj = dataSnapshot.getValue(faqSession.class);
                    if(faqObj == null){
                        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("FirstHiAnx").setLanguageCode("en-US")).build();
                        new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();

                    }else {
                        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("hi").setLanguageCode("en-US")).build();
                        new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(type.equals("both")){

            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("both123").setLanguageCode("en-US")).build();
            new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();

        }else if(type.equals("don't know")){
            //send this to settings to select
            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("unknown123").setLanguageCode("en-US")).build();
            new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();
        }
    }
    ///////////////////////////////////////////////////////////////// end //////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dep) {
            goDep();
        } else if (id == R.id.anx) {

        } else if (id == R.id.rel) {

        } else if (id == R.id.set) {
            goSettings();
        } else if (id == R.id.sout) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signOut(){
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void goSettings(){
        Intent intent=new Intent(this,settings.class);
        startActivity(intent);
    }

    public void goDep(){
        Intent intent=new Intent(this,Calculate.class);
        startActivity(intent);
    }















    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////rashini

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
        String msg;
        //change value from textEditor to spinner
        if(replySpinner.getVisibility() == View.VISIBLE){
            msg=replySpinner.getSelectedItem().toString();
        }else {
             msg = queryEditText.getText().toString();
        }


        if (msg.trim().isEmpty()) {
            Toast.makeText(MainMenu.this, "Please enter your message!", Toast.LENGTH_LONG).show();

        }else if(msg.equals("dog")) {
          Intent i=new Intent(this,TestingBackDoor.class);
          startActivity(i);
        }else{
            showTextView(msg, USER);
            queryEditText.setText("");

            String msgText=checkCode(replyState,msg);
            replyState=null;

            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msgText).setLanguageCode("en-US")).build();
            new RequestJavaV2Task(MainMenu.this, session, sessionsClient, queryInput).execute();

            reset();
        }
    }


    public void botSendMessage(DetectIntentResponse response) {
        if (response != null) {
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d(TAG, "V2 Bot Reply: " + botReply);

            if (botReply.equals("")) {
                showTextView("Sorry i can't understand.", BOT); }
            else {

                showTextView(botReply, BOT);
                //this function set the `code for reply state
                setCode(botReply);
            }
        }
        else {
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
        LayoutInflater inflater = LayoutInflater.from(MainMenu.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainMenu.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }


    public void setCode(String botMsg){

        if (botMsg.equals("oh, sorry to hear that. What made you feel not good today?")){

            replyState=type;
        }
        else if (botMsg.equals("Do you often feel hopelessness or guilty ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="dq1";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

            /*InputMethodManager imm = (InputMethodManager) getSystemService(MainMenu.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);*/


        }
        else if (botMsg.equals("Do you feel trouble concentrating or remembering things?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="dq2";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you feel trouble making decisions ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="dq3";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you loss of interest in things once pleasurable ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="dq4";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you have suicidal thoughts ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="dq5";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //first time depression questions
        else if (botMsg.equals("While you have depression, do you often feel hopelessness or guilty ?"))
        {
            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.replyArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            replyState="fdq1";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have depression, do you feel trouble concentrating or remembering things?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="fdq2";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have depression, do you feel trouble making decisions ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="fdq3";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have depression, do you loss of interest in things once pleasurable ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="fdq4";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have depression, do you have suicidal thoughts ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="fdq5";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //ANXIETY QUESTIONS
        else if (botMsg.equals("Do you experience restlessness on the majority of days ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq1";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you waking up in the middle of the night or having trouble falling asleep ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq2";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you having tense muscles on most days ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq3";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you fearful of being embarrassed or humiliated in front of others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq4";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you avoiding certain social events because of these fears ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq5";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("Do you worried that you may be judged or scrutinized by others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq6";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }

        else if (botMsg.equals("Do you feel Agitate while using public transportation or Being in open spaces or Being in enclosed spaces or Standing in line or being in a crowd or Being outside of the home alone."))
        {

            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="aq7";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //first time anxiety questions
        else if (botMsg.equals("While you have anxiety, do you experience restlessness on the majority of days ?"))
        {
            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.replyArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState="faq1";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you waking up in the middle of the night or having trouble falling asleep ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq2";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you having tense muscles on most days ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq3";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you fearful of being embarrassed or humiliated in front of others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq4";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you avoiding certain social events because of these fears ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq5";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you worried that you may be judged or scrutinized by others ?"))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq6";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
        else if (botMsg.equals("While you have anxiety, do you feel Agitate while using public transportation or Being in open spaces or Being in enclosed spaces or Standing in line or being in a crowd or Being outside of the home alone."))
        {
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
            replyState ="faq7";
            scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        //After dq choosing questions
        else if (botMsg.equals("Thanks! I saved your answers.What would you like to do next?"))
        {
            botTrainer bt = new botTrainer();
            String State=type;

            if(State.equals("depression")) {

                replySpinner = findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chooseArrayDep, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                replySpinner.setAdapter(adapter);
                queryEditText.setVisibility(View.GONE);
                replySpinner.setVisibility(View.VISIBLE);

            }else {

                replySpinner = findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chooseArrayAnx, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                replySpinner.setAdapter(adapter);
                queryEditText.setVisibility(View.GONE);
                replySpinner.setVisibility(View.VISIBLE);

            }
        }

        //Awsome reson
        else if (botMsg.equals("Wow. I'm so happy to hear that.What make you feel this much happy?"))
        {
            replyState ="awsome_reason";
        }
        else if (botMsg.equals("Please select a priority disorder to continue"))
        {
            Intent intent= new Intent(this,settings.class);
            startActivity(intent);
        }
        else if (botMsg.equals("Hello welcome.. i'm MOODY.Seems like you have experience of both depression and anxiety. For further improvements of app please answer following questions before starting."))
        {
            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.firstdDeseaseArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);

            String x=replySpinner.getSelectedItem().toString();
            if(x.equals("Start with anxiety questions")){
                replyState="go to anxiety";
            }else{
                replyState="go to depression";
            }

        }

        else if(botMsg.equals("Hello welcome.. i'm MOODY.Seems like you have experience of depression. For further improvements of app please answer following questions before starting."))
        {
            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.firstRoundDeQue, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
        }

        else if(botMsg.equals("Hello welcome.. i'm MOODY.Seems like you have experience of anxiety. For further improvements of app please answer following questions before starting."))
        {
            replySpinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.firstRoundAnQue, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            replySpinner.setAdapter(adapter);
            queryEditText.setVisibility(View.GONE);
            replySpinner.setVisibility(View.VISIBLE);
        }

    }





    public String checkCode(String reply,String msg){

        if(reply != null) {

            botTrainer bt = new botTrainer();

            if (reply.equals("depression"))
            {
                return "depression";
            }
            else if (reply.equals("anxiety"))
            {
                return "anxiety";
            }
            else if (reply.equals("go to depression"))
            {
                return "gtd";
            }
            else if (reply.equals("awsome_reason"))
            {
                return "awsome_reason";
            }

/////////////////////////////////////////depression questions reply state checker
            else if (reply.equals("dq1"))
            {
                dqSessionObj= new dqSession();
                dqSessionObj.setDq1(bt.ansPoint(msg,"low"));

                return "dq1";
            }
            else if (reply.equals("dq2"))
            {
                dqSessionObj.setDq2(bt.ansPoint(msg, "low"));
                return "dq2";
            }
            else if (reply.equals("dq3"))
            {
                dqSessionObj.setDq3(bt.ansPoint(msg, "low"));
                return "dq3";
            }
            else if (reply.equals("dq4"))
            {
                dqSessionObj.setDq4(bt.ansPoint(msg, "low"));
                return "dq4";
            }
            else if (reply.equals("dq5"))
            {
                dqSessionObj.setDq5(bt.ansPoint(msg, "high"));

                double dqPoint = dqSessionObj.GetDqPointPercentage();

                Toast.makeText(MainMenu.this, ""+dqPoint, Toast.LENGTH_LONG).show();

                SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("status","nDep");
                editor.putFloat("point",(float) dqPoint);
                editor.apply();

                Intent intent=new Intent(this,Calculate.class);
                startActivity(intent);

                return "dq5";
            }


//first depression reply state checker
            else if (reply.equals("fdq1"))
            {
                fdqSessionObj= new fdqSession();
                fdqSessionObj.setFdq1(bt.first_time_ansPoint(msg));
           /* String aa = "" +dqSessionObj.getDq1();
            Toast.makeText(BotActivity.this, aa, Toast.LENGTH_LONG).show(); */
                return "fdq1";
            }
            else if (reply.equals("fdq2"))
            {
                fdqSessionObj.setFdq2(bt.first_time_ansPoint(msg));
                return "fdq2";
            }
            else if (reply.equals("fdq3"))
            {
                fdqSessionObj.setFdq3(bt.first_time_ansPoint(msg));
                return "fdq3";
            }
            else if (reply.equals("fdq4"))
            {
                fdqSessionObj.setFdq4(bt.first_time_ansPoint(msg));
                return "fdq4";
            }
            else if (reply.equals("fdq5"))
            {
                fdqSessionObj.setFdq5(bt.first_time_ansPoint(msg));

                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date=new Date();
                String today= dateFormat.format(date);

                Fde_Database.child(userId).child(today).setValue(fdqSessionObj);

                SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("status","fDep");
                editor.apply();

                Intent intent=new Intent(this,Calculate.class);
                startActivity(intent);

                return "fdq5";
            }


 //Anxiety reply state checker
            else if (reply.equals("aq1"))
            {
                aqSessionObj = new aqSession();
                aqSessionObj.setAq1(bt.ansPoint(msg, "low"));
                return "aq1";
            }
            else if (reply.equals("aq2"))
            {
                aqSessionObj.setAq2(bt.ansPoint(msg, "low"));
                return "aq2";
            }
            else if (reply.equals("aq3"))
            {
                aqSessionObj.setAq3(bt.ansPoint(msg, "low"));
                return "aq3";
            }
            else if (reply.equals("aq4"))
            {
                aqSessionObj.setAq4(bt.ansPoint(msg, "low"));
                return "aq4";
            }
            else if (reply.equals("aq5"))
            {
                aqSessionObj.setAq5(bt.ansPoint(msg, "low"));
                return "aq5";
            }
            else if (reply.equals("aq6"))
            {
                aqSessionObj.setAq6(bt.ansPoint(msg, "low"));
                return "aq6";
            }
            else if (reply.equals("aq7"))
            {
                aqSessionObj.setAq7(bt.ansPoint(msg, "high"));

                double aqpoint = aqSessionObj.getAqPointTotal();

               /* Toast.makeText(MainMenu.this, ""+aqpoint, Toast.LENGTH_LONG).show();*/

                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date=new Date();
                String today= dateFormat.format(date);

                SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("status","nAnx");
                editor.putFloat("point",(float) aqpoint);
                editor.apply();

                Intent intent =new Intent(this,Calculate.class);
                startActivity(intent);

                return "aq7";
            }

            //First time anxiety questions
            else if (reply.equals("faq1"))
            {
                fdqSessionObj= new fdqSession();
                fdqSessionObj.setFdq1(bt.first_time_ansPoint(msg));
                return "faq1";
            }
            else if (reply.equals("faq2"))
            {
                fdqSessionObj.setFdq2(bt.first_time_ansPoint(msg));
                return "faq2";
            }
            else if (reply.equals("faq3"))
            {
                fdqSessionObj.setFdq3(bt.first_time_ansPoint(msg));
                return "faq3";
            }
            else if (reply.equals("faq4"))
            {
                fdqSessionObj.setFdq4(bt.first_time_ansPoint(msg));
                return "faq4";
            }
            else if (reply.equals("faq5"))
            {
                fdqSessionObj.setFdq4(bt.first_time_ansPoint(msg));
                return "faq5";
            }
            else if (reply.equals("faq6"))
            {
                fdqSessionObj.setFdq4(bt.first_time_ansPoint(msg));
                return "faq6";
            }
            else if (reply.equals("faq7"))
            {
                fdqSessionObj.setFdq5(bt.first_time_ansPoint(msg));

                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date=new Date();
                String today= dateFormat.format(date);


                Fan_Database.child(userId).child(today).setValue(fdqSessionObj);

                SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("status","fAnx");
                editor.apply();

                Intent intent2=new Intent(this,Calculate.class);
                startActivity(intent2);

                return "faq7";
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


    public String checkIllnessType() {

        userDetailRef = FirebaseDatabase.getInstance().getReference("Users");
        userIdRef = userDetailRef.child(userId);
        userIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user userObj = dataSnapshot.getValue(user.class);
                String status = userObj.getStatus();
                Log.i("child",String.valueOf(status));

                if(status == null){
                    type="depression";
                }else if (status.equals("I take/taken medicine for Depression")) {
                    type="depression";
                } else if (status.equals("I take/taken medicine for Anxiety")) {
                    type="anxiety";
                    Log.i("child",type);
                }else {
                    type="depression";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return type;
    }

////////////////////////////////////////////////////////////////////////////////////////



}
