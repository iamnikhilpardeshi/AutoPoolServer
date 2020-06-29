package com.project.autopooldriver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import pack.Bookings;
import pack.UserDetails;


public class Menu1 extends Fragment {

    ProgressDialog progressDialog;

    ListView list_item;
    ArrayList<Bookings> bookingsarrayList = new ArrayList<>();
    UserDetails userDetails;
    Bookings bookings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_1, container, false);
        list_item = (rootView).findViewById(R.id.list_item);
        userDetails = new UserDetails();
        userDetails.username = Settings.username;

        bookings = new Bookings();
        bookings.username = Settings.username;
        GetBookingsServlet getBookingsServlet = new GetBookingsServlet();
        getBookingsServlet.execute("");
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookings.bid = bookingsarrayList.get(position).bid;
                alertdialog();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public class GetBookingsServlet extends AsyncTask<String, Void, String> {
        //int resp = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bookingsarrayList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/GetNewRidesServlet";
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
                bookingsarrayList = (ArrayList<Bookings>) in.readObject();

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
            if (bookingsarrayList.size() > 0) {

                ArrayList<String> tempList = new ArrayList<String>();
                String status = "";
                for (int k = 0; k < bookingsarrayList.size(); k++) {
                    tempList.add("\nCustomer Name: " + bookingsarrayList.get(k).customername + "\n\nContact: " + bookingsarrayList.get(k).contact + "\n\nSource: " + bookingsarrayList.get(k).src + "\n\nDestination: " + bookingsarrayList.get(k).dest + "\n\nStatus : " + getBookingStatus(bookingsarrayList.get(k).status) + "\n\nDate : " + bookingsarrayList.get(k).dt + "\n");
                }

                list_item.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tempList));
            } else {
                Toast.makeText(getActivity(), "No bookings", Toast.LENGTH_SHORT).show();
            }


        }

    }

    String getBookingStatus(int i) {
        String status = "";
        switch (i) {
            case 0:
                status = "Pending";
                break;
            case 1:
                status = "Accepted";
                break;
            case 2:
                status = "Completed";
                break;
            case 3:
                status = "Rejected";
                break;
        }
        return status;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("New Ride / On-going Ride");
    }


    public class UpdateStatus extends AsyncTask<String, Void, String> {
        //int resp = 0;
        int resp = -1;

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/UpdateBookingStatusServlet";
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
                Toast.makeText(getActivity(), "Status updated succesfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }


    }

    public void alertdialog() {

        final String[] items = {"Accept", "Completed", "Reject"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update status")

                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getActivity(), items[which] + " is clicked", Toast.LENGTH_SHORT).show();
                        if (which == 0) {
                            bookings.status = which + 1;
                            alertFair();
                        } else {
                            bookings.status = which + 1;
                            UpdateStatus updateStatus = new UpdateStatus();
                            updateStatus.execute("");
                        }


                    }
                });

        // builder.setPositiveButton("OK", null);
        builder.setNegativeButton("CANCEL", null);
        //  builder.setNeutralButton("NEUTRAL", null);
        // builder.setPositiveButtonIcon(getResources().getDrawable(android.R.drawable.ic_menu_call, getTheme()));
        //  builder.setIcon(getResources().getDrawable(R.drawable.jd, getTheme()));

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

//        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        button.setBackgroundColor(Color.BLACK);
//        button.setPadding(0, 0, 20, 0);
//        button.setTextColor(Color.WHITE);
    }


    public void alertFair() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter fair  ₹");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().trim().isEmpty()) {
                    return;
                }
                bookings.fair = Double.valueOf(input.getText().toString().trim());
                UpdateStatus updateStatus = new UpdateStatus();
                updateStatus.execute("");
                // Toast.makeText(getActivity(), "Text entered is " + input.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}

