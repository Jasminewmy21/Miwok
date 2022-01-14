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
public class PhrasesFragment extends Fragment {

    private MediaPlayer mAudioPlayer;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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

    public PhrasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> phrases = new ArrayList<>();
        phrases.add(new Word("minto wuksus?", "Where are you going?", R.raw.phrase_where_are_you_going));
        phrases.add(new Word("tinnə oyaase'nə?", "What is your name?", R.raw.phrase_what_is_your_name));
        phrases.add(new Word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        phrases.add(new Word("michəksəs?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("kuchi achit", "I’m feeling good.", R.raw.phrase_im_feeling_good));
        phrases.add(new Word("əənəs'aa?", "Are you coming?", R.raw.phrase_are_you_coming));
        phrases.add(new Word("həə’ əənəm", "Yes, I’m coming", R.raw.phrase_yes_im_coming));
        phrases.add(new Word("əənəm", "I’m coming.", R.raw.phrase_im_coming));
        phrases.add(new Word("yoowutis", "Let’s go.", R.raw.phrase_lets_go));
        phrases.add(new Word("ənni'nem", "Come here.", R.raw.phrase_come_here));

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        WordAdapter wordAdapter = new WordAdapter(getActivity(), phrases, R.color.category_phrases);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //udacity说要从phrases中获得当前Word对象需要把phrases设置为final，但这里没设置也可以用phrases.get(position)
                Word currentWord = phrases.get(position);

                releaseAudioPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
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
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}