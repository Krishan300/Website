package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //declare variables
    //this is a  test comment
    // private TextView mfactTextView;
    public Button mLaunchMessage;

    private EditText getMessage;

    //private EditText GetName;
    //private EditText GetDate;

    //ArrayList<Message> listofmessages=new ArrayList<Message>();


    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    @Override
    public File getNoBackupFilesDir() {
        return super.getNoBackupFilesDir();
    }

    ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ListView mListView = (ListView) findViewById(R.id.list_view);

        final ArrayList<Message> listofMessages=new ArrayList<>();//set up Arraylist of Messages
         listofMessages.add(new Message("John", "cAKES", "Test"));//added Message because mListView was getting null error
         //Message c=new Message;
         //listofMessages.add()
        //String[] listItems = new String[listofMessages.size()];
        MyAdapter adapter=new MyAdapter(this, listofMessages);
        mListView.setAdapter(adapter);//set up adapter
        //MyAdapter adapter= new MyAdapter(this, listofMessages);
        //mListView.setAdapter(adapter);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //setContentView(R.layout.activity_main);
        mLaunchMessage = (Button) findViewById(R.id.button2);
        getMessage = (EditText) findViewById(R.id.editText);
        // GetName=(EditText) findViewById(R.id.NameEditText);
        //GetDate=(EditText) findViewById(R.id.DateEditText);

        mLaunchMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w) {
                //MyAdapter adapter = new MyAdapter(this, listofMessages);
                //String MessageText = getMessage.getText().toString();
                //Toast.makeText(MainActivity.this, MessageText, Toast.LENGTH_LONG).show();

                //Intent launchMessage = new Intent(MainActivity.this, ActivityMessage.class);
                //launchMessage.putExtra(getString(R.string.messagetext), MessageText);
                //startActivity(launchMessage);
                //Intent launchMessage=ActivityMessage.createIntent(a);
                //this.startActivity(launchMessage);

            }
        });




       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MessageText = getMessage.getText().toString();
                Toast.makeText(MainActivity.this, MessageText, Toast.LENGTH_LONG).show();
                Intent launchMessage = new Intent(MainActivity.this, ActivityMessage.class);
                //launchMessage.putExtra(getString(R.string.messagetext), MessageText);
                startActivity(launchMessage);

            }

        });*/


    }

        public void startMessageDetailsActivity (Message a)
        {
        Intent launchMessage = ActivityMessage.createIntent(a);
        this.startActivity(launchMessage); //shows details of each message in Listview
        }




}




