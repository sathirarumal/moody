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

public class anxietyGraph extends AppCompatActivity {

    LineChartView chartanxiety;
    public TextView textview;
    public TextView textmsg;

    //String[] Xaxis = {"09/05", "09/07", "09/09", "09/16", "09/19", "09/22", "09/30"};
    //int[] Yaxis = {10, 20, 50, 40, 90, 10, 30};
    public String status_anx;

    private FirebaseDatabase fA;
    private DatabaseReference users;
    private DatabaseReference userdata;
    private String userId;

    private FirebaseAuth mAuth;
    private DatabaseReference Anx_db;
    private DatabaseReference Anx_child;
    private String userName;
    private int TotalAx;


    private ArrayList<Integer> a1; //y
    private ArrayList<String>a2; //x

    //array
    private int[] Yaxis;
    private String[] Xaxis;
    private int val=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_anx);

        chartanxiety = findViewById(R.id.anxietygraph);
        TextView textView = (TextView) findViewById(R.id.welcomemsg);
        TextView textmsg=(TextView)findViewById(R.id.display2);

        //get userid
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        //firebase connection
        fA=FirebaseDatabase.getInstance();
        users=fA.getReference("Users");
        userdata=users.child(userId);

        //get ATR
        Anx_db = fA.getReference("ATR");
        Anx_child = Anx_db.child(userId);

        //get name from google account
        if(userName==null){
            userName =mAuth.getCurrentUser().getDisplayName();
        }

        //status
        String status1="you don't have Anxiety";
        String status2="you have Anxiety";
        String status3="your anxiety level is high please met docter";


        //arraylist
        a1=new ArrayList<>();
        a2=new ArrayList<>();

        //get today
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date2=new Date();
        String today= dateFormat.format(date2);

        //get data from firebase Users table
        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user userobj=dataSnapshot.getValue(user.class);
                userName=userobj.getCallName();
                Log.i("user",userName);
                status_anx="Hello " +userName+" \n see your anxiety level here";
                textView.setText(status_anx);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get data from firebase ATR table
        Anx_child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String dateval=ds.getKey();
                    Double AnVar=ds.getValue(Double.class);
                    int anvalue=(int)(AnVar*1);

                    if(today.equals(dateval)){
                        TotalAx=anvalue;
                    }
                    a1.add(anvalue);
                    a2.add(dateval);

                    val=val +1;

                    Log.i("test1",dateval+""+anvalue);
                    loadgraph();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(TotalAx<30){
            textmsg.setText(status1);
        }else  if(TotalAx<60){
            textmsg.setText(status2);
        }else{
            textmsg.setText(status3);
        }

    }


    //pass values to X and Y axis
    public void loadgraph() {

        Yaxis=new int[a1.size()];
        Xaxis=new String[a2.size()];

        int s;
        for (s=0;s<a1.size();s++){
            Yaxis[s]=a1.get(s);
            Xaxis[s]=a2.get(s);
        }


        List Xaxisvalues = new ArrayList();
        List Yaxisvalues = new ArrayList();

        Line line2 = new Line(Yaxisvalues).setColor(Color.parseColor("#DC143C"));

        for (int i = 0; i < Xaxis.length; i++) {
            Xaxisvalues.add(i, new AxisValue(i).setLabel(Xaxis[i]));
        }

        for (int i = 0; i < Yaxis.length; i++) {
            Yaxisvalues.add(new PointValue(i, Yaxis[i]));

        }

        List lines = new ArrayList();
        lines.add(line2);

        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axis = new Axis();
        axis.setName("Time Period");
        axis.setValues(Xaxisvalues);
        axis.setTextSize(12);
        axis.setTextColor(Color.parseColor("#616161"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Anxiety Level");
        yAxis.setTextColor(Color.parseColor("#616161"));
        yAxis.setTextSize(12);
        data.setAxisYLeft(yAxis);

        chartanxiety.setLineChartData(data);
        Viewport viewport = new Viewport(chartanxiety.getMaximumViewport());
        viewport.top = 120;
        chartanxiety.setMaximumViewport(viewport);
        chartanxiety.setCurrentViewport(viewport);
    }

}
