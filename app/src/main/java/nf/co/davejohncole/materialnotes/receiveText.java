package nf.co.davejohncole.materialnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Teacher on 11/7/2014.
 */
public class receiveText extends Activity {

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {
            // Toast
        }

    }

    public void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            System.out.println("Received something...");
            Intent listIntent = new Intent(this, Note.class);
            listIntent.putExtra("createNewNote", true);
            listIntent.putExtra("sharedText", sharedText);
            startActivity(listIntent);
            finish();
        }
    }


}
