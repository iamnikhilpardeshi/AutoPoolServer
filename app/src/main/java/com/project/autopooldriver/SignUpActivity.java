package com.project.autopooldriver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import pack.UserDetails;

public class SignUpActivity extends AppCompatActivity {

    EditText ed_name, ed_contact, ed_email, ed_city, ed_username, ed_password, ed_vehicleno;
    Button bt_submit;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bt_submit = findViewById(R.id.bt_submit);
        ed_name = findViewById(R.id.ed_name);
        ed_contact = findViewById(R.id.ed_contact);
        ed_email = findViewById(R.id.ed_email);
        ed_city = findViewById(R.id.ed_city);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        ed_vehicleno = findViewById(R.id.ed_vehicleno);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails = new UserDetails();
                userDetails.name = ed_name.getText().toString();
                userDetails.contact = ed_contact.getText().toString();
                userDetails.email = ed_email.getText().toString();
                userDetails.city = ed_city.getText().toString();
                userDetails.username = ed_username.getText().toString();
                userDetails.password = ed_password.getText().toString();
                userDetails.type = Settings.userType;
                userDetails.vehicleno = ed_vehicleno.getText().toString();
                RegisterUser registerUser = new RegisterUser();
                registerUser.execute("");
            }
        });

    }


    public class RegisterUser extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/SignUpServlet";
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
                Toast.makeText(getApplicationContext(), "Registered succesfully", Toast.LENGTH_SHORT).show();

                finish();
            } else if (resp == 2) {
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();

               // finish();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }


    }
}
