package com.tomal.attandenceapp.retrofit.userattandencelist;

import com.tomal.attandenceapp.adapters.usersAttandence.UserAttandenceAdapterModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetroInterface {

    @POST("user/get-data")
    Call<ArrayList<UserAttandenceAdapterModel>> getUserAttandence(@Body ModelDataForUserData modelDataForUserData);
}
