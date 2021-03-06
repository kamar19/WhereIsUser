package ru.firstset.whereisuser.data.tracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.firstset.whereisuser.R;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> implements View.OnClickListener {

    private LayoutInflater inflater;
    private List<TrackSummary> listTrackSummaries;
    public OnItemClickListener itemClickListener;
    int currentPos;

    public TrackAdapter(Context context, List<TrackSummary> listTrackSummaries, OnItemClickListener itemClickListener) {
        this.listTrackSummaries = listTrackSummaries;
        this.inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;

    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return listTrackSummaries.size();
    }

    public TrackSummary getItem(int position) {
        return listTrackSummaries.get(position);
    }

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, final int position) {
        holder.bind(getItem(position), itemClickListener);
        holder.itemView.setOnClickListener( new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Log.v("onClick", String.valueOf(position));
                            itemClickListener.onItemClick(position);
                        }
                    });
    }

    @Override
    public void onClick(View v) {
        Log.v("onClick2", String.valueOf(currentPos));
        this.itemClickListener.onItemClick(currentPos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView idTrack, dateTrack, countPoints;
        ViewHolder(View view) {
            super(view);
            idTrack = view.findViewById(R.id.listItemTrackId);
            dateTrack = view.findViewById(R.id.listItemDate);
            countPoints = view.findViewById(R.id.listItemCountPoints);
        }

        public void bind(TrackSummary trackSummary, OnItemClickListener clickListener) {
            idTrack.setText(String.valueOf(trackSummary.getIdTrack()));
            dateTrack.setText(trackSummary.getDate());
            countPoints.setText(String.valueOf(trackSummary.getCountPoints()));
            currentPos = trackSummary.getIdTrack();
        }
    }
}
