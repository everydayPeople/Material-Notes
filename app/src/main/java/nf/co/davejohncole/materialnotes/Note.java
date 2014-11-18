package nf.co.davejohncole.materialnotes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Note extends Activity {


    private String noteFN;
    private filehandler fh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        Explode ex = new Explode();
        ex.setDuration(500);
        Fade fa = new Fade();
        fa.setDuration(500);
       // ex.setStartDelay(1000);
        getWindow().setEnterTransition(fa);
        getWindow().setExitTransition(fa);
        getWindow().setReturnTransition(fa);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_notecard);

        Bundle extras = getIntent().getExtras();

        //should stop the keyboard from automatically popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fh = new filehandler();
        final String noteFN;

        boolean newNote;
        try {
            newNote = extras.getBoolean("createNewNote");
        } catch (Exception e) {
            newNote = true;
        }
        System.out.println("newNote = "+newNote);

        final EditText currentNote = (EditText) findViewById(R.id.noteText);
        currentNote.setText("");

        if (newNote){
            String content;
            try {
                content = extras.getString("sharedText");
            } catch (Exception e) {
                content="";
            }
            createNewNote(content);
        } else {
            noteFN = extras.getString("selectedNote");
            openNote(noteFN);
        }
    }



    /** Create a new note */
    public void createNewNote(String content) {
        final String c = content;
        final EditText notename = new EditText(this);
        final EditText noteText = (EditText) findViewById(R.id.noteText);
        noteText.setText(content);
        // Set the default note name to the current date, nd make it all selected
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        Date date = new Date();
        notename.setText("Note " + dateFormat.format(date));
        notename.setTextColor(Color.BLACK);
        notename.setSelectAllOnFocus(true);
        notename.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        notename.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                notename.setText("");
            }
        });
        new AlertDialog.Builder(this)
            .setTitle("Note Name")
            .setView(notename)
            .setCancelable(false)
            .setPositiveButton("Create Note", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String name = notename.getText().toString();

                    name.trim();
                    if (name.isEmpty()) {
                        finish();
                    } else {
                        if (fh.createFileLocal(name, c)) {
                            openNote(name + ".txt");
                        } else {
                            Toast.makeText(getBaseContext(), "Could not create note ... something went very wrong",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            })
            .show();
    }


    /** opens a file --- this should be a class */
    public void openNote (String notefn) {
        //sets the filename incase of new file
        noteFN = notefn;
        //gets the GUI items
        StringBuilder text = new StringBuilder();
        final EditText currentNote = (EditText) findViewById(R.id.noteText);
        TextView notetitle = (TextView) findViewById(R.id.noteTitle);
        //opens file
        try {
            notetitle.setText(noteFN.substring(0, notefn.length() - 4));
            currentNote.setText(fh.getNoteContentLocal(noteFN));
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Could not open note: "+noteFN,
                    Toast.LENGTH_LONG).show();}


        Button sharebtn = (Button) findViewById(R.id.shareBTN);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Share button clicked");
                String sendTxt = String.valueOf(currentNote.getText());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, (java.io.Serializable) sendTxt);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        }
        );

    }

    /** Save the file --- this should be a class */
    public void saveFile () {
        //EditText currentNote = (EditText) findViewById(R.id.notesTXT);
        final EditText currentNote = (EditText) findViewById(R.id.noteText);
        fh.saveFileLocal(noteFN,currentNote.getText());
    }

    /** makes sure to save the file if the user presses the back key, also returns to the */
    public void onBackPressed() {
        super.onBackPressed();
        saveFile();
        //finishAfterTransition();
        //saveFile();
        final ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this);
        Intent nextScreen;
        nextScreen = new Intent(getApplicationContext(), MainActivity.class);
        nextScreen.putExtra("refresh", true);
        startActivity(nextScreen,options.toBundle());
        //startActivity(nextScreen);

    }

    /** saves the file if the activity is left, like the home button is pressed, or a phone call comes through */
    @Override
    public void onPause () {
        super.onPause(); // always calls the super class first
        //save the file just in case
        saveFile();
    }

    /** method to redraw the screen in case of problem. Only call if COMPLETELY NECCESSARY */
    public void refresh () {
        this.recreate();
    }


}
