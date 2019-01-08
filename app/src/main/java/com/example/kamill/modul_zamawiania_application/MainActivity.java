package com.example.kamill.modul_zamawiania_application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);

        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nowy_produkt) {
            setTitle("Dodaj produkt");
            nowy_produkt warehouse = new nowy_produkt();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        } else if (id == R.id.nav_stan_magazynu) {
            setTitle("Stan magazynu");
            stan_magazynu warehouse = new stan_magazynu();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        } else if (id == R.id.nav_nowe_zamowienie) {
            setTitle("Nowe zamówienie");
            nowe_zamowienie warehouse = new nowe_zamowienie();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        } else if (id == R.id.nav_zamowienia) {
            setTitle("Zamówienia");
            zamowienia_magazynu warehouse = new zamowienia_magazynu();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        } else if (id == R.id.nav_stan_hurtowni) {
            setTitle("Stan hurtowni");
            stan_hurtowni warehouse = new stan_hurtowni();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        } else if (id == R.id.nav_oczekujące_zamowienia) {
            setTitle("Zamówienia");
            zamowienia_hurtownia warehouse = new zamowienia_hurtownia();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.fragment, warehouse).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
