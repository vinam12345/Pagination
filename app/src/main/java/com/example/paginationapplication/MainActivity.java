package com.example.paginationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    NestedScrollView scrollView;
    RecyclerView recycler;
    ProgressBar progressBar;
    ArrayList<Model> models = new ArrayList<>();
    MyAdapter adapter;
    int page = 0, limit = 10;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView);
        recycler = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);

         adapter = new MyAdapter(getApplicationContext(),models);
         recycler.setLayoutManager(new LinearLayoutManager(this));
         recycler.setAdapter(adapter);

         getData(page,limit);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    // when reach last item position
                    // increase page size

                    page++;
                    // show progress bar

                    progressBar.setVisibility(View.VISIBLE);
                    getData(page,limit);
                }
            }
        });


    }




    private void getData(int page, int limit) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://picsum.photos/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        // create main interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);

        // initialize call
        Call<ResponseBody>  call = mainInterface.STRING_CALL(page,limit);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // check condition

                if(response.isSuccessful() && response.body() !=null)
                {
                    try {
                        String jsonObject=response.body().string();
                        Log.e("ERROR+Res", "onResponse: "+jsonObject );
                        // initialize Json array
                        JSONArray jsonArray = new JSONArray(jsonObject);
                        // parse json array
                        parseResult(jsonArray);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    // when response is successfull not empty
                    // hide progress bar

                        progressBar.setVisibility(View.GONE);

                        try{


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("ERROR", "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(MainActivity.this, "fail"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseResult(JSONArray jsonArray) throws JSONException {

        for (int i =0; i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // initialize main data

            Model modell = new Model();
            modell.setImage(jsonObject.getString("download_url"));
            modell.setNamel(jsonObject.getString("author"));
            modell.setId(jsonObject.getString("id"));
            models.add(modell);
            adapter = new MyAdapter(MainActivity.this,models);
            recycler.setAdapter(adapter);

        }


    }
}