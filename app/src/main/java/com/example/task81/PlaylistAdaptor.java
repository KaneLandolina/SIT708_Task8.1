package com.example.task81;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PlaylistAdaptor extends RecyclerView.Adapter<PlaylistAdaptor.MyViewHolder> {

    Context context;

    ArrayList<String> playlist;


    public PlaylistAdaptor(Context context, ArrayList<String> playlist) {
        this.context = context;
        this.playlist = playlist;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlistrecyclerview_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set text and images on view
        holder.videoUrlTextView.setText(playlist.get(position));

        holder.videoUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when url pressed, allow use to play item in youtube or dismiss dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Play Video?");
                builder.setMessage("Would you like to play the video?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

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
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playlist.get(holder.getBindingAdapterPosition())));
                        context.startActivity(intent);

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView videoUrlTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //set items in view to variables
            videoUrlTextView = itemView.findViewById(R.id.videoUrlTextView);

        }
    }

}
