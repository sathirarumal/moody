package lk.sliit.moodypp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TestingBackDoor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
    }

    public void calculate(View view){

        //double result=proccessDepressionPerDay(0.3,0.1,0.5,0.1);

        EditText editText=findViewById(R.id.editText);
        EditText editText2=findViewById(R.id.editText2);
        EditText editText3=findViewById(R.id.editText3);
        EditText editText4=findViewById(R.id.editText4);
        EditText editText5=findViewById(R.id.editText5);
        EditText resultBox=findViewById(R.id.editText6);

        Double uh=Double.parseDouble(editText.getText().toString());
        Double ua=Double.parseDouble(editText2.getText().toString());
        Double us=Double.parseDouble(editText3.getText().toString());
        Double un=Double.parseDouble(editText4.getText().toString());
        Double dis=Double.parseDouble(editText5.getText().toString());

        daProbabilityAlgorithm day=new daProbabilityAlgorithm();

        day.setMeanAngryInKB(0.3);
        day.setMeanHappyInKB(0.05);
        day.setMeanNaturalInKB(0.15);
        day.setMeanSadInKB(0.5);

        day.setDisoderPerSample(dis);
        day.setUserAngryPerDay(ua);
        day.setUserHappyPerDay(uh);
        day.setUserSadPerDay(us);
        day.setUserNaturalPerDay(un);

        double disoderPoint=day.getProbalityToHavingDisoderUsingEmotions();

        resultBox.setText(""+disoderPoint);
    }
}
