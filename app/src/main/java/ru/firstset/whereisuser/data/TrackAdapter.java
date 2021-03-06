package ru.firstset.whereisuser.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.firstset.whereisuser.R;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<TrackSummary> listTrackSummaries;

    public TrackAdapter(Context context, List<TrackSummary> listTrackSummaries) {
        this.listTrackSummaries = listTrackSummaries;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public void bindMovie(List<TrackSummary> listTrackSummaries) {
        this.listTrackSummaries = listTrackSummaries;
        notifyDataSetChanged();
    }

    public void bind(List<TrackSummary> listTrackSummaries) {
        this.listTrackSummaries = listTrackSummaries;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, int position) {
        TrackSummary trackSummary = listTrackSummaries.get(position);
        holder.idTrack.setText(String.valueOf(trackSummary.getIdTrack()));
        holder.dateTrack.setText(trackSummary.getDate());
        holder.countPoints.setText(String.valueOf(trackSummary.getCountPoints()));


        holder.bind(getItem(position.toInt()));
        holder.itemView.setOnClickListener(
            someClickListener(position.toLong(),it);



    }



    @Override
    public int getItemCount() {
        return listTrackSummaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView idTrack, dateTrack, countPoints;

        ViewHolder(View view) {
            super(view);
            idTrack = view.findViewById(R.id.listItemTrackId);
            dateTrack = view.findViewById(R.id.listItemDate);
            countPoints = view.findViewById(R.id.listItemCountPoints);
        }
    }


}
