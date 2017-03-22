package krishanmadan.com.androidgroupwork;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by krishanmadan on 2/21/17.
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

    public static Message getDummy()  {
      String name="John Doe";
        String content="defaultmessage";
        String date="2/17/2017";
        return new Message(name, content, date);

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

    public String getName(){
        return name;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }



}