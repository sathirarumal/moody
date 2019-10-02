package lk.sliit.moodypp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class basicQuections extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference childRef;
    private static final String TAG=MainActivity.class.getSimpleName();
    private String email;
    private String name;
    public String callName;
    private String age;
    private String gender;
    private String status;
    private String userId;
    private TextView heyText;
    private TextView heading;
    private FirebaseAuth mAuth;
    private Spinner genderSpinner;
    private Spinner statusSpinner;
    private EditText callNameText;
    private EditText ageText;
    public String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_quections);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        heyText=findViewById(R.id.hiText);
        heading=findViewById(R.id.head);
        callNameText=findViewById(R.id.clname);
        ageText=findViewById(R.id.age);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        email=user.getEmail();
        name=user.getDisplayName();

        if (name == null){
            heyText.setText("Hey my friend");
        }else{
            heyText.setText("Hey " + name);
        }

        // gender spinner
        genderSpinner = (Spinner) findViewById(R.id.genderspin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.GenderArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // status spinner
        statusSpinner = (Spinner) findViewById(R.id.statusspin);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.StatusArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter2);

    }

    public void insertBasicData(View view){

        callName=callNameText.getText().toString();
        age=ageText.getText().toString();
        status=statusSpinner.getSelectedItem().toString();
        gender=genderSpinner.getSelectedItem().toString();


        if(callName.isEmpty()){
            callNameText.setError("please fill this form to Continue");
        }else if(age.isEmpty()){
            ageText.setError("please fill this form to Continue");
        }else{
       
            if (status.equals("I take/took medicine for Depression")) {
              type = "depression";
            } else if (status.equals("I take/took medicine for Anxiety")) {
              type = "anxiety";
            }else if(status.equals("I take/took medicine for Both")){
              type = "both";
            }else {
              type = "don't know";
            }

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("userType",type);
        editor.putString("sos","Deactivate");
        editor.putString("kb","off");
        editor.putString("disorder","depression");
        editor.apply();

        user userObj=new user(email,callName,age,gender,status);

        childRef=databaseReference.child(userId);
        childRef.setValue(userObj);

        Intent intent=new Intent(this, CheckUser.class);
        startActivity(intent);

        finish();
        }  

    }


    private void updateUI(){

    }



    private void checkFirstLogin(){

    }

}
