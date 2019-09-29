package lk.sliit.moodypp;

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
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference childRef;
    private static final String TAG=settings.class.getSimpleName();
    private String callName;
    private String email;
    private String age;
    private String gender;
    private String status;
    private String userId;
    private FirebaseAuth mAuth;
    private Spinner genderSpinner;
    private Spinner statusSpinner;
    private EditText callNameText;
    private EditText ageText;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter2;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private int phoneStatus;
    private int relationStatus;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //layout hide
        userLayout= findViewById(R.id.userDetails);
        userLayout.setVisibility(View.GONE);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        callName=callNameText.getText().toString();
                        age=ageText.getText().toString();
                        status=statusSpinner.getSelectedItem().toString();
                        gender=genderSpinner.getSelectedItem().toString();

                        user userObj=new user(email,callName,age,gender,status);

                        childRef=databaseReference.child(userId);
                        childRef.setValue(userObj);

                        Toast.makeText(settings.this, "All Details updated",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                }
        );


    }

    public void insertBasicData(View view){

        callName=callNameText.getText().toString();
        age=ageText.getText().toString();
        status=statusSpinner.getSelectedItem().toString();
        gender=genderSpinner.getSelectedItem().toString();

        if (status.equals("I take/took medicine for Depression")) {
            type = "depression";
        } else if (status.equals("I take/took medicine for Anxiety")) {
            type = "anxiety";
        }else if(status.equals("I take/took medicine for Both")){
            type = "depression";
        }else {
            type = "don't know";
        }

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("userType",type);
        editor.apply();

        user userObj=new user(email,callName,age,gender,status);

        childRef=databaseReference.child(userId);
        childRef.setValue(userObj);

        Toast.makeText(settings.this, "All Details updated",
                Toast.LENGTH_SHORT).show();

        finish();
        startActivity(getIntent());
    }


    public void loadUserLayout(View view){

        if(userLayout.getVisibility() == View.GONE){

            //userdetails declaretion

            firebaseDatabase= FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference("Users");
            mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
            assert user != null;
            userId=user.getUid();
            email=user.getEmail();
            childRef=databaseReference.child(userId);
            callNameText=findViewById(R.id.clname);
            ageText=findViewById(R.id.age);
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

                    user userObj=dataSnapshot.getValue(user.class);

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
        }else{

            userLayout.setVisibility(View.GONE);
        }

    }

    public void loadSOSLayout(View view){

        if(userLayout.getVisibility() == View.GONE){

            userLayout.setVisibility(View.VISIBLE);
        }else{

            userLayout.setVisibility(View.GONE);
        }

    }

    public void loadStorageLayout(View view){

        if(userLayout.getVisibility() == View.GONE){

            userLayout.setVisibility(View.VISIBLE);
        }else{

            userLayout.setVisibility(View.GONE);
        }

    }

    public void loadPermissionLayout(View view){

        if(userLayout.getVisibility() == View.GONE){

            userLayout.setVisibility(View.VISIBLE);
        }else{

            userLayout.setVisibility(View.GONE);
        }

    }
}
