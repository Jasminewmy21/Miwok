package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mAudioPlayer;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mAudioPlayer.pause();
                mAudioPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mAudioPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseAudioPlayer();
            }
        }
    };

    MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseAudioPlayer();
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> colorWords = new ArrayList<>();

        colorWords.add(new Word("weṭeṭṭi", "red", R.drawable.color_red, R.raw.color_red));
        colorWords.add(new Word("chokokki", "green", R.drawable.color_green, R.raw.color_green));
        colorWords.add(new Word("ṭakaakki", "brown", R.drawable.color_brown, R.raw.color_brown));
        colorWords.add(new Word("ṭopoppi", "gray", R.drawable.color_gray, R.raw.color_gray));
        colorWords.add(new Word("kululli", "black", R.drawable.color_black, R.raw.color_black));
        colorWords.add(new Word("kelelli", "white", R.drawable.color_white, R.raw.color_white));
        colorWords.add(new Word("ṭopiisə", "dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorWords.add(new Word("chiwiiṭə", "mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), colorWords, R.color.category_colors);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Word currentWord = colorWords.get(position);

                releaseAudioPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mAudioPlayer = MediaPlayer.create(getActivity(), currentWord.getAudioResourceId());
                    mAudioPlayer.start();
                    mAudioPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseAudioPlayer();
    }

    private void releaseAudioPlayer() {
        //If the player is not null, then it may be currently playing a sound.
        if (mAudioPlayer != null) {
            //Regardless of the current state of the media player
            //release its resources
            mAudioPlayer.release();

            mAudioPlayer = null;

            //Abandon audio focus when playback complete
            //Regardless of whether or not we were granted audio focus, abandon it.
            //This is unregisters the AudioFocusChangeListener so we don't get anymore callbacks
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangListener);
        }
    }
}