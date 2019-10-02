package lk.sliit.moodypp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings extends AppCompatActivity {

    public LinearLayout userLayout;
    public LinearLayout sosLayout;
    public LinearLayout priorityLayout;
    public LinearLayout kbLayout;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference childRef;
    public DatabaseReference databaseReference2;
    public DatabaseReference childRef2;
    private static final String TAG=settings.class.getSimpleName();

    private String callName;
    private String email;
    private String age;
    private String gender;
    private String status;
    private String userId;
    private String mpn;
    private String tpn;
    private String tName;
    private String sosMethod;


    private FirebaseAuth mAuth;
    private Spinner genderSpinner;
    private Spinner statusSpinner;
    private Spinner sosSpinner;
    private Spinner disoderSpinner;
    private Spinner kbSpinner;
    private EditText callNameText;
    private EditText ageText;
    private EditText mpntext;
    private EditText tpntext;
    private EditText tNametext;

    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter2;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    public String type;
    public SharedPreferences.Editor editor;

    public String sos;
    public String kb;
    public String disorder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        sos=sharePref.getString("sos",null);
        kb=sharePref.getString("kb",null);
        disorder=sharePref.getString("disorder",null);
        type=sharePref.getString("userType",null);

        if(type.equals("depression") || type.equals("anxiety")){
            TextView disorderTV=findViewById(R.id.priority);
            disorderTV.setVisibility(View.GONE);
        }


        editor = sharePref.edit();

        //layout hide
        userLayout= findViewById(R.id.userDetails);
        userLayout.setVisibility(View.GONE);
        sosLayout= findViewById(R.id.sosmenu);
        sosLayout.setVisibility(View.GONE);
        priorityLayout =findViewById(R.id.disorder);
        priorityLayout.setVisibility(View.GONE);
        kbLayout=findViewById(R.id.kbmenu);
        kbLayout.setVisibility(View.GONE);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                     if(userLayout.getVisibility() == View.VISIBLE) {
                         callName = callNameText.getText().toString();
                         age = ageText.getText().toString();
                         status = statusSpinner.getSelectedItem().toString();
                         gender = genderSpinner.getSelectedItem().toString();

                         if (status.equals("I take/took medicine for Depression")) {
                             type = "depression";
                         } else if (status.equals("I take/took medicine for Anxiety")) {
                             type = "anxiety";
                         } else if (status.equals("I take/took medicine for Both")) {
                             type = "both";
                         } else {
                             type = "don't know";
                         }

                         //save User Type
                         editor.putString("userType", type);
                         editor.apply();

                         user userObj = new user(email, callName, age, gender, status);

                         childRef = databaseReference.child(userId);
                         childRef.setValue(userObj);
                         //basic details updated
                     }else if (sosLayout.getVisibility() == View.VISIBLE) {

                         mpn = mpntext.getText().toString();
                         tpn = tpntext.getText().toString();
                         tName = tNametext.getText().toString();
                         sosMethod = sosSpinner.getSelectedItem().toString();

                         editor.putString("sos", sosMethod);
                         editor.apply();
                         SOS sosObj = new SOS(mpn, tpn, tName, sosMethod);

                         databaseReference2 = firebaseDatabase.getReference("SOS");
                         childRef2 = databaseReference2.child(userId);
                         childRef2.setValue(sosObj);
                         //sos details updated
                     }else if(kbLayout.getVisibility() == View.VISIBLE) {

                         String kbtext = kbSpinner.getSelectedItem().toString();
                         editor.putString("kb", kbtext);
                         editor.apply();
                         //kb data updated
                     }else if (priorityLayout.getVisibility() == View.VISIBLE) {
                         String diso = disoderSpinner.getSelectedItem().toString();
                         editor.putString("disorder", diso);
                         editor.apply();
                         //disorder updated
                     }
                        Toast.makeText(settings.this, "All Details updated",
                                Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                    }
                }
        );


    }


    public void loadUserLayout(View view){

        if(userLayout.getVisibility() == View.GONE) {

            //userdetails declaretion

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Users");
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            userId = user.getUid();
            email = user.getEmail();
            childRef = databaseReference.child(userId);
            callNameText = findViewById(R.id.clname);
            ageText = findViewById(R.id.age);
            // gender spinner
            genderSpinner = (Spinner) findViewById(R.id.genderspin);
            adapter = ArrayAdapter.createFromResource(this, R.array.GenderArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(adapter);
            // status spinner
            statusSpinner = (Spinner) findViewById(R.id.statusspin);
            adapter2 = ArrayAdapter.createFromResource(this, R.array.StatusArray, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(adapter2);


            //load user details

            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    user userObj = dataSnapshot.getValue(user.class);

                    callNameText.setText(userObj.getCallName());
                    ageText.setText(userObj.getAge());

                    int spinnerPosition = adapter.getPosition(userObj.getGender());
                    genderSpinner.setSelection(spinnerPosition);
                    int spinnerPosition2 = adapter2.getPosition(userObj.getStatus());
                    statusSpinner.setSelection(spinnerPosition2);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            userLayout.setVisibility(View.VISIBLE);
        }else {
            userLayout.setVisibility(View.GONE);
        }
    }

    public void loadSOSLayout(View view){

        if(sosLayout.getVisibility() == View.GONE){

            firebaseDatabase= FirebaseDatabase.getInstance();
            databaseReference2=firebaseDatabase.getReference("SOS");
            mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            assert user != null;
            userId=user.getUid();
            email=user.getEmail();
            childRef2=databaseReference2.child(userId);

            mpntext=findViewById(R.id.pnumber);
            tpntext=findViewById(R.id.Tpnumber);
            tNametext=findViewById(R.id.tname);

            childRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SOS sosObject=dataSnapshot.getValue(SOS.class);

                    if(sosObject != null) {
                        mpntext.setText(sosObject.getMyNo());
                        tpntext.setText(sosObject.getTrustednumber());
                        tNametext.setText(sosObject.getTrustedname());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            sosSpinner = (Spinner) findViewById(R.id.sosAction);
            adapter = ArrayAdapter.createFromResource(this, R.array.sosOption, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sosSpinner.setAdapter(adapter);

            int spinnerPosition = adapter.getPosition(sos);
            sosSpinner.setSelection(spinnerPosition);

            sosLayout.setVisibility(View.VISIBLE);
        }else {
            sosLayout.setVisibility(View.GONE);
        }

    }

    public void loadDisorderLayout(View view){

        if(priorityLayout.getVisibility() == View.GONE) {

            disoderSpinner = (Spinner) findViewById(R.id.disSpinner);
            adapter = ArrayAdapter.createFromResource(this, R.array.deseaseArray, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            disoderSpinner.setAdapter(adapter);

            int spinnerPosition = adapter.getPosition(disorder);
            disoderSpinner.setSelection(spinnerPosition);

            priorityLayout.setVisibility(View.VISIBLE);
        }else {
            priorityLayout.setVisibility(View.GONE);
        }

    }

    public void loadKBLayout(View view){

        if(kbLayout.getVisibility() == View.GONE){

            kbSpinner = (Spinner) findViewById(R.id.kbSpinner);
            adapter = ArrayAdapter.createFromResource(this, R.array.onOff, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kbSpinner.setAdapter(adapter);

            int spinnerPosition = adapter.getPosition(kb);
            kbSpinner.setSelection(spinnerPosition);

            kbLayout.setVisibility(View.VISIBLE);
        }else {
            kbLayout.setVisibility(View.GONE);
        }

    }

    public void resetDefault(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset");
        builder.setMessage("You are about to reset settings to default. Do you want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "not affected", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();

    }

    public void reset(){

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("sos","Deactivate");
        editor.putString("kb","off");
        editor.putString("disorder","depression");
        editor.apply();
    }
}
