package com.padunime.animemanager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class AnimeViewActivity extends AppCompatActivity implements AnimeAdapter.OnItemClickListener {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_DESCRIPTION = "description";
    public  static final String EXTRA_ANIMEID = "Anime ID";
    public static int ani_id;
    private RecyclerView mRecylcerView;
    private AnimeAdapter mAnimeAdapter;
    private ArrayList<AnimeItem> mAnimeList;
    private int REC_CODE = 0;

    public void getAnimeListView() throws IOException {

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        ArrayList<ArrayList<String>> mArrayList = mDatabaseHelper.getAnimeList();
        for (int i = 0; i < mArrayList.size(); i++) {

            int ani_id = Integer.parseInt(mArrayList.get(i).get(0));
            String title = mArrayList.get(i).get(1);
            int score = Integer.parseInt(mArrayList.get(i).get(2));
            String descrip = mArrayList.get(i).get(3);
            mAnimeList.add(new AnimeItem(get_Image(ani_id), title, descrip, score, ani_id));
        }


        Toast.makeText(getApplicationContext(), "Anime for everyone!", Toast.LENGTH_SHORT).show();


        mAnimeAdapter = new AnimeAdapter(AnimeViewActivity.this, mAnimeList);
        mRecylcerView.setAdapter(mAnimeAdapter);
        mAnimeAdapter.setOnItemClickListener(AnimeViewActivity.this);
    }


    public Drawable get_Image(int ani_id) throws IOException {
        InputStream ims = getAssets().open("covers/" + ani_id + ".jpg");
        Drawable d = Drawable.createFromStream(ims, null);
        return d;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu main_menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, main_menu);
        main_menu.findItem(R.id.settings_option).setVisible(false);
        MenuItem item = main_menu.findItem(R.id.default_sort);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.my_animes_option:
                Intent intentView = new Intent(AnimeViewActivity.this, AnimeViewUser.class);
                startActivity(intentView);
                return true;

            case R.id.sort_name_asc:
                Intent intentViewNameAsc = new Intent(AnimeViewActivity.this, AnimeViewNameAsc.class);
                //startActivityForResult(intentViewNameAsc, 1);
                startActivity(intentViewNameAsc);
                finish();
                return true;

            case R.id.sort_name_desc:
                Intent intentViewNameDesc = new Intent(AnimeViewActivity.this, AnimeViewNameDesc.class);

                //startActivityForResult(intentViewNameDesc, 2);
                startActivity(intentViewNameDesc);
                finish();

                return true;

            case R.id.sort_score_asc:
                Intent intentViewScoreAsc = new Intent(AnimeViewActivity.this, AnimeViewScoreAsc.class);
                //startActivityForResult(intentViewScoreAsc, 3);
                startActivity(intentViewScoreAsc);
                finish();
                return true;

            case R.id.sort_score_desc:
                Intent intentViewScoreDesc = new Intent(AnimeViewActivity.this, AnimeViewScoreDesc.class);
                //startActivityForResult(intentViewScoreDesc, 4);
                startActivity(intentViewScoreDesc);
                finish();
                return true;

            case R.id.settings_option:

                Intent intentSettings = new Intent(AnimeViewActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;


            case R.id.logout_option:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(AnimeViewActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_view);
        mRecylcerView = findViewById(R.id.RecyclerView);
        mRecylcerView.setHasFixedSize(true);
        mRecylcerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAnimeList = new ArrayList<>();
        try {
            getAnimeListView();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        AnimeItem clickedItem = mAnimeList.get(position);
        ani_id = clickedItem.getmAnimeId();
        Toast.makeText(getApplicationContext(), clickedItem.getmAnimeName(), Toast.LENGTH_SHORT).show();
        detailIntent.putExtra(EXTRA_TITLE, clickedItem.getmAnimeName());
        detailIntent.putExtra(EXTRA_SCORE, String.valueOf(clickedItem.getmScore()));
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedItem.getmDescription());
        detailIntent.putExtra(EXTRA_ANIMEID, clickedItem.getmAnimeId());
        detailIntent.putExtra(String.valueOf(ani_id), ani_id);
        startActivity(detailIntent);
    }

}