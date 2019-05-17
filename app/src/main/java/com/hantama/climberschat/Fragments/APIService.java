package com.hantama.climberschat.Fragments;

import com.hantama.climberschat.Notification.MyResponse;
import com.hantama.climberschat.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAATOBe3oA:APA91bG-bL2YBTXQWrvhzR6zSCumNhv7BxtM1y6p6HhH11X_qeV-ebS66TcqiPOPu4p4TqZIbFaXb32NdL03VBGQ1MHa-B1VghiiQyWRAQy0fb61b_0gh-ipuezh4x7GRMRtDkgQJL7R"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
