package com.albertlardizabal.packoverflow.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.dialogs.EditItemDialogFragment;
import com.albertlardizabal.packoverflow.dialogs.EditListDialogFragment;
import com.albertlardizabal.packoverflow.helpers.MyApplication;
import com.albertlardizabal.packoverflow.helpers.Utils;
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.albertlardizabal.packoverflow.ui.PackingListFragment.currentPackingList;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
		SavedListsFragment.OnSavedListsFragmentListener, TemplateListsFragment.OnTemplateListsFragmentListener {

	private static final String LOG_TAG = MainActivity.class.getSimpleName();

	private FirebaseAnalytics firebaseAnalytics;
	private FirebaseAuth firebaseAuth;

	public static Toolbar toolbar;
	private FloatingActionButton fab;

	private DrawerLayout drawer;
	public static NavigationView navigationView;
	private SharedPreferences sharedPreferences;

	private MenuItem shareMenuItem;
	private MenuItem newListMenuItem;
	private MenuItem renameListMenuItem;
	private MenuItem deleteSelectedMenuItem;
	private MenuItem deleteListMenuItem;

	private MenuItem accountNavigationItem;
	private TextView accountUserName;
	private TextView accountUserEmail;

	private static final int RC_SIGN_IN = 123;

	public static final int PACKING_LIST_FRAGMENT = 1000;
	public static final int SAVED_LISTS_FRAGMENT = 1001;
	public static final int TEMPLATE_LISTS_FRAGMENT = 1002;

	public static int CURRENT_FRAGMENT = PACKING_LIST_FRAGMENT;

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (!isTablet(this)) {
			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		}

		sharedPreferences = getPreferences(Context.MODE_PRIVATE);

		configureFloatingActionButton();
		configureFirebase();
		configureNavigationDrawer();

		Boolean isFirstLoad = sharedPreferences.getBoolean(getString(R.string.preferences_is_first_load), true);
		if (isFirstLoad) {
			stageData();
		}
	}

	private void stageData() {

		ArrayList<PackingList> savedLists = Utils.stageData();
		String userId = MyApplication.getContext().getString(R.string.firebase_demo_user);
		if (firebaseAuth.getCurrentUser() != null) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			userId = user.getUid();
		}
		Log.d(LOG_TAG, "Stage Data");
		for (PackingList list : savedLists) {
			PackingListFragment.savedListsReference.child(userId).child(list.getTitle()).setValue(list);
		}
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(getString(R.string.preferences_is_first_load), false);
		editor.commit();
	}

	private void configureFloatingActionButton() {
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
	}

	private void configureFirebase() {

		firebaseAnalytics = FirebaseAnalytics.getInstance(this);
		firebaseAuth = FirebaseAuth.getInstance();
	}

	private void configureNavigationDrawer() {

		if (drawer != null) {
			ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
					this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
			drawer.addDrawerListener(toggle);
			toggle.syncState();
		}

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		configureNavigationHeaderView();
	}

	private void configureNavigationHeaderView() {
		accountNavigationItem = navigationView.getMenu().findItem(R.id.nav_account);
		accountUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
		accountUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);
		if (firebaseAuth.getCurrentUser() != null) {
			// already signed in
			FirebaseUser user = firebaseAuth.getCurrentUser();
			accountNavigationItem.setTitle(getResources().getString(R.string.sign_out));
			accountUserName.setText(user.getDisplayName());
			accountUserEmail.setText(user.getEmail());
		} else {
			Log.d(LOG_TAG, "Not signed in");
			accountNavigationItem.setTitle(getResources().getString(R.string.sign_in));
			accountUserName.setText(getResources().getString(R.string.app_name));
			accountUserEmail.setText("");
		}
	}

	@Override
	public void onBackPressed() {
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

		if (id == R.id.action_share) {
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
		navigateToFragment(SAVED_LISTS_FRAGMENT);
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
						if (deleteItem != null) {
							if (deleteItem.getIsChecked()) {
								list.getItems().remove(j);
							}
						}
					}
					PackingListFragment.updateFirebase();
					return;
				}
			}
		} else if (CURRENT_FRAGMENT == SAVED_LISTS_FRAGMENT) {
			for (int j = lists.size() - 1; j >= 0; j--) {
				PackingList deleteList = lists.get(j);
				if (deleteList != null) {
					if (deleteList.getIsChecked()) {
						lists.remove(j);
						String userId = MyApplication.getContext().getString(R.string.firebase_demo_user);
						FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
						if (firebaseAuth.getCurrentUser() != null) {
							FirebaseUser user = firebaseAuth.getCurrentUser();
							userId = user.getUid();
						}
						PackingListFragment.savedListsReference.child(userId).child(deleteList.getTitle()).removeValue();
					}
				}
			}
			PackingListFragment.updateFirebase();
		}
	}

	private void deleteList() {
		ArrayList<PackingList> lists = PackingListFragment.packingLists;
		for (int i = 0; i < lists.size(); i++) {
			PackingList list = lists.get(i);
			if (list != null) {
				if (list.getTitle().equals(currentPackingList.getTitle())) {
					lists.remove(i);

					String userId = MyApplication.getContext().getString(R.string.firebase_demo_user);
					FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
					if (firebaseAuth.getCurrentUser() != null) {
						FirebaseUser user = firebaseAuth.getCurrentUser();
						userId = user.getUid();
					}
					PackingListFragment.savedListsReference.child(userId).child(list.getTitle()).removeValue();
					navigateToFragment(SAVED_LISTS_FRAGMENT);
					return;
				}
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
		} else if (id == R.id.nav_account) {
			// TODO - account
			if (firebaseAuth.getCurrentUser() != null) {
				// already signed in
				signOut();
			} else {
				Log.d(LOG_TAG, "Not signed in");
				signIn();
			}
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

		if (drawer != null) {
			drawer.closeDrawer(GravityCompat.START);
		}
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
		shareMenuItem.setVisible(true);
	}

	private void hideMenuItems() {
		shareMenuItem.setVisible(false);
	}

	// OnSavedListsFragmentListener interface conformance
	@Override
	public void onPackingListSelected(PackingList packingList) {
		for (PackingList list : PackingListFragment.packingLists) {
			if (list.getTitle().equals(packingList.getTitle())) {
				list.setActive(true);
			} else {
				list.setActive(false);
			}
		}
		PackingListFragment.updateFirebase();
		navigateToFragment(PACKING_LIST_FRAGMENT);
	}

	// OnTemplateListsFragmentListener interface conformance
	@Override
	public void onTemplateListSelected(PackingList packingList) {
		String userId = MyApplication.getContext().getString(R.string.firebase_demo_user);
		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
		if (firebaseAuth.getCurrentUser() != null) {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			userId = user.getUid();
		}
		PackingListFragment.savedListsReference.child(userId).child(packingList.getTitle()).setValue(packingList);
		navigateToFragment(SAVED_LISTS_FRAGMENT);
	}

	// Auth
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);

			// Successfully signed in
			if (resultCode == ResultCodes.OK) {
				PackingListFragment.updateFirebase();
			} else {
				// Sign in failed
				String errorMessage = getResources().getString(R.string.toast_unknown_error);
				if (response == null) {
					// User pressed back button - cancelled
					errorMessage = getResources().getString(R.string.toast_sign_in_canceled);
				} else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
					errorMessage = getResources().getString(R.string.toast_network_problems);
				} else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					errorMessage = getResources().getString(R.string.toast_unknown_error);
				}
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		configureNavigationHeaderView();
	}

	private void signIn() {
		startActivityForResult(
				// Get an instance of AuthUI based on the default app
				AuthUI.getInstance().createSignInIntentBuilder().build(),
				RC_SIGN_IN);
	}

	private void signOut() {
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage(getResources().getString(R.string.signing_out));
		progress.show();
		AuthUI.getInstance()
				.signOut(this)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					public void onComplete(@NonNull Task<Void> task) {
						// user is now signed out
						configureNavigationHeaderView();
						stageData();
						PackingListFragment.updateFirebase();
						progress.dismiss();
					}
				});
	}
}
