package com.example.android.miwok;

public class Word {

    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;

    public Word() {
    }

    /**
     * Create a new Word Object
     * @param miwokTranslation is the word in Miwok language
     * @param defaultTranslation is the word in a language that user is already familiar with
     *                           such as English
     * @param audioResourceId is the resource ID for audio file associated with this word
     */
    public Word(String miwokTranslation, String defaultTranslation, int audioResourceId) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mAudioResourceId = audioResourceId;
    }

    /**
     * Create a new Word Object
     * @param miwokTranslation is the word in Miwok language
     * @param defaultTranslation is the word in a language that user is already familiar with
     *                           such as English
     * @param ImageResourceId is the resource ID for image file associated with this word
     * @param audioResourceId is the resource ID for audio file associated with this word
     */
    public Word(String miwokTranslation, String defaultTranslation, int ImageResourceId, int audioResourceId) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mImageResourceId = ImageResourceId;
        mAudioResourceId = audioResourceId;
    }


    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    public void setDefaultTranslation(String defaultTranslation) {
        mDefaultTranslation = defaultTranslation;
    }

    public void setMiwokTranslation(String miwokTranslation) {
        mMiwokTranslation = miwokTranslation;
    }

    public void setImageResourceId(int imageResourceId) {
        mImageResourceId = imageResourceId;
    }

    public void setAudioResourceId(int audioResourceId) {
        mAudioResourceId = audioResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}
