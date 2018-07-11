package scrapingsoft.aer_doctor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import model.Consultaion;
import model.remote.APIService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Zephyr on 5/27/2018.
 */

public class ConsultaionController extends Fragment {
    static ListView listView1;
    TextView docName,docContact;
    ArrayList<Consultaion> doctor_List=new ArrayList<>();
    View rootView;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView= inflater.inflate(R.layout.doctor_fragment, container, false);
        context=this.getActivity();


        asyncGetDocotorConsultation(LoginActivity.jsonObject);

        ListAdapter listAdapter=new ConsultationListAdapter(context,doctor_List);

        listView1=(ListView) rootView.findViewById(R.id.consulatationList);

        listView1.setAdapter(listAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent consultaion=new Intent(context,EndConsultation.class);
                consultaion.putExtra("contact", doctor_List.get(position));
                startActivity(consultaion);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //doctor_List.clear();
                asyncGetDocotorConsultation(LoginActivity.jsonObject);
                //volleyPostRequest();
                Snackbar.make(view, "Loading available doctors...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return  rootView;
    }
    Consultaion docList;
    public void getDoctors(){

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url ="http://50.23.0.202:31090/api/queries/docEntersConsultation?doctor=resource%3Aorg.acme.Doctor%2311";
        final JSONObject json;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                        try {
                            JSONArray doctorList = new JSONArray(response);

                            for (int i=0;i<doctorList.length();i++){
                                JSONObject jsonObject=(JSONObject)doctorList.get(i);
                                docList=new Consultaion(jsonObject.getString("illnessDescription"),jsonObject.getString("patient"),jsonObject.getString("consultationID"));
                                doctor_List.add(docList);

                                listView1.invalidateViews();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }







    private void asyncGetDocotorConsultation (final JSONObject json) {

        final ProgressBar view = new ProgressBar(this.context);
        view.setBackgroundColor(0x7f000000);
        final WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.CENTER;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        new AsyncTask<Integer, Integer, Integer>() {
            public void onPreExecute() {
                // init your dialog here;
                //getWindowManager().addView(view, windowParams);
                System.out.println("load Doctor");
            }

            public void onPostExecute(Integer result) {
                // getWindowManager().removeView(view);
                // process result;
            }

            @Override
            protected Integer doInBackground(Integer... arg0) {
                // do your things here
                postCallToGetDoctor(json);

                return null;
            }
        }.execute();
    }


    public void postCallToGetDoctor(JSONObject json){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SignupActivity.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);

        RequestBody responseBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        apiService.myConsultation(responseBody).enqueue(new Callback<ResponseBody>() {
            Toast toast;
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    try {
                        JSONArray jsonArray=new JSONArray( response.body().string());

                        System.out.println("response Body :"+jsonArray.get(0));

                       // JSONArray doctorList = new JSONArray(response.body().string());
                        doctor_List.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            System.out.println("IllnessDescriotion :"+jsonObject.getString("illnessDescription"));
                            docList=new Consultaion(jsonObject.getString("illnessDescription"),jsonObject.getString("patient"),jsonObject.getString("consultationID"));
                            doctor_List.add(docList);

                            listView1.invalidateViews();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //onLoginSuccess("doctor added successfuly");
                   // progressDialog.dismiss();

                }
                else {
                    toast = Toast.makeText(context, "response code=" + response.code()+"response Message: "+response.message(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                toast = Toast.makeText(context, "doctor added failure", Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("response failure=" + t.getMessage());
            }
        });
    }




}
