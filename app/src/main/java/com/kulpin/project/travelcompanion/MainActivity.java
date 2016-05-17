package com.kulpin.project.travelcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.EventListFragment;
import com.kulpin.project.travelcompanion.fragment.JourneyListFragment;
import com.kulpin.project.travelcompanion.fragment.PagesContainerFragment;
import com.kulpin.project.travelcompanion.utilities.Constants;

public class MainActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private EventListFragment eventListFragment;
    private FragmentTransaction fragmentTransaction;
    private PagesContainerFragment pagesContainerFragment;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView usernameNav;
    private TextView emailNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initUser();
        initToolbar();
        initFragment();
        initNavigationView();
    }


    /*
    * Check if user account exists on this device
    * if account is empty calls login activity*/
    private void initUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
        if (sharedPreferences.getLong("userId", 0) == 0){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.RequestCodes.LOGIN_REQUEST);
        }
    }

    /*toolbar initialization*/
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.journeys);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment) {
                            Intent intent = new Intent(getBaseContext(), AddJourneyActivity.class);
                            startActivityForResult(intent, Constants.RequestCodes.JOURNEY_REQUEST);
                        }
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment) {
                            Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
                            intent.putExtra(JourneyDTO.class.getCanonicalName(), eventListFragment.getJourney());
                            startActivityForResult(intent, Constants.RequestCodes.EVENT_REQUEST);
                        }
                        break;
                    case R.id.refresh:
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment) {
                            pagesContainerFragment.onRefresh();
                        }
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment) {
                            eventListFragment.syncEventList();
                        }
                        break;
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu_main);
    }

    /*navigation initialization*/
    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_main);
        navigationView.inflateMenu(R.menu.menu_navigation_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.logout:
                        SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("userId", 0);
                        editor.putString("username", "");
                        editor.putString("email", "");
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        initUser();
                        break;
                }
                return true;
            }
        });

        navigationView.inflateHeaderView(R.layout.navigation_header);
        SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
        usernameNav = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username_navigation);
        usernameNav.setText(sharedPreferences.getString("username", ""));
        emailNav = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_navigation);
        emailNav.setText(sharedPreferences.getString("email", ""));
    }

    /*initialization of main fragment with the list of journeys*/
    private void initFragment(){
        if(findViewById(R.id.fragment_container) != null){
            pagesContainerFragment = new PagesContainerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pagesContainerFragment).commit();
        }
    }

    /*if journey list item selected, fragment with journey lists replaces by
    * EventListFragment*/
    public void onReplaceFragment(int position, JourneyDTO journey) {
        onFragmentReplace(journey);
    }
    public void onFragmentReplace(JourneyDTO journey){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("journeyId", journey.getId());
        bundle.putParcelable(JourneyDTO.class.getCanonicalName(), journey);
        eventListFragment = new EventListFragment();
        eventListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, eventListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        toolbar.setTitle(R.string.events);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case Constants.RequestCodes.JOURNEY_REQUEST:
                ((JourneyListFragment)pagesContainerFragment.getViewPagerAdapter().
                        getItem(pagesContainerFragment.getTabLayout().getSelectedTabPosition())).
                        addNewJourney((JourneyDTO) data.getParcelableExtra(JourneyDTO.class.getCanonicalName()));
                break;
            case Constants.RequestCodes.EVENT_REQUEST:
                eventListFragment.addNewEvent((EventDTO) data.getParcelableExtra(EventDTO.class.getCanonicalName()));
                break;
            case Constants.RequestCodes.LOGIN_REQUEST:
                SharedPreferences sharedPreferences = getSharedPreferences("TCPrefs", MODE_PRIVATE);
                usernameNav.setText(sharedPreferences.getString("username", ""));
                emailNav.setText(sharedPreferences.getString("email", ""));
                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment) {
                    pagesContainerFragment.onRefresh();
                }
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Options");
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ((getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment))
            switch (item.getItemId()){
                case R.id.delete_context:{
                    JourneyListFragment journeyListFragment = ((JourneyListFragment)pagesContainerFragment.getViewPagerAdapter().
                            getItem(pagesContainerFragment.getTabLayout().getSelectedTabPosition()));

                    journeyListFragment.deleteJourney(journeyListFragment.getItemId(journeyListFragment.getJourneyListAdapter().getSelectedPosition()));

                }
                break;

                case R.id.edit_context:{
                    Intent intent = new Intent(this, AddJourneyActivity.class);
                    intent.setAction(Constants.Actions.EDIT_JOURNEY_ACTION);
                    JourneyListFragment journeyListFragment = ((JourneyListFragment)pagesContainerFragment.getViewPagerAdapter().
                            getItem(pagesContainerFragment.getTabLayout().getSelectedTabPosition()));
                    JourneyDTO journey = journeyListFragment.getJourneyByPosition(journeyListFragment.getJourneyListAdapter().getSelectedPosition());
                    intent.putExtra(JourneyDTO.class.getCanonicalName(), journey);
                    startActivityForResult(intent, Constants.RequestCodes.JOURNEY_REQUEST);
                }
                break;
            }

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment){
            switch (item.getItemId()){
                case R.id.delete_context: {
                    eventListFragment.deleteEvent(
                            eventListFragment.getItemId(eventListFragment.getEventListAdapter().getSelectedPosition()));
                }
                break;
                case R.id.edit_context:{
                    Intent intent = new Intent(this, AddEventActivity.class);
                    intent.setAction(Constants.Actions.EDIT_EVENT_ACTION);
                    EventDTO event = eventListFragment.getEventByPosition(eventListFragment.getEventListAdapter().getSelectedPosition());
                    intent.putExtra(EventDTO.class.getCanonicalName(), event);
                    intent.putExtra(JourneyDTO.class.getCanonicalName(), eventListFragment.getJourney());
                    startActivityForResult(intent, Constants.RequestCodes.EVENT_REQUEST);
                }
            }

        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment) {
            toolbar.setTitle(R.string.journeys);
        }
        super.onBackPressed();
    }

    /*private class EventTask extends AsyncTask<Void, Void, EventDTO> {
        @Override
        protected EventDTO doInBackground(Void... params) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return template.getForObject(Constants.URL.GET_ALL_EVENTS, EventDTO.class);
        }

        @Override
        protected void onPostExecute(EventDTO list) {
        }
    }*/



}
