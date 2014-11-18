package nf.co.davejohncole.materialnotes;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;




public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    private List<String> items;
    private List<String> previews;

    private Context context;
    private Activity parentActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;
        private final TextView pTextView;
        private final Button mButtonView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.filename);
            pTextView = (TextView) v.findViewById(R.id.previewText);
            mButtonView = (Button) v.findViewById(R.id.delBTN);


        }

        public TextView getmTextView() {
            return mTextView;
        }
        public TextView getpTextView() {
            return pTextView;
        }
        public Button getmButtonView() { return mButtonView; }
    }

    public CustomAdapter(Context context,List<String> items, List<String> previews, Activity parentActivity) {
        this.items = items;
        this.previews = previews;
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cards, viewGroup, false);
        v.animate();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final String item = items.get(position);
        final String preview = previews.get(position);
        viewHolder.getmTextView().setText(item.substring(0, item.length()-4 ));
        viewHolder.getpTextView().setText(preview);

        final ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(parentActivity);

        viewHolder.getpTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(parentActivity, Note.class);
                intent.putExtra("selectedNote", item);
                parentActivity.startActivity(intent,options.toBundle());
            }
        });
        viewHolder.getmButtonView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //remove this item
                alert(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void alert(String item) {
        final String i = item;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete note '"+item+"'?");
        builder.setCancelable(true);
        AlertDialog.Builder delete = builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    remove(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //cancel
            }
        });
        AlertDialog alertDialog = builder.show();
    }

    public void remove(String item) throws IOException {
        filehandler fh = new filehandler();
        if (fh.delFileLocal(item)) {
            int position = items.indexOf(item);
            items.remove(position);
            notifyItemRemoved(position);
        } else {
            System.out.println("could not delete file");
        }
    }

}
