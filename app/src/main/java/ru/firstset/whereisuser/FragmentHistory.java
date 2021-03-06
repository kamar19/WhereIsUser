package ru.firstset.whereisuser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ru.firstset.whereisuser.data.LocationUser;
import ru.firstset.whereisuser.data.TrackAdapter;
import ru.firstset.whereisuser.data.TrackSummary;
import ru.firstset.whereisuser.location.LocationRepository;
import ru.firstset.whereisuser.util.DialogFragment;


public class FragmentHistory extends Fragment implements View.OnClickListener, TrackAdapter.OnItemClickListener {
    List<TrackSummary> trackSummaryList = new ArrayList<>();
    List<LocationUser> locationUserList = new ArrayList<>();
    LocationRepository locationRepository;
    View view;
    RecyclerView recyclerView;
    Button button;
//    public static FragmentHistory newInstance() {
//        FragmentHistory fragment = new FragmentHistory();
//        return fragment;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        locationRepository = new LocationRepository(getActivity());
        this.view = view;
        initialData();


//        recyclerView = this.<RecyclerView>findViewById(R.id.listRecycler);
        recyclerView = view.findViewById(R.id.listRecycler);


        TrackAdapter adapter = new TrackAdapter(getContext(), trackSummaryList, this);
//                new TrackAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                // TODO Handle item click
//                Log.e("@@@@@",""+position);
//            }
//        }));


//        ItemClickSupport.addTo(mRecyclerView)
//                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                    @Override
//                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                        // do it
//                    }
//                });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(getActivity(), recyclerView ,
//                new RecyclerView.OnI OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        // do whatever
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );
        button = view.findViewById(R.id.historyButtonClear);
        button.setOnClickListener(this);
    }


    private void doOnClick(Long id) {
        Log.v("doOnClick", String.valueOf(id));

    }

    private void initialData() {

        locationUserList = locationRepository.readAllLocations();
//        int count = 0;
//        int track = 0;
        if (locationUserList.size() != 0) {
            trackSummaryList.clear();
            for (int i = 0; i < locationUserList.size(); i++) {
//            track = locationUserList.get(i).track;
//            if (track)

                TrackSummary trackSummary = new TrackSummary(locationUserList.get(i).track, locationUserList.get(i).id, locationUserList.get(i).time);
                trackSummaryList.add(trackSummary);

            }
            Log.v("initialData()", String.valueOf(trackSummaryList.size()));
            Log.v("getDate()", String.valueOf(trackSummaryList.get(0).getDate()));
            Log.v("getIdTrack()", String.valueOf(trackSummaryList.get(0).getIdTrack()));
            Log.v("getCountPoints()", String.valueOf(trackSummaryList.get(0).getCountPoints()));
        }

    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.historyButtonClear: {
                Log.v("historyButtonClear", "1");


//                locationRepository.deleteAllLocations();
//                DialogFragment dialogFragment = new DialogFragment();
//                dialogFragment.show(MainActivity.fragmentManager, MainActivity.fragmentManager.beginTransaction(),"tt");

//
//                startActivity(new Intent(
//                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;

            }
        }
    }

    @Override
    public void onItemClick(int position) {
//        int itemPosition = recyclerView.getChildLayoutPosition(view);
        String item = trackSummaryList.get(position).getDate();
        Log.v("getDate", String.valueOf(trackSummaryList.get(position).getDate()));
        Log.v("getCountPoints", String.valueOf(trackSummaryList.get(position).getCountPoints()));
        PolylineOptions polylineOptions = new PolylineOptions();
//        Polyline polylineTemp = new  Polyline();
        MainActivity.fragmentManager.popBackStack();

        List<LocationUser> listLocationUser = locationRepository.readLocation(trackSummaryList.get(position).getIdTrack());
        Polyline polyline1;

        for (int i = 0; i < listLocationUser.size() - 1; i++) {
            polyline1 = MyMapFragment.googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(new LatLng(listLocationUser.get(i).latitude, listLocationUser.get(i).longitude))
            );
        }
    }
}