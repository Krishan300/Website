package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private Button mButtonAdd;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //kieran json from tutorial
    TextView mTxtDisplay;
    ImageView mImageView;
    String url = "https://helloworldluna.herokuapp.com/";     //need to figure out url, is it heroku?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change the action bar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.GRAY)
        );



        //corey caplan comments, think he was referring to refreshing
        // begin code that should occur when the user does an appropriate action
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new StandardErrorListener(this));

        // create a class that implments error listener and solves the problems uniformly
        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        // end code that should occur when the user does an appropriate action
        //end corey comments




        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mButtonAdd = (Button) findViewById(R.id.btn_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Define a layout for RecyclerView
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Initialize a new instance of RecyclerView Adapter instance with dummycode animalslist
        //DummyCode a = new DummyCode();
        //final List<String> animalsList = a.insertDummyCode();
        final List<String> animalsList = new ArrayList<String>();
        mAdapter = new AnimalsAdapter(getApplicationContext(), animalsList);


        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Set a click listener for add item button
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // instantiate a new AlertDialog Builder
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                // set the alert's title and message
                alert.setTitle("Input New Data");
                alert.setMessage("Please enter some new data here:");

                // create an EditText object to store the user's input.
                final EditText input = new EditText(getApplicationContext());
                input.setHintTextColor(Color.LTGRAY);
                input.setHint("Enter data here!");
                input.setTextColor(Color.BLACK);
                alert.setView(input);

                // create the in box buttons: first OKAY:
                alert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String itemLabel = input.getText().toString();
                        if(itemLabel.length() == 0){
                            Toast.makeText(getApplicationContext(), "Cannot post blank comments", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Now, let's add the value and notify the app about changes: Specify the position
                            int position = 0;

                            // Add an item to animalslist
                            animalsList.add(position, "" + itemLabel);

                            // Notify the adapter that an item inserted
                            mAdapter.notifyItemInserted(position);

                            // Scroll to newly added item position
                            mRecyclerView.scrollToPosition(position);
                        }
                    }
                });

                //Cancel button.
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(),"Addition Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });

                //show the alert box!
                alert.show();
            }
        });
    }
}