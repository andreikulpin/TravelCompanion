package com.kulpin.project.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.kulpin.project.travelcompanion.adapter.EventTypeListAdapter;
import com.kulpin.project.travelcompanion.dto.EventDTO;
import com.kulpin.project.travelcompanion.dto.JourneyDTO;
import com.kulpin.project.travelcompanion.fragment.BasicCreateFragment;
import com.kulpin.project.travelcompanion.fragment.CreateLocalEventFragment;
import com.kulpin.project.travelcompanion.fragment.CreateMoveEventFragment;
import com.kulpin.project.travelcompanion.utilities.Constants;

import java.util.ArrayList;

public class AddEventActivity extends FragmentActivity {
    private static final int LAYOUT = R.layout.activity_add_event;
    private Toolbar toolbar;
    private EventDTO newEvent;
    private JourneyDTO journey;
    private BasicCreateFragment createFragment;
    private FragmentTransaction fragmentTransaction;
    private ListView eventTypeListView;
    private BaseAdapter baseAdapter;
    private ArrayList<String> eventTypeList;
    private EventTypeListAdapter eventTypeListAdapter;
    private int selectedType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindActivity();
        initToolbar();
        if (getIntent().getAction() != null && getIntent().getAction().equals(Constants.Actions.EDIT_EVENT_ACTION)) {
            fillFields();
        }
    }

    private void fillFields() {
        EventDTO currentEvent = getIntent().getParcelableExtra(EventDTO.class.getCanonicalName());
        newEvent.setId(currentEvent.getId());

        Log.d("tclog", "" + currentEvent.getType());
        switch (currentEvent.getType()){
            case Constants.EventType.TYPE_PLANE:
                createFragment = new CreateMoveEventFragment();
                break;
            case Constants.EventType.TYPE_TRAIN:
                createFragment = new CreateMoveEventFragment();
                break;
            case Constants.EventType.TYPE_BUS:
                createFragment = new CreateMoveEventFragment();
                break;
            case Constants.EventType.TYPE_MUSEUM:
                createFragment = new CreateLocalEventFragment();
                break;
            case Constants.EventType.TYPE_CINEMA:
                createFragment = new CreateLocalEventFragment();
                break;
        }

        toolbar.inflateMenu(R.menu.menu_create);
        Bundle bundle = new Bundle();
        bundle.putString("action", Constants.Actions.EDIT_EVENT_ACTION);
        bundle.putParcelable(EventDTO.class.getCanonicalName(), currentEvent);
        bundle.putParcelable(JourneyDTO.class.getCanonicalName(), journey);
        createFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_add, createFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void bindActivity(){
        newEvent = new EventDTO();
        journey = getIntent().getParcelableExtra(JourneyDTO.class.getCanonicalName());
        eventTypeListAdapter = new EventTypeListAdapter(this);

        eventTypeListView = (ListView)findViewById(R.id.list_type_event);
        eventTypeListView.setAdapter(eventTypeListAdapter);
        eventTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toolbar.inflateMenu(R.menu.menu_create);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (findViewById(R.id.fragment_container_add) != null) {
                    switch (++position){
                        case Constants.EventType.TYPE_PLANE:
                            createFragment = new CreateMoveEventFragment();
                            selectedType = Constants.EventType.TYPE_PLANE;
                            break;
                        case Constants.EventType.TYPE_TRAIN:
                            createFragment = new CreateMoveEventFragment();
                            selectedType = Constants.EventType.TYPE_TRAIN;
                            break;
                        case Constants.EventType.TYPE_BUS:
                            createFragment = new CreateMoveEventFragment();
                            selectedType = Constants.EventType.TYPE_BUS;
                            break;
                        case Constants.EventType.TYPE_MUSEUM:
                            createFragment = new CreateLocalEventFragment();
                            selectedType = Constants.EventType.TYPE_MUSEUM;
                            break;
                        case Constants.EventType.TYPE_CINEMA:
                            createFragment = new CreateLocalEventFragment();
                            selectedType = Constants.EventType.TYPE_CINEMA;
                            break;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("action", Constants.Actions.CREATE_EVENT_ACTION);
                    bundle.putParcelable(JourneyDTO.class.getCanonicalName(), journey);
                    createFragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.fragment_container_add, createFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.create_event));
        //toolbar.inflateMenu(R.menu.menu_create);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.done: {
                        if (getSupportFragmentManager().getFragments() == null
                                || getSupportFragmentManager().getFragments().get(0) == null) {
                        }
                        else {
                            if (createFragment.checkValues()){
                                newEvent = createFragment.getNewEvent();
                                if (selectedType != 0)
                                    newEvent.setType(selectedType);

                                Intent intent = new Intent();
                                intent.putExtra(EventDTO.class.getCanonicalName(), newEvent);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    }
                    break;
                }
                return false;
            }
        });

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        /*if (getSupportFragmentManager().getFragments().get(0) != null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("move");
            //fragmentTransaction.remove(fragment);
            //fragmentTransaction.detach(createFragment);
            fragmentTransaction.remove(createFragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().popBackStack();


            Log.d("tclog", "" + getSupportFragmentManager().getFragments().get(0));
            Log.d("tclog", "" + ((FrameLayout) findViewById(R.id.fragment_container_add)).getChildCount());
            Log.d("tclog", "" + getSupportFragmentManager().getFragments().size());
            return;
        }*/
        toolbar.getMenu().clear();
        super.onBackPressed();
    }


}
