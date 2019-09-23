package lk.sliit.moodypp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class audiodownload extends AppCompatActivity {


    String[] songList = {"mySong1","mySong2","mySong3","mySong4",
            "mySong5","mySong6","mySong7","mySong8"};

    ListView mylistviewSong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiodownload);


        mylistviewSong = (ListView)findViewById(R.id.dummylist);
        ArrayAdapter<String> myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songList);
        mylistviewSong.setAdapter(myadapter);


    }



}



