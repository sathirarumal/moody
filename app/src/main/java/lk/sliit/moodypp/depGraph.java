package lk.sliit.moodypp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.grpc.netty.shaded.io.netty.channel.MaxBytesRecvByteBufAllocator;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class depGraph<i> extends AppCompatActivity {

    LineChartView lineChartView;

   //static String t="";
   static  String status="Hello Madushi see your Depression Progress result here";
   static  String status1="You don't have depression";
    static  String status2="You have depression please follow up to check more";
    static  String status3="your depression level is high.please met Doctor";



   public TextView tv;
   public TextView Result;
   int[] pointvalue={10,50,94};
   //public String[] axisData;
   // double[] yAxisData = {0.2,0.65,0.34,0.91, 0.20, 0.60, 0.15, 0.40, 0.45, 0.10, 0.90, 0.18};
   //int[] yAxisData ;
   public int Max;

    private String userId;

    public DatabaseReference Fde_Database;
    public DatabaseReference F_deChild;
    public FirebaseAuth mAuth;

    public int val=1;
    double dpVar;
    int dpval;
    String dateVar;

    ArrayList<Integer> a1; //y
    ArrayList<String> a2; //x

    int[] yAxisData={};
    String[] axisData={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view2);

        //get userid from firebase
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        //firebase connection
        Fde_Database = FirebaseDatabase.getInstance().getReference("DTR");
        F_deChild=Fde_Database.child(userId);

        //a1=new ArrayList<>();
        //a2=new ArrayList<>();

         //yAxisData=new int[a1.size()];
         //axisData=new String[a2.size()];

        F_deChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    String dateVar=ds.getKey();
                    Double dpVar=ds.getValue(Double.class);
                    int dpvalue=(int)(dpVar*100);

                    yAxisData[0]=dpvalue;
                    axisData[0]=dateVar;


                    Log.i("sra",dateVar+" "+dpvalue);
                    val= val+1;
                }
                //Log.i("sra",dp[0]+" "+date[0]);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //yAxisData=a1.toArray(yAxisData);
        //axisData=a2.toArray(axisData);
        //Log.i("sra",axisData[0]+" "+dpVar);

//
        //String[] axisData = {"09/01", "09/03", "09/05", "09/08", "09/11", "09/13", "09/18", "09/20", "09/21","09/20", "09/23", "09/29","10/02","10/05","10/06","10/09"};
        //int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18,10,20,30,45};

        lineChartView = findViewById(R.id.chart2);
        TextView tv = (TextView) findViewById(R.id.textView4);
        TextView Result = (TextView) findViewById(R.id.display2);
        tv.setText(status);

        // Max=pointvalue[100];
        if (pointvalue[0] > 0 && pointvalue[0] < 30) {
            Result.setText(status1);
        } else if (pointvalue[1] > 30 && pointvalue[1] < 70) {
            Result.setText(status2);
        } else if (pointvalue[2] > 70 && pointvalue[2] < 100) {
            Result.setText(status);
        } else
            Result.setText("Hello");

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
        axis.setTextColor(Color.parseColor("#808080"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Depression Level");
        yAxis.setTextColor(Color.parseColor("#808080"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 100;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

    }
}
