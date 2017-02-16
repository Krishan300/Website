package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class ActivityMessage extends AppCompatActivity {
    public static final String TAG=ActivityMessage.class.getSimpleName();
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        String content;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Intent intent=getIntent();
        content=intent.getStringExtra(getString(R.string.messagetext));
        if (content==null)
        {
            content="empty message";
        }
        Log.e(TAG, content);
        TextView DisplayMessage=(TextView) findViewById(R.id.MessageDisplay);
        DisplayMessage.setText(content);

    }

}
