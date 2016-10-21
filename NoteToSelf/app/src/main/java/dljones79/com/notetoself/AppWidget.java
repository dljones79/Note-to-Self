package dljones79.com.notetoself;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "dljones79.com.notetoself.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "dljones79.com.notetoself.AppWidget.EXTRA_ITEM";
    public static final String ACTION_VIEW_ADD = "dljones79.notetoself.ACTION_VIEW_ADD";

    private static final int REQUEST_CODE = 2;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i(AppWidget.class.getSimpleName(), ": onUpdate Running Now");

        // Update all Widgets
        for (int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            widgetView.setRemoteAdapter(R.id.note_list, intent);
            widgetView.setEmptyView(R.id.note_list, R.id.empty);

            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.note_list, pendingIntent);

            Intent addIntent = new Intent(ACTION_VIEW_ADD);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setOnClickPendingIntent(R.id.addButton, pendingIntent2);

            appWidgetManager.updateAppWidget(widgetId, widgetView);
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.note_list);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // If list item is clicked
        if (intent.getAction().endsWith(ACTION_VIEW_DETAILS)) {
            Note note = (Note)intent.getSerializableExtra(EXTRA_ITEM);

            if (note != null) {
                Intent details = new Intent(context, DialogShowNote.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DialogShowNote.NOTE_EXTRA, note);
                context.startActivity(details);
            }
        // If add button is clicked
        } else if (intent.getAction().equals(ACTION_VIEW_ADD)) {
            Intent add = new Intent(context, DialogNewNote.class);
            add.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            add.putExtra(DialogNewNote.ADD_EXTRA, "addNote");
            context.startActivity(add);
        }
        super.onReceive(context, intent);
    }
}
