package com.padunime.animemanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private Context mContext;
    private ArrayList<AnimeItem> mAnimeList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position) throws IOException, JSONException;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AnimeAdapter(Context context, ArrayList<AnimeItem> animeList){
        mContext = context;
        mAnimeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.anime_view, parent, false);
        return new AnimeViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {

        AnimeItem currentItem = mAnimeList.get(position);

        Drawable imageAsset = currentItem.getmImageAsset();
        String animeName = currentItem.getmAnimeName();
        int scoreCount = currentItem.getmScore();
        holder.mTextViewCreator.setText(animeName);
        holder.mTextViewLikes.setText("Score: " + scoreCount + "/100");
        holder.mImageView.setImageDrawable(imageAsset);
    }

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;


        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            try {
                                mListener.onItemClick(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }


}
