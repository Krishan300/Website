package krishanmadan.com.androidgroupwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by krishanmadan on 2/21/17.
 */

public class MessageActivity extends AppCompatActivity {
    //Recyclerview is filled with these instances of this class

    private Message messageitem;
    private static final String KEY_MESSAGE = "MESSAGE";

    //displays content of message
    TextView content=(TextView) findViewById(R.id.MessageDisplay);
    TextView date=(TextView) findViewById(R.id.dateText);

     public static Intent createIntent(Message messageItem) {
        Intent intent = new Intent(GlobalApplication.context(), MessageActivity.class);
        intent.putExtra(KEY_MESSAGE, messageItem);
        return intent;
    }

    final EditText sendsData=(EditText) findViewById(R.id.editText);//editText for inputting message
    final EditText writeDate=(EditText) findViewById (R.id.editText2); //editText for inputting Datw

    private void bindmessageItem(){

         sendsData.setText(R.string.messagetext);
         content.setText(sendsData.getText().toString());
         date.setText(writeDate.getText().toString());

     }





}
