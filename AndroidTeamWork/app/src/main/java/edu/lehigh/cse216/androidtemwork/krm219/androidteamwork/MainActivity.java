package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    //declare variables
    //this is a  test comment
    // private TextView mfactTextView;
    public Button mLaunchMessage;

    private EditText GetMessage;

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
        ListView mListView=(ListView) findViewById(R.id.list_view);
       // ArrayList<Message> Allthemessages=new ArrayList<>();
       // ArrayAdapter adapter=new ArrayAdapter(this, Android.R.layout.simple_list_item_1, listItems);




        //setContentView(R.layout.activity_main);
        mLaunchMessage=(Button) findViewById(R.id.button2);
        GetMessage=(EditText) findViewById(R.id.editText);
        // GetName=(EditText) findViewById(R.id.NameEditText);
        //GetDate=(EditText) findViewById(R.id.DateEditText);

       mLaunchMessage.setOnClickListener(new View.OnClickListener() {
           public void onClick(View w) {
               String MessageText = GetMessage.getText().toString();
               Toast.makeText(MainActivity.this, MessageText, Toast.LENGTH_LONG).show();
               Intent launchMessage = new Intent(MainActivity.this, ActivityMessage.class);
               launchMessage.putExtra(getString(R.string.messagetext), MessageText);
               startActivity(launchMessage);

           }
       });


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MessageText = GetMessage.getText().toString();
                Toast.makeText(MainActivity.this, MessageText, Toast.LENGTH_LONG).show();
                Intent launchMessage = new Intent(MainActivity.this, ActivityMessage.class);
                //launchMessage.putExtra(getString(R.string.messagetext), MessageText);
                startActivity(launchMessage);

            }

        });*/



  }

}




