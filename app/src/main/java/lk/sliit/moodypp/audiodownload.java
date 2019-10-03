package lk.sliit.moodypp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class audiodownload extends AppCompatActivity {

        Button downloadMusic;
        FirebaseStorage firebaseStorage;
        StorageReference storageReference;
        StorageReference ref;
        StorageReference ref2;
        StorageReference ref3;





    //String[] songList = new String[]{"Meditation_Music","Stress_Relaxation","Therapy_Music",};
    private ArrayList<String> songList=new ArrayList<String>();
    Intent Output;
    ListView mylistviewSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiodownload);

        /*recyclerView = findViewById(R.id.rv_heroes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MusicListAdapter heroListAdapter = new MusicListAdapter(musics, this);
        recyclerView.setAdapter(heroListAdapter);*/

        downloadMusic = findViewById(R.id.downloadmusic);


        downloadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        mylistviewSong = (ListView) findViewById(R.id.dummylist);
        songList.add("Meditation_Music");
        songList.add("Stress_Relaxation");
        songList.add("Therapy_Music");
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songList);
        mylistviewSong.setAdapter(myadapter);


        // ListView on item selected listener.
        mylistviewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(songList.get(position).equals("Meditation_Music")) {
                    Output = new Intent(audiodownload.this, First_Activity.class);
                        startActivity(Output);
                }

                if(songList.get(position).equals("Stress_Relaxation")) {
                    //Output = new Intent(audiodownload.this, SecondActivity.class);
                    startActivity(Output);
                }
            }
        });

        //PopulateMusic();

    }

        


    public void download(){
        storageReference=firebaseStorage.getInstance().getReference();
        ref=storageReference.child("Anxiety - Background Music.mp3");
        ref2=storageReference.child("Most Emotional Music A Final Sacrifice by Luke Richards.mp3");
        ref3=storageReference.child("Most Emotional Music A Final Sacrifice by Luke Richards.mp3");

        //mysong1
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(audiodownload.this,"Anxiety - Background Music",".mp3",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //mysong2
        ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(audiodownload.this,"Most Emotional Music A Final Sacrifice by Luke Richards",".mp3",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //mysong3
        ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(audiodownload.this,"SUSPENSEFUL ANXIETY MUSIC",".mp3",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

   /* public  void godownloadsong(View view){
        Intent i=new Intent(this,download_music.class);
        startActivity(i);
    }*/

    public  void downloadfiles(Context context,String fileName,String fileExtension,String destinationdirectory,
    String url){
        DownloadManager downloadManager=(DownloadManager)context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationdirectory,fileName+fileExtension);

        downloadManager.enqueue(request);


    }


}






