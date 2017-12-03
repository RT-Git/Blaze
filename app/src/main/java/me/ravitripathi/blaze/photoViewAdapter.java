package me.ravitripathi.blaze;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Ravi on 02-06-2017.
 */

public class photoViewAdapter extends RecyclerView.Adapter<photoViewAdapter.arcVH> {
    private List<photoItem> photoList;

    public photoViewAdapter(List<photoItem> photoList){
        this.photoList = photoList;
    }

    public class arcVH extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public arcVH(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public arcVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view_item, parent, false);

        return new arcVH(itemView);
    }

    @Override
    public void onBindViewHolder(arcVH holder, int position) {
        photoItem photo = photoList.get(position);
        Uri uri = photo.getUri();

        Context c = holder.imageView.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_home_black_24dp)
                .error(R.drawable.ic_notifications_black_24dp);


        Glide.
                with(c)
                .load(uri)
                .apply(options)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
