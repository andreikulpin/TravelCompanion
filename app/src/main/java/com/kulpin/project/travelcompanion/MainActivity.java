package com.kulpin.project.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.EventListFragment;
import com.kulpin.project.travelcompanion.fragment.JourneyListFragment;
import com.kulpin.project.travelcompanion.fragment.PagesContainerFragment;

public class MainActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private EventListFragment eventListFragment;
    private FragmentTransaction fragmentTransaction;
    private PagesContainerFragment pagesContainerFragment;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initFragment();
        initNavigationView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment) {
                            Intent intent = new Intent(getBaseContext(), AddJourneyActivity.class);
                            startActivityForResult(intent, 1);
                        }
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment) {
                            Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
                            startActivityForResult(intent, 2);
                        }
                        break;
                    case R.id.delete:
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PagesContainerFragment) {
                            pagesContainerFragment.onDelete();
                        }
                        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment) {
                            eventListFragment.deleteEvent(0);
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

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void initFragment(){
        if(findViewById(R.id.fragment_container) != null){
            pagesContainerFragment = new PagesContainerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pagesContainerFragment).commit();
        }
    }

    public void onItemClicked(int position, long journeyId) {
        onFragmentReplace(journeyId);
    }

    public void onFragmentReplace(long journeyId){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("journeyId", journeyId);
        eventListFragment = new EventListFragment();
        eventListFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, eventListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case 1:
                ((JourneyListFragment)pagesContainerFragment.getViewPagerAdapter().
                        getItem(pagesContainerFragment.getTabLayout().getSelectedTabPosition())).
                        addNewJourney((JourneyDTO) data.getParcelableExtra(JourneyDTO.class.getCanonicalName()));
                break;
            case 2:
                eventListFragment.addNewEvent((EventDTO) data.getParcelableExtra(EventDTO.class.getCanonicalName()));
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
                case R.id.menu_context_delete:{
                    JourneyListFragment journeyListFragment = ((JourneyListFragment)pagesContainerFragment.getViewPagerAdapter().
                            getItem(pagesContainerFragment.getTabLayout().getSelectedTabPosition()));

                    journeyListFragment.deleteJourney(journeyListFragment.getItemId(journeyListFragment.getJourneyListAdapter().getSelectedPosition()));

                }
            }

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventListFragment){
            switch (item.getItemId()){
                case R.id.menu_context_delete: {
                    eventListFragment.deleteEvent(
                            eventListFragment.getItemId(eventListFragment.getEventListAdapter().getSelectedPosition()));
                }

            }
        }

        return super.onContextItemSelected(item);
    }



    public PagesContainerFragment getPagesContainerFragment() {
        return pagesContainerFragment;
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
