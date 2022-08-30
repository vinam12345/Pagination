package com.example.paginationapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {

    @GET("v2/list")
    Call<ResponseBody> STRING_CALL(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
