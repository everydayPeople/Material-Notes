package nf.co.davejohncole.materialnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {

    public static Context context;

    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    filehandler fh;
    File file[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        Slide s = new Slide();
        s.setDuration(500);
        s.setStartDelay(400);
        s.setSlideEdge(Gravity.BOTTOM);
        getWindow().setEnterTransition(s);
        Fade f = new Fade();
        f.setDuration(1000);
        getWindow().setExitTransition(f);
        getWindow().setReenterTransition(f);
        getWindow().setReturnTransition(f);
        setContentView(R.layout.activity_main);

        context = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        fh = new filehandler();
        if (fh.listFilesLocal() == null) {
            try {
                fh.mkDirLocal(String.valueOf(getString(R.string.demoNote)));
                Toast.makeText(getBaseContext(), "Welcome to Material Notes",
                        Toast.LENGTH_LONG).show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        File file[] = fh.listFilesLocal();
        List<String> items = new ArrayList<String>();
        final String[] files = new String[file.length];
        for (int i = 0; i < file.length; i++) {
            items.add(file[i].getName());
        }

        List<String> previews = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            try {
                previews.add(String.valueOf(fh.getNoteContentLocal(file[i].getName())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //initialise the adapter
        mAdapter = new CustomAdapter(this,items, previews,MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        //new note button
        int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        Outline outline = new Outline();
        outline.setOval(0, 0, size, size);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newNote();
            }
        });
    }


    public void newNote() {
        Intent listIntent = new Intent(this, Note.class);
        listIntent.putExtra("createNewNote", true);
        listIntent.putExtra("sharedText", "");
        startActivity(listIntent);
        //finishAfterTransition();
    }

    public void search () {
        final EditText searchText = new EditText(this);
        searchText.setTextColor(Color.BLACK);
        new AlertDialog.Builder(this)
                .setTitle("Search")
                .setView(searchText)
                .setCancelable(true)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String searchFor = searchText.getText().toString();
                        String results[] = new String[fh.search(searchFor).length];
                        for (int i=0; i < fh.search(searchFor).length ; i++) {
                            results[i]=fh.search(searchFor)[i];
                        }
                        if (results.length > 0) {
                            showResults(results);
                        } else {
                            Toast.makeText(getBaseContext(), "No results for: " + searchFor,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public void showResults (String[] r) {
        final String[] result = r;
        for (int i=0; i < r.length; i++) {
            System.out.println(r[i]);
        }
        Toast.makeText(getBaseContext(), "Found "+r.length+" results",
                Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Results")
                .setItems(r, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent nextScreen;
                        nextScreen = new Intent(getApplicationContext(), Note.class);
                        nextScreen.putExtra("selectedNote", result[which]);
                        startActivity(nextScreen);
                        finish();
                    }
                });
        builder.show();
    }



    //Handles the Menu system on the top bar. To be implemented later
    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            search();
        }
        return super.onOptionsItemSelected(item);
    }


    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

}

