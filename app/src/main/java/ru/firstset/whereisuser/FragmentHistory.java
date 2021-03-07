package ru.firstset.whereisuser;

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
import android.widget.Button;
import com.google.android.gms.maps.model.PolylineOptions;
import ru.firstset.whereisuser.data.location.LocationUser;
import ru.firstset.whereisuser.data.tracker.TrackAdapter;
import ru.firstset.whereisuser.data.tracker.TrackSummary;
import ru.firstset.whereisuser.data.location.LocationRepository;

public class FragmentHistory extends Fragment implements View.OnClickListener, TrackAdapter.OnItemClickListener {
    List<TrackSummary> trackSummaryList = new ArrayList<>();
    List<LocationUser> locationUserList = new ArrayList<>();
    LocationRepository locationRepository;
    View view;
    RecyclerView recyclerView;
    Button button;

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
        recyclerView = view.findViewById(R.id.listRecycler);
        TrackAdapter adapter = new TrackAdapter(getContext(), trackSummaryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        button = view.findViewById(R.id.historyButtonClear);
        button.setOnClickListener(this);
    }

    private void doOnClick(Long id) {
        Log.v("doOnClick", String.valueOf(id));
    }

    private void initialData() {
        locationUserList = locationRepository.readAllLocations();
        if (locationUserList.size() != 0) {
            trackSummaryList.clear();
            for (int i = 0; i < locationUserList.size(); i++) {
                TrackSummary trackSummary = new TrackSummary(locationUserList.get(i).track, locationUserList.get(i).id, locationUserList.get(i).time);
                trackSummaryList.add(trackSummary);
            }
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.historyButtonClear: {
               break;
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        String item = trackSummaryList.get(position).getDate();
        PolylineOptions polylineOptions = new PolylineOptions();
        MainActivity.fragmentManager.popBackStack();
        MyMapFragment.listLocationUser = locationRepository.readLocation(trackSummaryList.get(position).getIdTrack());
        Log.v("getIdTrack", String.valueOf(trackSummaryList.get(position).getIdTrack()));

    }
}