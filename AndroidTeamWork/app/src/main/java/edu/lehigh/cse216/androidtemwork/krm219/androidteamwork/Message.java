package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by krishanmadan on 2/16/17.
 */

public class Message implements Parcelable {
    String name;
     String content;
     String date;

    Message(String inputname, String inputcontent, String inputdate) {
        name=inputname;
        content=inputcontent;
        date=inputdate;
    }


    protected Message(Parcel in) {
        name = in.readString();
        content = in.readString();
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };




}