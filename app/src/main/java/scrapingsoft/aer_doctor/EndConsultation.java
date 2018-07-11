package scrapingsoft.aer_doctor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import model.Consultaion;
import model.remote.APIService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EndConsultation extends AppCompatActivity {
    Consultaion consultaion;
    TextView pName, ilnessDesc, cid;
    Button startConsultaion;
    EditText illnessDescription,message,drug,drugMg,drugDosage;
    final Context context;
    private RequestQueue requestQueue;
    APIService mAPIService;
    RadioButton medReq,vsitHos,testReq;
    public EndConsultation() {
        context = this;
    }

    String url = "http://50.23.0.202:31090/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        startConsultaion = (Button) findViewById(R.id.startConsultation);


        pName = (TextView) findViewById(R.id.pName);
        ilnessDesc = (TextView) findViewById(R.id.ilnessDesc);
        cid = (TextView) findViewById(R.id.cid);
        message = (EditText) findViewById(R.id.message);
        drug = (EditText) findViewById(R.id.drug);
        drugMg = (EditText) findViewById(R.id.drugMg);
        drugDosage = (EditText) findViewById(R.id.drugDosage);

        medReq=(RadioButton) findViewById(R.id.medReq);
        vsitHos=(RadioButton) findViewById(R.id.vsitHos);
        testReq=(RadioButton) findViewById(R.id.testReq);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Bundle data = getIntent().getExtras();
        consultaion = (Consultaion) data.getParcelable("contact");
        pName.setText(consultaion.getPatient());
        ilnessDesc.setText(consultaion.getIllnessDescription());
        cid.setText(consultaion.getConsultationID());
        requestQueue = Volley.newRequestQueue(this);

        startConsultaion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                JSONObject jsonObject=new JSONObject();
                JSONObject consultationDetails=new JSONObject();
                JSONObject treatment=new JSONObject();
                JSONObject treatmentDrugs=new JSONObject();
                JSONArray drugsList=new JSONArray();
                JSONObject drugs=new JSONObject();


                try {

                    jsonObject.put("patientID", "1");
                    jsonObject.put("doctorID", "1");

                    jsonObject.put("cardId", "1");
                    jsonObject.put("consultationID", cid.getText());
                    jsonObject.put("message", message.getText());
                    jsonObject.put("treatmentDrugsID", ""+new Random().nextInt(1000));
                    jsonObject.put("treatmentID", ""+new Random().nextInt(1000));
                    jsonObject.put("illnessDescription", ilnessDesc.getText());
                    jsonObject.put("medicineName", drug.getText());
                    jsonObject.put("medicineMG", drugMg.getText());
                    jsonObject.put("medicineAmount", drugDosage.getText());


                    System.out.println("upe "+jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadDoctor(jsonObject);
            }
        });




    }
    public void startConsultaiton(JSONObject json){
                Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SignupActivity.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);

        RequestBody responseBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        apiService.startConsultaion(responseBody).enqueue(new Callback<ResponseBody>() {
            Toast toast;
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("response code=" + response.code()+"response Message:"+response.message());

                if(response.isSuccessful()){
                    System.out.println("consutaltion submited successfuly");
                    toast = Toast.makeText(context, "consutaltion submited successfuly", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    toast = Toast.makeText(context, "response code=" + response.code()+"response Message:"+response.message(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                toast = Toast.makeText(context, "consutaltion submited successfuly", Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("response failure=" + t.getMessage());
                EndConsultation.super.onBackPressed();



            }
        });
    }


    private void loadDoctor(final JSONObject json) {


        final ProgressBar view = new ProgressBar(context);
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
               getWindowManager().addView(view, windowParams);
                System.out.println("load Doctor");
            }

            public void onPostExecute(Integer result) {
                getWindowManager().removeView(view);
                // process result;
            }

            @Override
            protected Integer doInBackground(Integer... arg0) {
                // do your things here
                startConsultaiton(json);

                return null;
            }
        }.execute();
    }
}
