package com.kulpin.project.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.EventListFragment;
import com.kulpin.project.travelcompanion.fragment.JourneyListFragment;
import com.kulpin.project.travelcompanion.fragment.PagesContainerFragment;

public class MainActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar mToolbar;
    private EventListFragment eventListFragment;
    private JourneyListFragment journeyListFragment;
    private FragmentTransaction fragmentTransaction;
    private PagesContainerFragment pagesContainerFragment;
    private AppController appController;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initFragment();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_2);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        mToolbar.inflateMenu(R.menu.menu);
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
