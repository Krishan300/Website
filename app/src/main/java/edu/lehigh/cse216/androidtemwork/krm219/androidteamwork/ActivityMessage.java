package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ActivityMessage extends AppCompatActivity {
    public static final String TAG = ActivityMessage.class.getSimpleName();

    @Override


    protected void onCreate(Bundle savedInstanceState) {
       // String content;
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


    }

    public static Intent createIntent(Message a) {
        Intent intent = new Intent(GlobalApplication.context(), ActivityMessage.class);
        intent.putExtra("KEY_MESSAGE", a);
        return intent;
    }


    public static Intent IntentwithString(Message a, String c)
    {
        Intent b=new Intent(GlobalApplication.context(), MyAdapter.class);
        a=b.getExtras().getParcelable("foo");
        a.content=b.getData().toString();
        return b;
    }







}
