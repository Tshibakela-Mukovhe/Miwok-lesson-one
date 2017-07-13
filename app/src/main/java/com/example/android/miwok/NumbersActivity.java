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

public class NumbersActivity extends AppCompatActivity {
    //
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
        setContentView(R.layout.word_list);

        //for audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //created a list of array
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word(getResources().getString(R.string.one), "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word(getResources().getString(R.string.two), "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word(getResources().getString(R.string.three), "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word(getResources().getString(R.string.four), "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word(getResources().getString(R.string.five), "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word(getResources().getString(R.string.six), "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word(getResources().getString(R.string.seven), "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word(getResources().getString(R.string.eight), "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word(getResources().getString(R.string.nine), "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word(getResources().getString(R.string.ten), "na'aacha", R.drawable.number_ten, R.raw.number_ten));


        // setb adapter.................
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //we fetch the words by the following
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

                    mediaplayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId());

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

