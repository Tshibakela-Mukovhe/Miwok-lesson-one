package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tshibakela on 2017/07/12.
 */

class WordAdapter extends ArrayAdapter<Word> {
    /**
     * Resource ID for the background color for this list of words
     */
    private int mColorResourceId;

    public WordAdapter(Activity context, List<Word> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Word currentWord = getItem(position);

//this describes how the activity must show thge layout list_item is reference of how things must display
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            //take list item looking at an array list arraylist
            //because we inflated we can target anything depending on position
        }


        // miworkTextView is the TextView within list_item.xml
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // defaultTextView is the TextView within list_item.xml
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        //Validating the image field.
        if (currentWord.hasImage()) {
            //if image is provided display it
            imageView.setImageResource(currentWord.getImageResourceId());

/** *************************Make sure the image is visible***********************
 * **********************                                   ********************
 */
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);//Gone set the image to hide and daz not take memery
        }

        /*Set the theme color for the list item
         */
        View textContainer = listItemView.findViewById(R.id.text_container);

        //Find the color thata the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        //Set background color of the text container View
        textContainer.setBackgroundColor(color);

        return listItemView;
    }
}
