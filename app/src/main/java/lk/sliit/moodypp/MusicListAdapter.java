package lk.sliit.moodypp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter {

    List<Music> music;
    Activity activity;

    public MusicListAdapter(List<Music> music, Activity activity) {
        this.music = music;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.music_item_layout, viewGroup, false);

        MusicViewHolder viewHolder = new MusicViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Music music = this.music.get(position);
        final MusicViewHolder musicViewHolder = (MusicViewHolder) viewHolder;
        musicViewHolder.mname.setText(music.getMname());


        musicViewHolder.Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(music.getDownloadLink()));

                activity.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return this.music.size();
    }
}


