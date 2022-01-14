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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mAudioPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;

    /**
     * Create an instance of
     * AudioManager.OnAudioFocusChangeListener
     * and implement callback method
     * onAudioFocusChange(int focusChange)
     */

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            /**Adapt playback behavior when audio focus state changes.*/
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //Pause playback and reset player to the start of the file.
                //seetTo(0) play the word from the beginning when we resume playback
                mAudioPlayer.pause();
                mAudioPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //Resume playing the audio file
                //没有直接的resume方法，用start
                mAudioPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //The AUDIOFOCUS_LOSS case means we've lost audio focus and
                //stop playback and cleanup resources
                releaseAudioPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    //直接创建一个MediaPlayer.OnCompletionListener全局变量， 就不用每次播放完都创建一个这个对象
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseAudioPlayer();
        }
    };


    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        /** Inset all the code from the NumbersActivity's onCreate() method after setContentView method call*/
        //Fragment无法访问系统服务，而Activity可以
        //解决方法：首先获取 Activity 对象实例。
        // 这是封装当前 Fragment 的 Activity，即 NumbersActivity 封装了 NumbersFragment。
        // 然后对该 Activity 对象调用 getSystemService(String)。
        /** Create and setup the {@link AudioManager} to request audio focus */
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("lutti", "one", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("otiiko", "two", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("tolookosu", "three", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("oyyisa", "four", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("massokka", "five", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("temmokka", "six", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("kenekaku", "seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("kawinta", "eight", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("wo'e", "nine", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("na'aacha", "nine", R.drawable.number_ten, R.raw.number_ten));

        //传入 WordAdapter 构造函数的参数存在问题，因为第一个参数“this” 指的是这个类（即 NumbersFragment），
        // 而 Fragment 不是有效的 Context。
        // 但是， 当“this”指的是 NumbersActivity 时代码是可行的，
        // 因为 Activity 是个有效的 Context。

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(wordAdapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //通过listView的方法，传入position，获取当前点击的Word
                Word currentWord = (Word) listView.getItemAtPosition(position);

                //Release the media player if it currently exists because we are about to
                //play a different sound file
                releaseAudioPlayer();

                //Request audio focus for playback
                //当某项被点击后，希望使用AudioManager来请求Audio Focus，然后再设置MediaPlayer播放声音
                //所以放在找到currentCord之后，release资源之后
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangListener,
                        //Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        //Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //We have a audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mAudioPlayer = MediaPlayer.create(getActivity(), currentWord.getAudioResourceId());

                    // Start the audio file
                    mAudioPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mAudioPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //When the activity is stopped, release the media player resources
        //because we won't by playing any more sounds.
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