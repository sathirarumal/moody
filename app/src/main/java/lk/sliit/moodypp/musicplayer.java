package lk.sliit.moodypp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class musicplayer extends AppCompatActivity {

    Button btn_next,btn_previous,btn_pause;
    TextView songTextbox;
    SeekBar myseekbar;
    String sname;
    static MediaPlayer myMediaplayer;
    int position;

    ArrayList<File> mySongs;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        btn_next = (Button) findViewById(R.id.next);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_pause = (Button) findViewById(R.id.pause);

        songTextbox = (TextView) findViewById(R.id.sname);
        myseekbar = (SeekBar) findViewById(R.id.seekbar);

      // getSupportActionBar().setTitle("Now Playing");
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       //getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateseekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = myMediaplayer.getDuration();
                int currentposition = 0;

                while (currentposition < totalDuration) {
                    try {
                        sleep(500);
                        currentposition = myMediaplayer.getCurrentPosition();
                        myseekbar.setProgress(currentposition);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaplayer != null) {
            myMediaplayer.stop();
            ;
            myMediaplayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songTextbox.setText(songName);
        songTextbox.setSelected(true);

        position = bundle.getInt("pos", 0);

        Uri u = Uri.parse(mySongs.get(position).toString());
        myMediaplayer = MediaPlayer.create(getApplicationContext(), u);

        myMediaplayer.start();

        myseekbar.setMax(myMediaplayer.getDuration());

        //seekbar update
        myseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        myseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        myseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaplayer.seekTo(seekBar.getProgress());
            }
        });

        //pause button

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myseekbar.setMax(myMediaplayer.getDuration());
                if (myMediaplayer.isPlaying()) {
                    btn_pause.setBackgroundResource(R.drawable.ic_play_arrow);
                } else {
                    //btn_pause.setBackground(R.drawable.ic_pause);
                   //  myMediaplayer.start();
                }
            }
        });

        //next button
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                myMediaplayer.start();
                myMediaplayer.release();
                position = ((position + 1) % mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaplayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextbox.setText(sname);

                myMediaplayer.start();
           }
        });

        //previous button

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaplayer.stop();
                myMediaplayer.release();

                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaplayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextbox.setText(sname);

                myMediaplayer.start();
            }
        });

    }

        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId()== android.R.id.home){
                onBackPressed();
            }
            return super.onOptionsItemSelected(item);

        }

    }








