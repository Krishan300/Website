package edu.lehigh.cse216.androidtemwork.krm219.androidteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLaunchMessage=(Button) findViewById(R.id.button3);
        GetMessage=(EditText) findViewById(R.id.editText);
       // GetName=(EditText) findViewById(R.id.NameEditText);
        //GetDate=(EditText) findViewById(R.id.DateEditText);

        mLaunchMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w)
            {
                String MessageText=GetMessage.getText().toString();
                //Toast.makeText(MainActivity.this, MessageText, Toast.LENGTH_LONG).show();
                Intent launchMessage=new Intent(MainActivity.this, ActivityMessage.class);
                launchMessage.putExtra(getString(R.string.messagetext), MessageText);
                startActivity(launchMessage);
               /* String Name=GetName.getText().toString();
                launchMessage.putExtra("Name", Name);
                String Date=GetDate.getText().toString();
                launchMessage.putExtra("Date", Date);*/
            }
        });
        //Toolbar toolbar = (Toolbar) findViewById(toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
             //assign views from layout file to corresponding file
               // mfactTextView = findViewById(int id);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
