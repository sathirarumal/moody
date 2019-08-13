package lk.sliit.moodypp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class chatBot extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference databaseReferenceExtraDetails;
    public DatabaseReference childRef;
    public DatabaseReference childRefTR;
    public DatabaseReference childRefDTR;
    public DatabaseReference childRefATR;
    private String userId;
    private String email;
    private FirebaseAuth mAuth;
    private double dtr;
    private double atr;
    EditText dtrEdit;
    EditText atrEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReferenceExtraDetails=firebaseDatabase.getReference("Results");


        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();
        email=user.getEmail();

        childRef=databaseReference.child(userId);

        dtrEdit=findViewById(R.id.dtr);
        atrEdit= findViewById(R.id.atr);

    }

    @Override
    protected void onStart() {
        super.onStart();

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userObj=dataSnapshot.getValue(user.class);

                if(userObj == null){
                    goBasicQuestionPage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goBasicQuestionPage(){
        Intent intent=new Intent(this,basicQuections.class);
        startActivity(intent);
    }

    public void insertTestResults(View view){

        dtr=Double.parseDouble(dtrEdit.getText().toString());
        atr=Double.parseDouble(atrEdit.getText().toString());

        childRefTR=databaseReferenceExtraDetails.child(userId);
        childRefDTR=childRefTR.child("DTR");
        childRefDTR.setValue(dtr);
        childRefATR=childRefTR.child("ATR");
        childRefATR.setValue(atr);

        Toast.makeText(chatBot.this, "ATR,DTR updated",
                Toast.LENGTH_SHORT).show();

    }

}
