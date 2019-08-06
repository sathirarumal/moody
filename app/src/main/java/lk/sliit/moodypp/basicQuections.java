package lk.sliit.moodypp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class basicQuections extends AppCompatActivity {

    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private static final String TAG=MainActivity.class.getSimpleName();
    private String Email;
    private String Name;
    private TextView emailText;
    private TextView nameText;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_quections);

        emailText=findViewById(R.id.emailtext);
        nameText=findViewById(R.id.nametext);

        database = FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        Email=user.getEmail();
        Name=user.getDisplayName();

        emailText.setText(Email);
        nameText.setText(Name);

        //getInstance();

    }

    private void getInstance(){
        DatabaseReference myRef = database.getReference("UserEmail");
        myRef.setValue(Email);
    }

    private void onDataChange(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void updateUI(){

    }


    public void signOut(View view){
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
