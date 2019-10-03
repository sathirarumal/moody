package lk.sliit.moodypp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicViewHolder extends RecyclerView.ViewHolder  {

    TextView mname;
    ImageView imageView;
    Button Download;

    public MusicViewHolder(View itemView) {
        super(itemView);

        mname = itemView.findViewById(R.id.tv_name);
        imageView = itemView.findViewById(R.id.iv_image);
        Download = itemView.findViewById(R.id.btn_go);

    }
}
