package com.project.autopooldriver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Menu2 extends Fragment {
    ListView list_item;
    ArrayList<Bookings> bookingsarrayList = new ArrayList<>();
    UserDetails userDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu_2, container, false);
        list_item = (rootView).findViewById(R.id.list_item);
        userDetails = new UserDetails();
        userDetails.username = Settings.username;
        GetBookingsServlet getBookingsServlet = new GetBookingsServlet();
        getBookingsServlet.execute("");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Rides history");
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

                String urlstr = "http://" + Settings.ip + "/AutoPoolServer/GetDriverBookingsServlet";
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
                    tempList.add("\nCustomer Name: " + bookingsarrayList.get(k).customername + "\n\nContact: " + bookingsarrayList.get(k).contact + "\n\nSource: " + bookingsarrayList.get(k).src + "\n\nDestination: " + bookingsarrayList.get(k).dest + "\n\nStatus : " + getBookingStatus(bookingsarrayList.get(k).status) + "\n\nDate : " + bookingsarrayList.get(k).dt + "\n\nFair: " + bookingsarrayList.get(k).fair);
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

}