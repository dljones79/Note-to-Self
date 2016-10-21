package dljones79.com.notetoself;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by dljones on 10/21/16.
 */

public class AppWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x1010101;
    private static final String TAG = AppWidgetViewFactory.class.getSimpleName();

    private ArrayList<Note> mNotes;
    private Context mContext;

    public AppWidgetViewFactory(Context context) {
        mContext = context;
        mNotes = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        // load data from storage
        loadData();

        Log.i(TAG, mNotes.toString());
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    @Override
    public void onDestroy() {
        mNotes.clear();
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Note note = mNotes.get(position);

        RemoteViews noteView = new RemoteViews(mContext.getPackageName(), R.layout.note_item);

        noteView.setTextViewText(R.id.noteTitle, note.getTitle());
        noteView.setTextViewText(R.id.noteDescription, note.getDescription());

        Intent intent = new Intent();
        intent.putExtra(AppWidget.EXTRA_ITEM, note);
        noteView.setOnClickFillInIntent(R.id.note_item, intent);

        return noteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    // Method to load saved data
    public void loadData() {
        JSONSerializer serializer;

        Log.i(TAG, "Attempting to load data.");

        serializer = new JSONSerializer("NoteToSelf.json",
                mContext);

        try {
            mNotes = serializer.load();
            Log.i(TAG, "Loading Successful!");
        } catch (Exception e) {
            Log.e("Error loading notes: ", "", e);
        }
    }
}

