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
    Boolean hasCamera;
    String mCurrentPhotoPath;
    String cacheID;
    private int mInterval = 20000; // 20 seconds between cache saves
    private Handle mHandler;

    // uses android camera API to take and save picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider", //this should probably be changed
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //adds picture to android gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



    // saves image file with collision resistant name
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //local cache storage
    public class MyCacheManager {

        static Map<String, Object> mycache;

        public static Object getData(String cacheid) {
            return mycache.get(cacheid);
        }

        public static void putData(String cacheid, Object obj) {
            mycache.put(cacheid, obj);
        }

    }

    public MyCacheManager mCacheManager;



    Runnable mCacheUpdate = new Runnable() {
        @Override
        public void run() {
            try {
                cacheID = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mCacheManager.putData(cacheID, animalsList);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mCacheUpdate, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mCacheUpdate.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mCacheUpdate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        hasCamera = hasSystemFeature(PackageManager.FEATURE_CAMERA);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        final List<String> animalsList = new ArrayList<String>();
        if (cacheID != null){
            animalsList = mCacheManager.getData(cacheID);
        }
        startRepeatingTask();

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
                        String itemLabel;
                        if (mCurrentPhotoPath == null) {
                            itemLabel = input.getText().toString();
                        }
                        else {
                            itemLabel = mCurrentPhotoPath;  //if we ever actually add the google API to this, mCurrentPhotoPath must be replaced by a link to the photo in Drive
                        }
                        mCurrentPhotoPath = null;
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

                // create photo button if the device has a camera:
                if (hasCamera) {
                    alert.setPositiveButton("PHOTO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            static final int REQUEST_IMAGE_CAPTURE = 1;
                            dispatchTakePictureIntent();
                            galleryAddPic();
                        }
                    });
                }

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