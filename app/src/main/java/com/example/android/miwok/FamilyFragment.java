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
public class FamilyFragment extends Fragment {

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

    public FamilyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> familyWords = new ArrayList<>();

        familyWords.add(new Word("əpə", "father", R.drawable.family_father, R.raw.family_father));
        familyWords.add(new Word("əṭa", "mother", R.drawable.family_mother, R.raw.family_mother));
        familyWords.add(new Word("angsi", "son", R.drawable.family_son, R.raw.family_son));
        familyWords.add(new Word("tune", "daughter", R.drawable.family_daughter, R.raw.family_daughter));
        familyWords.add(new Word("taachi", "old brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyWords.add(new Word("chalitti", "younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyWords.add(new Word("teṭe", "old sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyWords.add(new Word("kolliti", "younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyWords.add(new Word("ama", "grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyWords.add(new Word("paapa", "grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));


        WordAdapter wordAdapter = new WordAdapter(getActivity(), familyWords, R.color.category_family);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Word currentWord = familyWords.get(position);

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
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
            mAudioPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangListener);
        }
    }
}