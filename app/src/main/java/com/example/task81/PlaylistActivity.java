package com.example.task81;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.task81.databinding.ActivityPlaylistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    ActivityPlaylistBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    PlaylistAdaptor playlistAdaptor;

    RecyclerView.LayoutManager playlistLayoutManager;

    ArrayList<String> playlist;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating binding for using items from view
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //connect to the firebase user authenticator
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //set up recyclerview for truck list
        playlist = new ArrayList<>();


        playlistLayoutManager = new LinearLayoutManager(this);
        playlistAdaptor = new PlaylistAdaptor(this, playlist);

        binding.playlistRecyclerView.setAdapter(playlistAdaptor);
        binding.playlistRecyclerView.setLayoutManager(playlistLayoutManager);

        String id = firebaseAuth.getCurrentUser().getUid();

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myRef.child(id).child("playlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //get the playlist stored in firebase and add all urls to the view
                    String url = dataSnapshot.getValue(String.class);
                    playlist.add(url);
                }
                playlistAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}