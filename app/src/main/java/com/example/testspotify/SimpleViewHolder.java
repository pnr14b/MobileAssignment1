package com.example.testspotify;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
    private TextView simpleTextView;

    public SimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        simpleTextView = (TextView)itemView.findViewById(R.id.simple_text);
    }

    public void bindData(final lTrack track1) {
        simpleTextView.setText(track1.getSong() + " by " + track1.getArtist());
    }
}
