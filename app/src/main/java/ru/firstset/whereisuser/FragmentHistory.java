package ru.firstset.whereisuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import ru.firstset.whereisuser.data.LocationUser;
import ru.firstset.whereisuser.data.TrackAdapter;
import ru.firstset.whereisuser.data.TrackSummary;
import ru.firstset.whereisuser.location.LocationRepository;


public class FragmentHistory extends Fragment implements View.OnClickListener  {
    List<TrackSummary> trackSummaryList = new ArrayList<>();
    List<LocationUser> locationUserList = new ArrayList<>();
    LocationRepository locationRepository;

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
        initialData();

//        recyclerView = this.<RecyclerView>findViewById(R.id.listRecycler);
        recyclerView = view.findViewById(R.id.listRecycler);
        TrackAdapter adapter = new TrackAdapter(getContext(), trackSummaryList);
        recyclerView.setAdapter(adapter);
        button = view.findViewById(R.id.historyButtonClear);
        button.setOnClickListener(this);
    }


    private void initialData() {

        locationUserList = locationRepository.readAllLocations();
//        int count = 0;
//        int track = 0;
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

    @Override
    public void onClick(View v) {

    }
}