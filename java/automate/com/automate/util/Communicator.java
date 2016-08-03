package automate.com.automate.util;

import android.util.Log;

import com.squareup.otto.Produce;

import automate.com.automate.model.ServiceRequestData;
import automate.com.automate.intf.RetroInterface;
import automate.com.automate.model.ErrorEvent;
import automate.com.automate.model.ServerEvent;
import automate.com.automate.model.ServerResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by varun on 7/30/2016.
 */

public class Communicator {
    private static  final String TAG = "Communicator";
    private static final String SERVER_URL = "http://poc-dev.us-west-2.elasticbeanstalk.com";

    public void requestService(ServiceRequestData serviceRequestData){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RetroInterface communicatorInterface = restAdapter.create(RetroInterface.class);
        Callback<ServerResponse> callback = new Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(), serverResponse.getMessage()));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postData(serviceRequestData, callback);
    }

   /* public void loginGet(String username, String password){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RetroInterface communicatorInterface = restAdapter.create(RetroInterface.class);
        Callback<ServerResponse> callback = new Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse serverResponse, Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(), serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.getData("login", username, password, callback);
    }*/

    @Produce
    public ServerEvent produceServerEvent(ServerResponse serverResponse) {
        return new ServerEvent(serverResponse);
    }

    @Produce
    public ErrorEvent produceErrorEvent(int errorCode, String errorMsg) {
        return new ErrorEvent(errorCode, errorMsg);
    }
}
