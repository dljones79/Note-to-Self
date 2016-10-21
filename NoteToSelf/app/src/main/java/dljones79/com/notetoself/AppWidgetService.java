package dljones79.com.notetoself;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by dljones on 10/21/16.
 */

public class AppWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetViewFactory(getApplicationContext());
    }
}
