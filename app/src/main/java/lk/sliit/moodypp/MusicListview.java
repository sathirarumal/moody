package lk.sliit.moodypp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
   public String type;
  // private StorageReference mStorageRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference ref;
    StorageReference ref2;
    StorageReference ref3;
    StorageReference ref4;
    StorageReference ref5;
    StorageReference ref6;
    StorageReference ref7;
    StorageReference ref8;
    StorageReference ref9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_listview);

        mylistviewSong = (ListView)findViewById(R.id.myList);
        runtimepermission();

        //store data in a file
        SharedPreferences sharePref2= PreferenceManager.getDefaultSharedPreferences(this);
        type= sharePref2.getString("userType",null);

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


/////////////////////////get
    public ArrayList<File> findSong(File root){

        ArrayList<File> arrayList = new ArrayList<File>();
        File directory = getExternalFilesDir("MoodyMusics");  //directory of the main file is MoodyMmusic
        File[] files = directory.listFiles();

        // array return not an order(2,3,4,1)- for (File singleFile : files)
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
                        .putExtra("songs",mysongs).putExtra("songname",songName)  //attach songs(bundle) to the file
                        .putExtra("pos",i));



            }
        });



    }



    //download
    public void download(View view){

        //File sdCardRoot = new File(Environment.getExternalStorageDirectory(), "/moodyMusics");

        storageReference=firebaseStorage.getInstance().getReference();
        ref=storageReference.child("Anxiety - Background Music.mp3");
        ref2=storageReference.child("Most Emotional Music A Final Sacrifice by Luke Richards.mp3");
        ref3=storageReference.child("SUSPENSEFUL ANXIETY MUSIC.mp3");
        ref4=storageReference.child("Cafe Del Mar We Can Fly.wmv.mp3");
        ref5=storageReference.child("DJ Shah - Mellomaniac (Chillout MIx).mp3");
        ref6=storageReference.child("Enya - (1988) Watermark - 01 Watermark.mp3");
        ref7=storageReference.child("Marconi Union - Weightless (Official Video).mp3");
        ref8=storageReference.child("Mozart - Canzonetta Sull'aria.mp3");
        ref9=storageReference.child("Pure Chill Out - Airstream - Electra.mp3");


        if(type.equals("depression")){

            //mysong4
            ref4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Cafe Del Mar We Can Fly.wmv",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //mysong5
            ref5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"DJ Shah - Mellomaniac (Chillout MIx)",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //mysong6
            ref6.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Enya - (1988) Watermark - 01 Watermark",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });






        }else if(type.equals("anxiety")){

            //mysong1
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Anxiety - Background Music",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //mysong2
            ref3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"SUSPENSEFUL ANXIETY MUSIC",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


            //mysong3
            ref2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Most Emotional Music A Final Sacrifice by Luke Richards",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e){

                }
            });




        }else if(type.equals("both") || type.equals("don't know") ){

            //mysong7
            ref7.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Marconi Union - Weightless (Official Video)",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //mysong8
            ref8.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Mozart - Canzonetta Sull'aria",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //mysong9
            ref9.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=uri.toString();
                    downloadfiles(MusicListview.this,"Pure Chill Out - Airstream - Electra",".mp3",url);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });




        }

    }



    public  void downloadfiles(Context context, String fileName, String fileExtension, String url) {

        String path = "/MoodyMusics";
        File directory = getExternalFilesDir("MoodyMusics");
        String dir=directory.toString();
        String filePath=dir+path+fileName+".mp3";
        Log.i("ssss",filePath);

        File file = new File(filePath);
        if (file.isFile()) {


        } else {

            DownloadManager downloadManager = (DownloadManager) context.
                    getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, path, fileName + fileExtension);

            downloadManager.enqueue(request);

        }
    }
}
