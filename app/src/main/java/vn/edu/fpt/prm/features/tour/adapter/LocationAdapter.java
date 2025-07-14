package vn.edu.fpt.prm.features.tour.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.features.tour.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> locationList = new ArrayList<>();

    public void setLocationList(List<Location> list) {
        if (list != null) {
            this.locationList = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.tvDescription.setText(location.getDescription());
        holder.tvDay.setText("Day " + location.getDay());
    }

    @Override
    public int getItemCount() {
        return locationList != null ? locationList.size() : 0;
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvDay;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_location_name);
            tvDay = itemView.findViewById(R.id.tv_day_number);
        }
    }
}