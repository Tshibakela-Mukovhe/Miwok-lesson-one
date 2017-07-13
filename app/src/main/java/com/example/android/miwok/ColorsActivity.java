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

public class ColorsActivity extends AppCompatActivity {

    ListView listView;

    MediaPlayer mediaPlayer;
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
                mediaPlayer.pause();

                //resume
                mediaPlayer.seekTo(0);


            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //regain /resume the playback
                mediaPlayer.start();
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
        setContentView(R.layout.activity_colors);

        //create and set link audioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //created a list of array
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word(getResources().getString(R.string.red), "wetetti", R.drawable.color_red, R.raw.color_red));
        words.add(new Word(getResources().getString(R.string.mustardyellow), "chiwiitә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word(getResources().getString(R.string.dustyyellow), "topiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word(getResources().getString(R.string.green), "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word(getResources().getString(R.string.brown), "takaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word(getResources().getString(R.string.gray), "topoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word(getResources().getString(R.string.black), "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word(getResources().getString(R.string.white), "kelelli", R.drawable.color_white, R.raw.color_white));

        //set adapter.............
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

        listView = (ListView) findViewById(R.id.GridL);

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

                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmAudioResourceId());

                    //start aud play
                    mediaPlayer.start();

                    // set listener on media ply inorder to stop and release the mediaplyt onc sound hs finished
                    mediaPlayer.setOnCompletionListener(mCompletionListener);

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


    /**
     * Connects with the initial onclick methods
     */
    //clean up media player
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            //regardless of current state of media player since we no longer need it
            mediaPlayer.release();

            //set the media player back to null. for code we decide to set media player to null (nat playing)
            mediaPlayer = null;

            //abandon the audio focus when playback complete
            mAudioManager.abandonAudioFocus(AfterChangeListener);

        }
    }

}

