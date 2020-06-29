package com.project.autopooldriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import pack.UserDetails;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_username, ed_password;
    private Button bt_login;
    TextView tv_newuser;
    private ProgressDialog progressDialog;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);

        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed_username.getText().toString().isEmpty() || ed_password.getText().toString().isEmpty()) {
                    show_toast("Invalid username or password");
                    return;
                }
                userDetails = new UserDetails();
                userDetails.username = ed_username.getText().toString();
                userDetails.password = ed_password.getText().toString();
                userDetails.type = Settings.userType;
                login();
            }
        });
        tv_newuser = findViewById(R.id.tv_newuser);
        tv_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkDate();
    }


    void login() {

        Login login = new Login();
        login.execute("");
        //  System.out.println(""+finalURL);
        // callServer(finalURL);
    }

    void startProgressBar() {
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public class Login extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/LoginServlet";
                URL url = new URL(urlstr);
                URLConnection connection = url.openConnection();

                connection.setDoOutput(true);
                connection.setDoInput(true);

                // don't use a cached version of URL connection
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // specify the content type that binary data is sent
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                // send and serialize the object

                out.writeObject(userDetails);
                out.close();

                // define a new ObjectInputStream on the input stream
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                // receive and deserialize the object, note the cast
                resp = (int) in.readObject();

                in.close();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (resp == 1) {
                Settings.username = ed_username.getText().toString();
                Toast.makeText(getApplicationContext(), "Login succesfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_SHORT).show();
            }


        }


    }

    void show_toast(String msg) {
        Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
    }
}
