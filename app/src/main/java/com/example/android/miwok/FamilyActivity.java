/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    ListView listView;
    MediaPlayer mediaplayer;
    int focusChange;
    private AudioManager mAudioManager;
    /**
     * the following code snippet,
     * is for playback and mediaplayer objects if the audio is trasient
     * and resume it when regain the focus. if the loss is permenant
     * it unregister the media button event receiver
     * and stops monitoring the changes
     */

    AudioManager.OnAudioFocusChangeListener AfterChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {


            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||

                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //for pause playback
                mediaplayer.pause();

                //resume
                mediaplayer.seekTo(0);


            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //regain /resume the playback
                mediaplayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                //stop playback n clean source code
                releaseMediaPlayer();
//                mAudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
//                mAudioManager.abandonAudioFocus(AfterChangeListener);
//
            }

        }
    };

    //this listener gets triggered when the link mediaplayer has completed playing diff audio
    private MediaPlayer.OnCompletionListener mCompletionListener = (new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //stop activity , release media player resources
            // for we wont be using it anymore
            releaseMediaPlayer();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

//create and set link audioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //created a list of array
        final ArrayList<Word> words = new ArrayList<Word>();
//final allows it to be read sing the onclik methods
        words.add(new Word(getResources().getString(R.string.father), "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word(getResources().getString(R.string.mother), "әta", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word(getResources().getString(R.string.son), "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word(getResources().getString(R.string.daughter), "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word(getResources().getString(R.string.olderbrother), "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word(getResources().getString(R.string.youngerbrother), "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word(getResources().getString(R.string.oldersister), "tete", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word(getResources().getString(R.string.youngersister), "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word(getResources().getString(R.string.grandfather), "ama", R.drawable.family_grandfather, R.raw.family_grandfather));
        words.add(new Word(getResources().getString(R.string.grandmother), "paapa", R.drawable.family_grandmother, R.raw.family_grandmother));

        //seting the adapter ....
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

        listView = (ListView) findViewById(R.id.FamilyXml); //for family consist of id FamilyXml

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//the above we use it to read rather than use of mp3 and the below is for the mp3 raw file
                Word word = words.get(position);


                ///request audio focus for play back

                int results = mAudioManager.requestAudioFocus(AfterChangeListener,

                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);//because our audio are short otherwise gain.

                if (results == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // mAudioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    //here we have audio focus

                    mediaplayer = MediaPlayer.create(FamilyActivity.this, word.getmAudioResourceId());

                    //start aud play
                    mediaplayer.start();

                    // set listener on media ply inorder to stop and release the mediaplyt onc sound hs finished
                    mediaplayer.setOnCompletionListener(mCompletionListener);

                }

            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        //when the activity is stopped, relaese the mediaplayer resources because we won't
        //be playing any more sounds
        releaseMediaPlayer();
    }

    //clean up media player

    private void releaseMediaPlayer() {
        if (mediaplayer != null) {
            //regardless of current state of media player since we no longer need it
            mediaplayer.release();

            //set the media player back to null. for code we decide to set media player to null (nat playing)
            mediaplayer = null;


            //abandon the audio focus when playback complete
            mAudioManager.abandonAudioFocus(AfterChangeListener);
        }
    }

}

