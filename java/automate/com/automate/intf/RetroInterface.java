package automate.com.automate.intf;

import automate.com.automate.model.ServiceRequestData;
import automate.com.automate.model.ServerResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by varun on 7/30/2016.
 */
public interface RetroInterface {

    //This method is used for "POST"
    //@FormUrlEncoded
    @POST("/api/sr")
    void postData(@Body ServiceRequestData body,
                  Callback<ServerResponse> serverResponseCallback);

    /*//This method is used for "GET"
    @GET("/api.php")
    void getData(@Query("method") String method,
                 @Query("username") String username,
                 @Query("password") String password,
                 Callback<ServerResponse> serverResponseCallback);*/
}
