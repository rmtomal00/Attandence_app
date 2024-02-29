package com.tomal.attandenceapp.retrofit.userattandencelist;

import static com.tomal.attandenceapp.BuildConfig.url;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tomal.attandenceapp.adapters.usersAttandence.UserAttandenceAdapterModel;
import com.tomal.attandenceapp.common.SharePref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroMethod extends ViewModel {

    private MutableLiveData<ArrayList<UserAttandenceAdapterModel>> mutableLiveData = new MutableLiveData<>();
    public void getData(Context context, String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroInterface retroInterface = retrofit.create(RetroInterface.class);

        ModelDataForUserData m = new ModelDataForUserData(new SharePref(context).getToken(), email);
        Call<ArrayList<UserAttandenceAdapterModel>> call = retroInterface.getUserAttandence(m);

        call.enqueue(new Callback<ArrayList<UserAttandenceAdapterModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserAttandenceAdapterModel>> call, Response<ArrayList<UserAttandenceAdapterModel>> response) {
                if (response.isSuccessful()){
                    System.out.println(response.body());
                    ArrayList<UserAttandenceAdapterModel> list = response.body();
                    mutableLiveData.setValue(list);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserAttandenceAdapterModel>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public MutableLiveData<ArrayList<UserAttandenceAdapterModel>> getMutableLiveData() {
        return mutableLiveData;
    }
}
