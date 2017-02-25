package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Context mContext;

    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private Button mButtonAdd;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();

        // Change the action bar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("darkgrey"))
        );

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mButtonAdd = (Button) findViewById(R.id.btn_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Initialize a new String array
        final String[] animals = {
                "A",
                "Screaming",
                "Comes",
                "Across",
                "The",
                "Sky"
        };

        // Intilize an array list from array
        final List<String> animalsList = new ArrayList(Arrays.asList(animals));

        // Define a layout for RecyclerView
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new AnimalsAdapter(mContext,animalsList);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Set a click listener for add item button
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here's where we need to add functionlity to retrieve user input.
                // We'll do this using the AlertDialog.Builder API.
                // First, let's instantiate a new AlertDialog Builder.
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                // Next, let's set the alert's title and message.
                alert.setTitle("Input New Data");
                alert.setMessage("Please enter some new data here:");

                // We'll create an EditText object to store the user's input.
                final EditText input = new EditText(mContext);
                alert.setView(input);

                // Finally, let's create the in box buttons: first OKAY:
                alert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String itemLabel = input.getText().toString();

                        // Now, let's add the value and notify the app about changes:
                        // Specify the position
                        int position = 0;

                        // Add an item to animals list
                        animalsList.add(position,"" + itemLabel);

                        // Notify the adapter that an item inserted
                        mAdapter.notifyItemInserted(position);

                        // Scroll to newly added item position
                        mRecyclerView.scrollToPosition(position);

                        // Show the added item label
                        Toast.makeText(mContext,"Added : " + itemLabel,Toast.LENGTH_SHORT).show();
                    }
                });

                // And last: our Cancel button.
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(mContext,"Addition Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });

                // Finally, let's just show the alert box!
                alert.show();
            }
        });
    }
}