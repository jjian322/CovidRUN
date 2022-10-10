package com.example.covidrun.statistics;

public class csvAdapterItem {

    private int mImageResource;
    private String mTxt1;
    private String mTxt2;

    public csvAdapterItem(int imageResource, String txt1, String txt2){

        mImageResource= imageResource;
        mTxt1=txt1;
        mTxt2=txt2;
    }

    public int getimageResource(){
        return mImageResource;
    }
    public String gettxt1(){
        return mTxt1;
    }
    public String gettxt2(){
        return mTxt2;
    }
}
