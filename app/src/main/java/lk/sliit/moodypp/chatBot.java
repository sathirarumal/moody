package lk.sliit.moodypp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

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

public class chatBot extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference databaseReferenceExtraDetails;
    public DatabaseReference childRef;
    public DatabaseReference childRefTR;
    public DatabaseReference childRefDTR;
    public DatabaseReference childRefATR;
    private String userId;
    private FirebaseAuth mAuth;
    private double dtr;
    private double atr;
    EditText dtrEdit;
    EditText atrEdit;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReferenceExtraDetails=firebaseDatabase.getReference("Results");


        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user != null;
        userId=user.getUid();

        childRef=databaseReference.child(userId);

        dtrEdit=findViewById(R.id.clname);
        atrEdit= findViewById(R.id.age);

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

        /*dtr=Double.parseDouble(dtrEdit.getText().toString());
        atr=Double.parseDouble(atrEdit.getText().toString());

        childRefTR=databaseReferenceExtraDetails.child(userId);
        childRefDTR=childRefTR.child("DTR");
        childRefDTR.setValue(dtr);
        childRefATR=childRefTR.child("ATR");
        childRefATR.setValue(atr);

        Toast.makeText(chatBot.this, "ATR,DTR updated",
                Toast.LENGTH_SHORT).show();*/

        Intent intent=new Intent(this,Calculate.class);
        startActivity(intent);

    }

    public void goSettings(){

        Intent intent = new Intent(this,userDetails.class);
        startActivity(intent);
    }

    public void showPopup(View v) {

        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actions);
        popup.show();
    }

    public void signOut(){
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userDetails:
                goSettings();
                return true;
            case R.id.logout:
                signOut();
                return true;
            default:
                return false;
        }
    }

}
