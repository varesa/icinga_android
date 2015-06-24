package fi.dy.esav.icingaandroidclient;

import android.util.JsonReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by esa on 24.6.2015.
 */
public class ServiceObject {
    public String host_name = "";
    public String service_name = "";
    public int service_status = -1;

    public ServiceObject(String host_name, String service_name, int service_status) {
        this.host_name = host_name;
        this.service_name = service_name;
        this.service_status = service_status;
    }

    static List<ServiceObject> parseJSON(JsonReader reader) throws IOException {
        List<ServiceObject> services = new LinkedList<ServiceObject>();
        reader.beginObject();
        if(reader.nextName().equals("services")) {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();

                String host_name = "None";
                String service_name = "None";
                int service_status = -1;
                while(reader.hasNext()) {

                    String name = reader.nextName();
                    switch (name) {
                        case "host_name":
                            host_name = reader.nextString();
                            break;
                        case "service_name":
                            service_name = reader.nextString();
                            break;
                        case "service_state":
                            service_status = reader.nextInt();
                            break;
                        default:
                            reader.nextString(); // Invalid key, try removing a string token
                    }
                }

                services.add(new ServiceObject(host_name, service_name, service_status));
                reader.endObject();
            }
            reader.endArray();
        }
        reader.endObject();

        return services;
    }
}
