package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter<Word> {

    /**
     * Resource Id for the background color for this list of words.
     */
    private int mColorResourceId;

    private MediaPlayer audioPlayer;

    /**
     * 当出现listItemView请求时，WordAdapter将找到正确位置的Android视图，
     * 然后创建或重复使用列表项布局
     * 系统将根据Word对象中的信息更新这些view
     * 然后将listItemView返回给listView
     *
     * @param context
     * @param words
     */
    public WordAdapter(Activity context, ArrayList<Word> words, int colorRsourceId) {
        super(context, 0, words);
        mColorResourceId = colorRsourceId;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The view for the position in the AdapterView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //
        Word currentWord = getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            //如果listItemView是null，会从list_item.xml布局文件隐形膨胀新的列表项view
            //用LayoutInflater将xml布局文件变成实际的视图对象，
            //手动隐形膨胀view，所以不需要在构造函数中向super里传递资源id，所以写为0
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
            //false是因为暂时不希望将列表项view附到父listView
        }

        //listItemView变量目前引用的是列表项布局的根LinearLayout，
        // 所以能通过findViewById找到R.id.miwok_text_view等
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            //set the ImageView to the image resource specified in the current Word
            imageView.setImageResource(currentWord.getImageResourceId());

            //make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            //otherwise hide the ImageView when there is no image
            imageView.setVisibility(View.GONE);
        }


        //set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);

        //find the color that the resource Id map to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);

        //set the background color of the text container View
        textContainer.setBackgroundColor(color);

        return listItemView;
    }
}

