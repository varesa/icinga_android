package fi.dy.esav.icingaandroidclient;

import android.util.JsonReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ServiceObject {
    public String host_name = "";
    public String service_name = "";
    public int service_status = -1;

    public ServiceObject(String host_name, String service_name, int service_status) {
        this.host_name = host_name;
        this.service_name = service_name;
        this.service_status = service_status;
    }

    static ServiceObjectList parseJSON(JsonReader reader) throws IOException {
        ServiceObjectList services = new ServiceObjectList();
        reader.beginObject();
        if(reader.nextName().equals("services")) {
            reader.beginArray();
            while (reader.hasNext()) {
                services.add(JSONParseSingleService(reader));
            }
            reader.endArray();
        }
        reader.endObject();

        return services;
    }

    private static ServiceObject JSONParseSingleService(JsonReader reader) throws IOException {
        String host_name = "None";
        String service_name = "None";
        int service_status = -1;

        reader.beginObject();
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
        reader.endObject();
        return new ServiceObject(host_name, service_name, service_status);
    }
}
