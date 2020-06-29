package com.project.autopooldriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static int exitCnt = 0;//press back two times to exit
    int currentScreen = 0;

    //    private int closecounter = 1;
    private ProgressDialog progressDialog;
    private int user = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

//        MenuItem nav_item1 = menuNav.findItem(R.id.nav_menu1);
//        nav_item1.setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);

       // loadMenu();

        displaySelectedScreen(R.id.nav_menu1);
    }


    void initclosecounter() {
        exitCnt = 0;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            exitCnt++;
            //System.out.println("curr: " + currentScreen);
            if (currentScreen != 0) {
                currentScreen = 0;
                if (user != 1) { // if not employee
                    displaySelectedScreen(R.id.nav_menu1);
                }

            } else {
                if (exitCnt == 2) {
                    super.onBackPressed();
                    finishAffinity();
                } else {
                    showToast("Press back again to exit");
                }
            }

        }

    }

    void showToast(String msg) {
        Toast.makeText(MainActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        initclosecounter();
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new Menu1();
                currentScreen = 0;
                break;
            case R.id.nav_menu2:
                fragment = new Menu2();
                currentScreen = -1;
                break;
            case R.id.nav_menu3:
                fragment = new Menu3();
                currentScreen = -1;
                break;
            case R.id.nav_exit:
                // System.exit(0);
                finishAffinity();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exitCnt == 2) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out); //backpressed
        } else {
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

    }

    void startProgressBar() {
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
    }

    void loadMenu() {

    }


//    void callServer(final String finalURL) {
//        startProgressBar();
//        //creating a string request to send request to the url
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //hiding the progressbar after completion
//                        progressDialog.dismiss();
//                        try {
//                            //getting the whole json object from the response
//                            JSONObject obj = new JSONObject(response);
//                            //System.out.println("" + obj);
//                            if (finalURL.contains("getAllMenuItems")) {
//                                Settings.jsonArrayAllItems = obj.getJSONArray("menuData");
//                                if (user == 1) {//if employee
//                                    displaySelectedScreen(R.id.nav_menu3);
//                                } else {
//                                    displaySelectedScreen(R.id.nav_menu1);
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //displaying the error in toast if occurrs
//                        progressDialog.dismiss();
//                        show_toast(error.getMessage());
//                    }
//                });
//
//        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//
//        //adding the string request to request queue
//        requestQueue.add(stringRequest);
//    }


    void show_toast(String msg) {
        Toast.makeText(MainActivity.this
                , "" + msg, Toast.LENGTH_SHORT).show();
    }
}
