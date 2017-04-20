package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by kjh219 on 3/31/17.
 */

public class StandardErrorListener implements Response.ErrorListener {

    private final Activity activity;

    public StandardErrorListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof AuthFailureError) {
            Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(intent);
        }
        else {
            // TODO display an error to the user because error (non AUTHFAILURE) occurred
        }
    }

}
