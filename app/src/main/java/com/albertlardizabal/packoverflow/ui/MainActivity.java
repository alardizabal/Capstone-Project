package com.albertlardizabal.packoverflow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.dialogs.EditItemDialogFragment;
import com.albertlardizabal.packoverflow.dialogs.EditListDialogFragment;
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FirebaseAnalytics firebaseAnalytics;
    public static Toolbar toolbar;
    private FloatingActionButton fab;
    private MenuItem calendarMenuItem;
    private MenuItem shareMenuItem;
    private MenuItem newListMenuItem;
    private MenuItem renameListMenuItem;
    private MenuItem deleteSelectedMenuItem;
    private MenuItem deleteListMenuItem;

    private static final int RC_SIGN_IN = 123;

    private static final int PACKING_LIST_FRAGMENT = 1000;
    private static final int SAVED_LISTS_FRAGMENT = 1001;
    private static final int TEMPLATE_LISTS_FRAGMENT = 1002;

    private static int CURRENT_FRAGMENT = PACKING_LIST_FRAGMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURRENT_FRAGMENT == PACKING_LIST_FRAGMENT) {
                    DialogFragment newFragment = new EditItemDialogFragment();
                    newFragment.show(getSupportFragmentManager(), "editListItem");
                } else if (CURRENT_FRAGMENT == SAVED_LISTS_FRAGMENT) {
                    makeNewList();
                }
            }
        });

        // Set up Firebase
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set up navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        calendarMenuItem = menu.findItem(R.id.action_calendar);
        shareMenuItem = menu.findItem(R.id.action_share);

        newListMenuItem = menu.findItem(R.id.action_new_list);
        renameListMenuItem = menu.findItem(R.id.action_rename_list);
        deleteSelectedMenuItem = menu.findItem(R.id.action_delete_selected);
        deleteListMenuItem = menu.findItem(R.id.action_delete_list);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            return true;
        } else if (id == R.id.action_share) {
            setShareIntent();
            return true;
        } else if (id == R.id.action_new_list) {
            makeNewList();
            return true;
        } else if (id == R.id.action_rename_list) {
            return true;
        } else if (id == R.id.action_delete_selected) {
            return true;
        } else if (id == R.id.action_delete_list) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent() {
        ArrayList<PackingListItem> list = PackingListFragment.currentListItems;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        StringBuilder stringBuilder = new StringBuilder();
        for (PackingListItem item : list) {
            stringBuilder.append(item.getTitle());
            stringBuilder.append("\n");
        }
        String currentListString = stringBuilder.toString();

        shareIntent.putExtra(Intent.EXTRA_TEXT, currentListString);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_list_title)));
    }

    private void makeNewList() {
        DialogFragment newFragment = new EditListDialogFragment();
        newFragment.show(getSupportFragmentManager(), "editList");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new Fragment();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_current_list) {
            fragment = new PackingListFragment();
            fab.setVisibility(View.VISIBLE);
            CURRENT_FRAGMENT = PACKING_LIST_FRAGMENT;
            showMenuItems();
            newListMenuItem.setVisible(true);
            renameListMenuItem.setVisible(true);
            deleteListMenuItem.setVisible(true);
        } else if (id == R.id.nav_saved_lists) {
            fragment = new SavedListsFragment();
            fab.setVisibility(View.VISIBLE);
            CURRENT_FRAGMENT = SAVED_LISTS_FRAGMENT;
            hideMenuItems();
            newListMenuItem.setVisible(false);
            renameListMenuItem.setVisible(false);
            deleteListMenuItem.setVisible(false);
        } else if (id == R.id.nav_template_lists) {
            fragment = new TemplateListsFragment();
            fab.setVisibility(View.GONE);
            CURRENT_FRAGMENT = TEMPLATE_LISTS_FRAGMENT;
            hideMenuItems();
            newListMenuItem.setVisible(false);
            renameListMenuItem.setVisible(false);
            deleteListMenuItem.setVisible(false);
        }

        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMenuItems() {
        calendarMenuItem.setVisible(true);
        shareMenuItem.setVisible(true);
    }

    private void hideMenuItems() {
        calendarMenuItem.setVisible(false);
        shareMenuItem.setVisible(false);
    }
}
