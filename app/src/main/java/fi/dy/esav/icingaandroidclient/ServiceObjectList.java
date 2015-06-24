package fi.dy.esav.icingaandroidclient;

import java.util.LinkedList;

public class ServiceObjectList extends LinkedList<ServiceObject> {
    public ServiceObjectList filterNotOK() {
        ServiceObjectList servicesNotOK = new ServiceObjectList();
        for(ServiceObject service : this) {
            if(service.service_status != 0) {
                servicesNotOK.add(service);
            }
        }
        return servicesNotOK;
    }

    public ServiceObjectList filterOK() {
        ServiceObjectList servicesOK = new ServiceObjectList();
        for(ServiceObject service: this) {
            if(service.service_status != 0) {
                servicesOK.add(service);
            }
        }
        return servicesOK;
    }
}
