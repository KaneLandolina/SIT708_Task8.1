package com.example.task81;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.task81.databinding.ActivityMainBinding;
import com.example.task81.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.text.StringEscapeUtils;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final String API_KEY = "AIzaSyB5zonSrHVCSZ5WPlOfFyWgTCLI5-xO0CU";

    private List<String> videoIds;
    private List<String> videoThumbnails;
    private List<String> videoTitles;

    //private ArrayAdapter<String> videoListAdaptor;
    private SearchViewAdaptor videoListAdaptor;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating binding for using items from view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //arrays to store video ids, thumbnails and titles
        videoIds = new ArrayList<>();
        videoThumbnails = new ArrayList<>();
        videoTitles = new ArrayList<>();

        //pass ids, thumbnails and titles to adaptor to process for use in listview
        videoListAdaptor = new SearchViewAdaptor(this, videoIds, videoThumbnails, videoTitles);
        binding.searchListView.setAdapter(videoListAdaptor);

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = binding.editTextSearch.getText().toString();
                searchVideos(query);
            }
        });

        binding.searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //when search results are shown, when an item is clicked, show an alert box
                //the alert box will allow the user to add to playlist or play from youtube
                Log.v("position clicked",  videoIds.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Play or Add?");
                builder.setMessage("Would you like to Play the video or add to playlist?");

                builder.setPositiveButton("PLAY", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //I tried to use ExoPlayer for the YouTube videos, but it requires downloading
                        // the video via a HTTP GET request and then decoding multiple times to ensure no errors
                        // downloading Youtube videos is against the ToS which I believe would make this app
                        // not permitted for the app store

                        //Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                        //intent.putExtra("videoUrl", videoIds.get(position));
                        //Log.v("videoids", videoIds.get(position));
                        //startActivity(intent);
                        //finish();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoIds.get(position)));
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("ADD TO PLAYLIST", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = firebaseAuth.getCurrentUser().getUid();
                        myRef.child(id).child("playlist").push().setValue("https://www.youtube.com/watch?v=" + videoIds.get(position));
                        Toast.makeText(getApplicationContext(), "Video Added To Playlist",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        binding.buttonMyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlaylistActivity.class));
                finish();
            }
        });

    }

    private void searchVideos(String query) {
        new SearchYouTubeTask().execute(query);
    }

    private class SearchYouTubeTask extends AsyncTask<String, Void, List<YouTubeSearchResults.VideoItem>> {
        @Override
        protected List<YouTubeSearchResults.VideoItem> doInBackground(String... params) {
            //do api call and store results in variable
            String query = params[0];
            OkHttpClient client = new OkHttpClient();
            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&q=" + query + "&type=video&key=" + API_KEY;
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Gson gson = new Gson();
                YouTubeSearchResults searchResults = gson.fromJson(json, YouTubeSearchResults.class);
                return searchResults.getItems();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<YouTubeSearchResults.VideoItem> items) {
            //once doinbackground has completed, save results into ids, titles and thumbnails arrays
            //then notify the adaptor that the dataset has changed to update the view
            if(items != null) {
                videoIds.clear();
                videoThumbnails.clear();
                videoTitles.clear();

                for(YouTubeSearchResults.VideoItem item: items) {
                    String videoTitle = item.getSnippet().getTitle();
                    videoTitle = StringEscapeUtils.unescapeHtml3(videoTitle);
                    videoTitles.add(videoTitle);
                    videoIds.add(item.getVideoId().getVideoId());
//                    Log.v("thumbnails", item.getSnippet().getThumbnails().getMedium().getUrl());
//                    Log.v("video titles", videoTitle);
                    videoThumbnails.add(item.getSnippet().getThumbnails().getMedium().getUrl());
                }

                Log.v("videoIds", videoIds.toString());
                Log.v("video titles", videoTitles.toString());
                videoListAdaptor.clear();
                videoListAdaptor.addAll(videoTitles);
                videoListAdaptor.notifyDataSetChanged();

            }

        }

    }

}