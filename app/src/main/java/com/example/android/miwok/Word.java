package com.example.android.miwok;

/**
 * Created by Tshibakela on 2017/07/12.
 */

public class Word {

    //constant value that represent no image privede
    private static final int NO_IMAGE_PROVIDED = -1;
    /**
     * Default translation for the word
     */
    private String mDefaultTranslation;
    /**
     * Miwok translation for the word
     */
    private String mMiwokTranslation;
    /**
     * Image resource ID for the word
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    //audio resourse ID for words
    private int mAudioResourceId;


    //Created a Word object constructor
    public Word(String defaultTranslation, String miwokTranslation, int audioResourseId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourseId;
    }

    //Created a Word object constructor to add image
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourseId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourseId;
    }

    //get methods
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    /**
     * Return whether or not there is an image for this word
     *
     * @return
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
