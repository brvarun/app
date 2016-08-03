package automate.com.automate.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by varun on 7/30/2016.
 */
public class ServiceRequestData {

    long customerId;

    public ServiceRequestData(long customerId, String serviceDate, String serviceProvider, LatLng productLoc) {
        this.customerId = customerId;
        this.serviceDate = serviceDate;
        this.serviceProvider = serviceProvider;
        this.productLoc = productLoc;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public LatLng getProductLoc() {
        return productLoc;
    }

    public void setProductLoc(LatLng productLoc) {
        this.productLoc = productLoc;
    }

    String serviceDate;
    String serviceProvider;
    LatLng productLoc;
}
