package fi.dy.esav.icingaandroidclient;

import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by esa on 23.6.2015.
 */
public class IcingaWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IcingaViewsFactory(this.getApplicationContext(), intent);
    }
}

class IcingaViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private URL url;
    private URLConnection connection;
    private ServiceObjectList services;
    private ServiceObjectList servicesNotOK;



    public IcingaViewsFactory(Context context, Intent intent) {
        this.context = context;
        System.out.println("Constructor");
    }

    @Override
    public void onCreate() {
        try {
            this.url = new URL("http://192.168.43.85:5000/services");
            this.connection = this.url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.services = new ServiceObjectList();
    }

    @Override
    public void onDataSetChanged() {
        try {
            InputStream in = new BufferedInputStream(this.connection.getInputStream());
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            this.services.addAll(ServiceObject.parseJSON(reader));
            this.servicesNotOK = this.services.filterNotOK();
            for(ServiceObject service : this.servicesNotOK) {
                System.out.println(service.host_name + "-" + service.service_name + ": " + service.service_status);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return this.servicesNotOK.size()+1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        int layoutId;
        String text;

        if(position < this.servicesNotOK.size()) {
            ServiceObject service = this.servicesNotOK.get(position);


            switch (service.service_status) {
                case 1:
                    layoutId = R.layout.service_warning;
                    break;
                case 2:
                    layoutId = R.layout.service_error;
                    break;
                case 3:
                default:
                    layoutId = R.layout.service_unknown;
                    break;
            }

            text = service.host_name + ": " + service.service_name;
        } else {
            layoutId = R.layout.service_ok;
            text = "Services OK: " + this.services.filterOK().size();
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), layoutId);
        rv.setTextViewText(R.id.item_textview, text);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
