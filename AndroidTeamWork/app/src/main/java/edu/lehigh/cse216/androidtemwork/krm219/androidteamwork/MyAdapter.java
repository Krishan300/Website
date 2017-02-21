package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by krishanmadan on 2/17/17.
 */

public class MyAdapter extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    int messagelikes=0;
    Message a;

    public MyAdapter(Context context, ArrayList<Message> data) {
        super(context, 0);
        mData = data;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    ArrayList<Message> mData;


    @Override
    public View getView(int i, View convertview, ViewGroup parent) {

        final int messagepos = i;
        View itemview = convertview;

        if (itemview == null) {
            itemview = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false); //fill layout with itemview objects
        }


        TextView messageHeading = (TextView) itemview.findViewById(R.id.MessageDisplay);
        messageHeading.setText(R.string.inputmessage);
        messageHeading.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Message d = mData.get(messagepos);
                Snackbar.make(v, d.name + "-" + d.content + "-" + d.date, Snackbar.LENGTH_LONG).setAction(d.date, null).show();

            }
        }); //displays contents of each Message


        final EditText sendsData=(EditText) itemview.findViewById(R.id.editText);//editText for inputting message
        sendsData.setText(R.string.messagetext);
        sendsData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent=new Intent(GlobalApplication.context(), ActivityMessage.class);
                intent.putExtra(sendsData.getText().toString(), a);  //Passes to ActivityMessage a Message and it's contents

                mContext.startActivity(intent);

            }
        });


      /* Button LikeMessage = (Button) itemview.findViewById(R.id.likeMessage)  {
            LikeMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View w) {
                    messagelikes++;
                }

            });



        return itemview;
    }*/

        return itemview;

  }


}







