package ru.firstset.whereisuser.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

//    public void OnItemClickListener() {
//        public void onItemClick(int position);
//    }

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
//        OnClickListener mOnClickListener = new MyOnClickListener();
//        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, final int position) {
        Log.v("onBindViewHolder", String.valueOf(position));
        holder.bind(getItem(position), itemClickListener);
//        holder.itemView.setOnClickListener(itemClickListener.onItemClick(listTrackSummaries.get(position).getIdTrack()));
//        holder.itemView.setOnClickListener {
//            someClickListener(position.toLong(),it)
//        }
        holder.itemView.setOnClickListener( new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Log.v("onClick3", String.valueOf(position));

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
            Log.v("bind-IdTrack", String.valueOf(trackSummary.getIdTrack()));

            idTrack.setText(String.valueOf(trackSummary.getIdTrack()));
            dateTrack.setText(trackSummary.getDate());
            countPoints.setText(String.valueOf(trackSummary.getCountPoints()));
            currentPos = trackSummary.getIdTrack();

//            itemView.setOnClickListener(
//                    new View.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(View v)
//                        {
//                            Log.v("onClick3", "-3");
//
//                            clickListener.onItemClick(currentPos);
//                        }
//                    });

//                    new View.OnClickListener()
//                    {
//
//                    }
////                        @Override
//                        public void onClick(View v)
//                        {
//                            int pos = itemView.getId();
//                            int pos2 = pos - 1;
//
//                            Log.v("onClick3", String.valueOf(pos2));
//
//                            clickListener.onItemClick(pos2);
//                        }
//                    });
        }
    }
}
