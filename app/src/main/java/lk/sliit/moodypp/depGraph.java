package lk.sliit.moodypp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class depGraph extends AppCompatActivity {

    LineChartView lineChartView;

   public DatabaseReference users;
   public DatabaseReference userdata;
   public TextView tv;
   public TextView Result;
   int[] pointvalue={10,50,60};
   public FirebaseDatabase fd;
   public String userId;

    public DatabaseReference Fde_Database;
    public DatabaseReference F_deChild;
    public FirebaseAuth mAuth;

    public int val=1;
    public String userName;

    public ArrayList<Integer> a1; //y
    public ArrayList<String> a2; //x
    public int totalDp;

    //array
    public int[] yAxisData;
    public String[] axisData;

    public String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_dep);

        lineChartView = findViewById(R.id.chart2);
        TextView tv = (TextView) findViewById(R.id.textView4);
        TextView Result = (TextView) findViewById(R.id.display2);

        //get userid from firebase
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        fd=FirebaseDatabase.getInstance();
        users=fd.getReference("Users");
        userdata=users.child(userId);

        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userObj=dataSnapshot.getValue(user.class);
                userName=userObj.getCallName();
                Log.i("sra",userName);
                status="Hello "+ userName +" see your Depression Progress result here";
                tv.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //get name from google account
        if(userName == null) {
            userName = mAuth.getCurrentUser().getDisplayName();
        }

        String status1="You don't have depression";
        String status2="today You slightly have depression";
        String status3="My be you have depression";
        String status4="your depression level is high.please met Doctor";

        //firebase connection
        Fde_Database = FirebaseDatabase.getInstance().getReference("DTR");
        F_deChild=Fde_Database.child(userId);

        //check with arraylist
        a1=new ArrayList<>();
        a2=new ArrayList<>();

        //get today
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date=new Date();
        String today= dateFormat.format(date);



        F_deChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    String dateVar=ds.getKey();
                    Double dpVar=ds.getValue(Double.class);
                    int dpvalue=(int)(dpVar*100);

                    a1.add(dpvalue);
                    a2.add(dateVar);

                    if(today.equals(dateVar)){
                        totalDp=dpvalue;
                    }

                    Log.i("madu",dateVar+" "+totalDp);
                    val= val+1;

                    loadGraph();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (totalDp < 30) {
            Result.setText(status1);
        } else if (totalDp < 50) {
            Result.setText(status2);
        } else if (totalDp < 70) {
            Result.setText(status3);
        } else if(totalDp < 100){
            Result.setText(status4);
        } else {
            Result.setText(status4);
        }

    }

    public void loadGraph(){
        Log.i("sra",""+a2.size());

        yAxisData=new int[a1.size()];
        axisData=new String[a2.size()];

        int s;
        for(s=0;s<a1.size();s++){
            yAxisData[s]=a1.get(s);
            axisData[s]=a2.get(s);
        }

        Log.i("sra",""+axisData[0]);

        //String[] axisData = {"09/01", "09/03", "09/05", "09/08", "09/11", "09/13", "09/18", "09/20", "09/21","09/20", "09/23", "09/29","10/02","10/05","10/06","10/09"};
        //int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18,10,20,30,45};

        List yAxisValues=new ArrayList();
        List axisValues=new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#FFFFFF"));



        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for ( int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i,yAxisData[i]));
            // yAxisValues.add(new PointValue(i,new AxisValue(i).setLabel(yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axis = new Axis();
        axis.setName("Time Period");
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#373A3A"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Depression Level");
        yAxis.setTextColor(Color.parseColor("#373A3A"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 300;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
