package lk.sliit.moodypp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userDetails extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference childRef;
    private static final String TAG=MainActivity.class.getSimpleName();
    private String email;
    private String callName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        email=user.getEmail();

        childRef=databaseReference.child(userId);

        callNameText=findViewById(R.id.dtr);
        ageText=findViewById(R.id.atr);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }

    public void insertBasicData(View view){

        callName=callNameText.getText().toString();
        age=ageText.getText().toString();
        status=statusSpinner.getSelectedItem().toString();
        gender=genderSpinner.getSelectedItem().toString();

        user userObj=new user(email,callName,age,gender,status);

        childRef=databaseReference.child(userId);
        childRef.setValue(userObj);

        Toast.makeText(userDetails.this, "All Details updated",
                Toast.LENGTH_SHORT).show();

        finish();
        startActivity(getIntent());
    }





}
