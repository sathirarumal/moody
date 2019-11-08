package lk.sliit.moodypp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    String[] Xaxis={"09/05","09/07","09/09","09/16","09/19","09/22","09/30"};
    int[] Yaxis={10,20,50,40,90,10,30};
    static  String status_anx="Hello Madushi see your Anxiety Progress result here";

    private String userId;
    public FirebaseAuth mAuth;

    public DatabaseReference Fan_Database;
    public DatabaseReference F_anChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_anx);

        //firebase connection
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        Fan_Database = FirebaseDatabase.getInstance().getReference("ATR");
        F_anChild=Fan_Database.child(userId);


        F_anChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fdqSession fdqObj = dataSnapshot.getValue(fdqSession.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chartanxiety=findViewById(R.id.anxietygraph);
        TextView textView=(TextView)findViewById(R.id.welcomemsg);
        textView.setText(status_anx);

        List Xaxisvalues=new ArrayList();
        List Yaxisvalues=new ArrayList();

        Line line2=new Line(Yaxisvalues).setColor(Color.parseColor("#DC143C"));

        for (int i = 0; i < Xaxis.length; i++) {
            Xaxisvalues.add(i, new AxisValue(i).setLabel(Xaxis[i]));
        }

        for ( int i = 0; i < Yaxis.length; i++) {
            Yaxisvalues.add(new PointValue(i,Yaxis[i]));

        }

        List lines = new ArrayList();
        lines.add(line2);

        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axis = new Axis();
        axis.setName("Time Period");
        axis.setValues(Xaxisvalues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#616161"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Anxiety Level");
        yAxis.setTextColor(Color.parseColor("#616161"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        chartanxiety.setLineChartData(data);
        Viewport viewport = new Viewport(chartanxiety.getMaximumViewport());
        viewport.top = 100;
        chartanxiety.setMaximumViewport(viewport);
        chartanxiety.setCurrentViewport(viewport);
    }

}
