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
import com.albertlardizabal.packoverflow.helpers.Utils;
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static com.albertlardizabal.packoverflow.ui.PackingListFragment.currentPackingList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    public static final int PACKING_LIST_FRAGMENT = 1000;
    public static final int SAVED_LISTS_FRAGMENT = 1001;
    public static final int TEMPLATE_LISTS_FRAGMENT = 1002;

    public static int CURRENT_FRAGMENT = PACKING_LIST_FRAGMENT;

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

        // TODO - Stage data
        ArrayList<PackingList> savedLists = Utils.stageData();
        for (PackingList list : savedLists) {
            PackingListFragment.savedListsReference.child(list.getTitle()).setValue(list);
        }
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

        if (CURRENT_FRAGMENT == PACKING_LIST_FRAGMENT) {
            configureViewForPackingListFragment();
        } else if (CURRENT_FRAGMENT == SAVED_LISTS_FRAGMENT) {
            configureViewForSavedListsFragment();
        } else if (CURRENT_FRAGMENT == TEMPLATE_LISTS_FRAGMENT) {
            configureViewForTemplateListsFragment();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_calendar) {
            return true;
        } else if (id == R.id.action_share) {
            setShareIntent();
            return true;
        } else if (id == R.id.action_new_list) {
            makeNewList();
            return true;
        } else if (id == R.id.action_rename_list) {
            renameList();
            return true;
        } else if (id == R.id.action_delete_selected) {
            deleteSelectedItems();
            return true;
        } else if (id == R.id.action_delete_list) {
            deleteList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        DialogFragment dialogFragment = new EditListDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "newList");
    }

    private void renameList() {
        Bundle itemBundle = new Bundle();
        itemBundle.putParcelable("packingList", currentPackingList);
        DialogFragment dialogFragment = new EditListDialogFragment();
        dialogFragment.setArguments(itemBundle);
        dialogFragment.show(getSupportFragmentManager(), "editList");
    }

    private void deleteSelectedItems() {
        ArrayList<PackingList> lists = PackingListFragment.packingLists;
        if (CURRENT_FRAGMENT == PACKING_LIST_FRAGMENT) {
            for (int i = 0; i < lists.size(); i++) {
                PackingList list = lists.get(i);
                if (list.getTitle().equals(currentPackingList.getTitle())) {
                    for (int j = list.getItems().size() - 1; j >= 0; j--) {
                        PackingListItem deleteItem = list.getItems().get(j);
                        if (deleteItem.getIsChecked()) {
                            list.getItems().remove(j);
                        }
                    }
                    PackingListFragment.updateFirebase();
                    return;
                }
            }
        } else if (CURRENT_FRAGMENT == SAVED_LISTS_FRAGMENT) {
            for (int j = lists.size() - 1; j >= 0; j--) {
                PackingList deleteList = lists.get(j);
                if (deleteList.getIsChecked()) {
                    lists.remove(j);
                }
            }
            PackingListFragment.updateFirebase();
        }
    }

    private void deleteList() {
        ArrayList<PackingList> lists = PackingListFragment.packingLists;
        for (int i = 0; i < lists.size(); i++) {
            PackingList list = lists.get(i);
            if (list.getTitle().equals(currentPackingList.getTitle())) {
                for (int j = list.getItems().size() - 1; j >= 0; j--) {
                    PackingListItem deleteItem = list.getItems().get(j);
                    if (deleteItem.getIsChecked()) {
                        list.getItems().remove(j);
                    }
                }
                PackingListFragment.updateFirebase();
                return;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_current_list) {
            navigateToFragment(PACKING_LIST_FRAGMENT);
        } else if (id == R.id.nav_saved_lists) {
            navigateToFragment(SAVED_LISTS_FRAGMENT);
        } else if (id == R.id.nav_template_lists) {
            navigateToFragment(TEMPLATE_LISTS_FRAGMENT);
        }
        return true;
    }

    private void navigateToFragment(int fragmentId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new Fragment();

        if (fragmentId == PACKING_LIST_FRAGMENT) {
            fragment = new PackingListFragment();
            CURRENT_FRAGMENT = PACKING_LIST_FRAGMENT;
            configureViewForPackingListFragment();
        } else if (fragmentId == SAVED_LISTS_FRAGMENT) {
            fragment = new SavedListsFragment();
            CURRENT_FRAGMENT = SAVED_LISTS_FRAGMENT;
            configureViewForSavedListsFragment();
        } else if (fragmentId == TEMPLATE_LISTS_FRAGMENT) {
            fragment = new TemplateListsFragment();
            CURRENT_FRAGMENT = TEMPLATE_LISTS_FRAGMENT;
            configureViewForTemplateListsFragment();
        }

        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void configureViewForPackingListFragment() {
        fab.setVisibility(View.VISIBLE);
        showMenuItems();
        newListMenuItem.setVisible(true);
        renameListMenuItem.setVisible(true);
        deleteListMenuItem.setVisible(true);
        deleteSelectedMenuItem.setVisible(true);
    }

    private void configureViewForSavedListsFragment() {
        toolbar.setTitle(R.string.saved_lists);
        fab.setVisibility(View.VISIBLE);
        hideMenuItems();
        newListMenuItem.setVisible(false);
        renameListMenuItem.setVisible(false);
        deleteListMenuItem.setVisible(false);
        deleteSelectedMenuItem.setVisible(true);
    }

    private void configureViewForTemplateListsFragment() {
        toolbar.setTitle(R.string.template_lists);
        fab.setVisibility(View.GONE);
        hideMenuItems();
        newListMenuItem.setVisible(false);
        renameListMenuItem.setVisible(false);
        deleteListMenuItem.setVisible(false);
        deleteSelectedMenuItem.setVisible(false);
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
