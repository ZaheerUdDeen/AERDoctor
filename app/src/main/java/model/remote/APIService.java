package model.remote;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Zephyr on 5/30/2018.
 */

public interface APIService {

        @Headers("Content-Type: application/json")
        @POST("Doctor/")
        Call<RequestBody> addDoctors(@Body RequestBody requestBody);

        @Headers("Content-Type: application/json")
        @POST("endConsultation/")
        Call<ResponseBody> startConsultaion(@Body RequestBody requestBody);

        @Headers("Content-Type: application/json")
        @POST("registerMember/")
        Call<RequestBody> addDoctor(@Body RequestBody requestBody);
        @Headers("Content-Type: application/json")
        @POST("doctorData/")
        Call<ResponseBody> getDoctor(@Body RequestBody requestBody);
        @POST("myConsultation/")
        Call<ResponseBody> myConsultation(@Body RequestBody requestBody);
}
