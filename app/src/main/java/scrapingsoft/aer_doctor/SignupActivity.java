package scrapingsoft.aer_doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import model.remote.APIService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    EditText _nameText;

    EditText _doctorID;


    EditText contact;
    EditText _description;
    EditText cardid;
    Button _signupButton;
    TextView _loginLink;
    static String url="https://ee2bbefb.ngrok.io/api/";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        _nameText=(EditText) findViewById(R.id.input_name) ;
        _doctorID =(EditText) findViewById(R.id.doctorID) ;


        contact =(EditText) findViewById(R.id.contact) ;

        _description =(EditText) findViewById(R.id.description) ;
        cardid =(EditText) findViewById(R.id.carid) ;

        _signupButton=(Button) findViewById(R.id.btn_signup) ;
        _loginLink=(TextView) findViewById(R.id.link_login) ;



        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.MaterialLoginTheme_SelectableItemTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        //progressDialog.show();

        String name = _nameText.getText().toString();
        String doctorId = _doctorID.getText().toString();

        String mobile = contact.getText().toString();
        String password = _description.getText().toString();

        // TODO: Implement your own signup logic here.

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("accountnumber",_doctorID.getText().toString());
            jsonObject.put("firstname",_nameText.getText().toString());
            jsonObject.put("lastname","hi");
            jsonObject.put("cardid",cardid.getText().toString());
            jsonObject.put("email","za@gmail.com");
            jsonObject.put("phonenumber",contact.getText().toString());
            asyncAddDoctor(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        System.out.println("success add doctor");
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        System.out.println("failure add doctor");
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _doctorID.getText().toString();

        String mobile = contact.getText().toString();
        String password = _description.getText().toString();
        String reEnterPassword = cardid.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _doctorID.setError("Enter Valid Address");
            valid = false;
        } else {
            _doctorID.setError(null);
        }




        if (mobile.isEmpty() || mobile.length()!=10) {
            contact.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            contact.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _description.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _description.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            cardid.setError("Password Do not match");
            valid = false;
        } else {
            cardid.setError(null);
        }

        return valid;
    }


    private void asyncAddDoctor (final JSONObject json) {

        final Context context=this.getApplication();
        final ProgressBar view = new ProgressBar(this);
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
                postCallToAddDoctor(json);

                return null;
            }
        }.execute();
    }



    public void postCallToAddDoctor(JSONObject json){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);

        RequestBody responseBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        apiService.addDoctor(responseBody).enqueue(new Callback<RequestBody>() {
            Toast toast;
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                System.out.println("response code=" + response.code()+"response Message:"+response.message());

                if(response.isSuccessful()){
                    System.out.println("doctor added successfuly");

                    toast = Toast.makeText(getApplicationContext(), "doctor added successfuly", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    toast = Toast.makeText(getApplicationContext(), "response code=" + response.code()+"response Message: "+response.message(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                toast = Toast.makeText(getApplicationContext(), "doctor added failure", Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("response failure=" + t.getMessage());
                SignupActivity.super.onBackPressed();
          }
        });
    }
}