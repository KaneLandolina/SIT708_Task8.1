package com.example.task81;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class SearchViewAdaptor extends ArrayAdapter<String> {

    private Activity context;
    private List<String> videoIds;

    private List<String> videoThumbnails;

    private List<String> videoTitles;

    public SearchViewAdaptor(Activity context, List<String> videoIds, List<String> videoThumbnails, List<String> videoTitles) {
        super(context, R.layout.search_list_view);
        // TODO Auto-generated constructor stub  

        this.context=context;
        this.videoIds=videoIds;
        this.videoThumbnails=videoThumbnails;
        this.videoTitles=videoTitles;

    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.search_list_view, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.titleTextView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        Log.v("received title", videoTitles.get(position));
        Log.v("received thumb", videoThumbnails.get(position));
        Log.v("received ids", videoIds.get(position));


        titleText.setText(videoTitles.get(position));
        //imageView.setImageResource(videoThumbnails.get(position));
        Glide.with(context).load(videoThumbnails.get(position)).into(imageView);

        return rowView;

    };
}  