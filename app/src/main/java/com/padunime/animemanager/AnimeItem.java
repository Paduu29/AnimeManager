package com.padunime.animemanager;

import android.graphics.drawable.Drawable;

public class AnimeItem {
    private Drawable mImageAsset;
    private String mAnimeName;
    private int mScore;
    private String mDescription;
    private int mAnimeId;

    public AnimeItem(Drawable imageAsset, String animeName, String description, int score, int animeId){
        mImageAsset = imageAsset;
        mAnimeName = animeName;
        mDescription = description;
        mScore = score;
        mAnimeId = animeId;
    }

    public Drawable getmImageAsset() {
        return mImageAsset;
    }

    public String getmAnimeName() {
        return mAnimeName;
    }
    public String getmDescription(){return mDescription;}

    public int getmScore() {
        return mScore;
    }
    public int getmAnimeId() { return mAnimeId; }
}
