package com.padunime.animemanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.internal.Internal;

import static com.padunime.animemanager.AnimeViewActivity.EXTRA_DESCRIPTION;
import static com.padunime.animemanager.AnimeViewActivity.EXTRA_SCORE;
import static com.padunime.animemanager.AnimeViewActivity.EXTRA_TITLE;
import static com.padunime.animemanager.AnimeViewActivity.EXTRA_ANIMEID;

public class AnimeViewUser extends AppCompatActivity implements AnimeAdapter.OnItemClickListener{

    private RecyclerView mRecylcerView;
    private AnimeAdapter mAnimeAdapter;
    private ArrayList<AnimeItem> mAnimeList;
    public static int ani_id;
    DatabaseHelper mDatabaseHelper;
    //final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    



    public void getUserAnime() throws IOException {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        ArrayList<ArrayList<String>> mArrayList = mDatabaseHelper.getAnimeUser();


        for (int i = 0; i < mArrayList.size(); i++){

            int ani_id = Integer.parseInt(mArrayList.get(i).get(0));
            String title = mArrayList.get(i).get(1);
            int score = Integer.parseInt(mArrayList.get(i).get(2));
            String descrip = mArrayList.get(i).get(3);
            mAnimeList.add(new AnimeItem(get_Image(ani_id), title, descrip, score, ani_id));

        }

        Toast.makeText(getApplicationContext(), "My Animes!", Toast.LENGTH_SHORT).show();


        mAnimeAdapter = new AnimeAdapter(AnimeViewUser.this, mAnimeList);
        mRecylcerView.setAdapter(mAnimeAdapter);
        mAnimeAdapter.setOnItemClickListener(AnimeViewUser.this);

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
        main_menu.findItem(R.id.sort).setVisible(false);
        main_menu.findItem(R.id.settings_option).setVisible(false);
        main_menu.findItem(R.id.my_animes_option).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_option:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(AnimeViewUser.this, MainActivity.class);
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
            getUserAnime();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    @Override
    public void onItemClick(int position) throws IOException, JSONException {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        AnimeItem clickedItem = mAnimeList.get(position);
        ani_id = clickedItem.getmAnimeId();
        Toast.makeText(getApplicationContext(), clickedItem.getmAnimeName(), Toast.LENGTH_SHORT).show();
        detailIntent.putExtra(EXTRA_TITLE, clickedItem.getmAnimeName());
        detailIntent.putExtra(EXTRA_SCORE, String.valueOf(clickedItem.getmScore()));
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedItem.getmDescription());
        detailIntent.putExtra(EXTRA_ANIMEID, clickedItem.getmAnimeId());
        startActivity(detailIntent);
    }
}

