package lk.sliit.moodypp;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MusicListview extends AppCompatActivity {
   public ListView mylistviewSong;
   public String[] items;
  // private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_listview);

        mylistviewSong = (ListView)findViewById(R.id.myList);
        runtimepermission();

      //  mStorageRef = FirebaseStorage.getInstance().getReference();

    }


    private void runtimepermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public  void gonewlist(View view){
        Intent intent=new Intent(this,audiodownload.class);
        startActivity(intent);
    }



    /*public void getAudio(){

        File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }*/
/////////////////////////get
    public ArrayList<File> findSong(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();

        for (File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));

            }else{
                if(singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav") ){
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
    }

    void display(){
        final ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());
        items=new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","");
        }

        ArrayAdapter<String> myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        mylistviewSong.setAdapter(myadapter);

        mylistviewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                String songName=mylistviewSong.getItemAtPosition(i).toString();
                startActivity(new Intent(getApplicationContext(),musicplayer.class)
                        .putExtra("songs",mysongs).putExtra("songname",songName)
                        .putExtra("pos",i));



            }
        });



    }


}
