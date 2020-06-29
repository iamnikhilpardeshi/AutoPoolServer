package com.project.autopooldriver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import pack.Bookings;
import pack.UserDetails;

public class BookAutoActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    ListView list_item;
    UserDetails userDetails;
    Bookings bookings;
    ArrayList<UserDetails> driverarrayList = new ArrayList<UserDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_auto);

        bookings = new Bookings();
        bookings.username = Settings.username;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookings.src = bundle.getString("src");
            bookings.dest = bundle.getString("dest");
        }
        list_item = findViewById(R.id.list_item);

        progressDialog = new ProgressDialog(getApplicationContext());


//        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//        startActivity(intent);

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookings.driver = driverarrayList.get(position).username;


                AlertDialog.Builder builder = new AlertDialog.Builder(BookAutoActivity.this);

                //  builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to book this auto ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                BookAuto bookAuto = new BookAuto();
                                bookAuto.execute("");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Confirm booking ?");
                alert.show();
            }
        });

        userDetails = new UserDetails();
        userDetails.location = "";
        GetDriverServlet getDriverServlet = new GetDriverServlet();
        getDriverServlet.execute("");
    }

    public class GetDriverServlet extends AsyncTask<String, Void, String> {
        //int resp = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            driverarrayList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/GetDriverServlet";
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
                driverarrayList = (ArrayList<UserDetails>) in.readObject();

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
            if (driverarrayList.size() > 0) {

                ArrayList<String> tempList = new ArrayList<String>();
                for (int k = 0; k < driverarrayList.size(); k++) {
                    tempList.add("\nDriver Name : " + driverarrayList.get(k).name + "\n\n" + driverarrayList.get(k).vehicleno + "\n");
                }

                list_item.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tempList));
            } else {
                Toast.makeText(getApplicationContext(), "No driver available", Toast.LENGTH_SHORT).show();
            }


        }

    }


    public class BookAuto extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/BookAutoServlet";
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

                out.writeObject(bookings);
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

                Toast.makeText(getApplicationContext(), "Auto booked succesfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to book", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
