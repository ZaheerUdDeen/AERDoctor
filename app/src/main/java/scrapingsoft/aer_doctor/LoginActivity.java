package scrapingsoft.aer_doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;


        //@Bind(R.id.input_email)
        EditText cardid;
        EditText _orgName;
        //@Bind(R.id.input_password)
        EditText doctorId;
        //@Bind(R.id.btn_login)
        Button _loginButton;
        //@Bind(R.id.link_signup)
        TextView _signupLink;
    static JSONObject jsonObject;
    ProgressDialog progressDialog;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            //_orgName=(EditText) findViewById(R.id.input_org) ;
            cardid =(EditText) findViewById(R.id.cardid) ;

            doctorId =(EditText) findViewById(R.id.doctorId) ;

            _loginButton=(Button) findViewById(R.id.btn_login) ;

            _signupLink=(TextView) findViewById(R.id.link_signup) ;

            String prId = "zahr";
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);


            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(prId, "zaheer dinud ");
            prefsEditor.commit();
            ///prId = prefs.getString("zaheer","Chiragh");
            System.out.println("trr "+prefs.getString(prId,"Chiragh"));


            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });
        }

        public void login() {
            Log.d(TAG, "Login");

//            if (!validate()) {
//                onLoginFailed();
//                return;
//            }



            _loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(this,
                        R.style.MaterialLoginTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();



                // TODO: Implement your own authentication logic here.
            try {
                jsonObject =new JSONObject();


                jsonObject.put("cardid",cardid.getText().toString());
                jsonObject.put("doctorID",doctorId.getText().toString());

                asyncGetDocotor(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
//                new android.os.Handler().postDelayed(
//                        new Runnable() {
//                            public void run() {
//                                // On complete call either onLoginSuccess or onLoginFailed
//                                openMainMenu();
//                                progressDialog.dismiss();
//                            }
//                        }, 3000);
                return;


        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {
                    System.out.println("here at signup");
                    // TODO: Implement successful signup logic here
                    // By default we just finish the Activity and log them in automatically
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
        }


        public void onLoginSuccess(String message) {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            openMainMenu();
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed(String message) {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String email = cardid.getText().toString();
            String password = doctorId.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                cardid.setError("enter a valid email address");
                valid = false;
            } else {
                cardid.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                doctorId.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                doctorId.setError(null);
            }

            return valid;
        }
        public void openMainMenu(){
          Intent insertDetail=new Intent(this, MainActivity.class);
          insertDetail.putExtra("mainMenu",1);
          startActivity(insertDetail);
          finish();
  }
    private void asyncGetDocotor (final JSONObject json) {

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
        System.out.print("data json : "+json.toString());

        RequestBody responseBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        apiService.getDoctor(responseBody).enqueue(new Callback<ResponseBody>() {
            Toast toast;
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){


                    onLoginSuccess("doctor added successfuly");
                    progressDialog.dismiss();

                }
                else {
                    toast = Toast.makeText(getApplicationContext(), "response code=" + response.code()+"response Message: "+response.message(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                toast = Toast.makeText(getApplicationContext(), "doctor added failure", Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("response failure=" + t.getMessage());
                LoginActivity.super.onBackPressed();
            }
        });
    }

}
