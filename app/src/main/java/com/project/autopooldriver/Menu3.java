package com.project.autopooldriver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import pack.UserDetails;


public class Menu3 extends Fragment {

    UserDetails userDetails;
    EditText ed_password;
    Button btsubmit, btsubmitlocation;
    private Spinner spinner_src;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_3, container, false);
        userDetails = new UserDetails();
        userDetails.username = Settings.username;
        ed_password = (rootView).findViewById(R.id.ed_password);
        spinner_src = (rootView).findViewById(R.id.spinner_src);
        spinner_src.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, Settings.locations));
        btsubmit = (rootView).findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_password.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                userDetails.password = ed_password.getText().toString();
                ChangePassword changePassword = new ChangePassword();
                changePassword.execute("");
            }
        });

        btsubmitlocation = (rootView).findViewById(R.id.btsubmitlocation);
        btsubmitlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails.location = spinner_src.getSelectedItem().toString();
                ChangeLocationPassword changeLocationPassword = new ChangeLocationPassword();
                changeLocationPassword.execute("");
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Account");
    }


    public class ChangePassword extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/ChangePasswordServlet";
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
                Toast.makeText(getActivity(), "Password changed succesfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }


    }

    public class ChangeLocationPassword extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/ChangeLocationServlet";
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
                Toast.makeText(getActivity(), "Location changed succesfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }


    }
}