package vn.edu.fpt.prm.features.tour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.utils.Formatter;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.features.tour.Tour;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private final Context context;
    private List<Tour> tourList;

    // Listener for click events on tours
    public interface OnTourClickListener {
        void onTourClick(Tour tour);
    }

    private final OnTourClickListener listener;

    public TourAdapter(Context context, List<Tour> tourList, OnTourClickListener listener) {
        this.context = context;
        this.tourList = tourList;
        this.listener = listener;
    }

    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
        notifyDataSetChanged();
    }

    // Create a new ViewHolder for the tour item
    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour_card, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        holder.bind(tour);
    }

    @Override
    public int getItemCount() {
        return tourList != null ? tourList.size() : 0;
    }

    class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvAddress;
        TextView tvTourName;
        TextView tvSummary;
        TextView tvDuration;
        TextView tvRating;
        TextView tvPrice;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTourName = itemView.findViewById(R.id.tv_tour_name);
            tvSummary = itemView.findViewById(R.id.tv_summary);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        // Bind the tour data to the views
        public void bind(Tour tour) {
            Logger.debug("TourAdapter", "Image URL: " + tour.getImageCover());

            String originalUrl = tour.getImageCover();

            Logger.debug("TourAdapter", "Original Image URL: " + originalUrl);

            String resizedUrl = originalUrl.replace("/upload/", "/upload/w_800,h_600,c_limit/");

            RequestOptions options = new RequestOptions()
                    .override(800, 600)
                    .centerCrop()
                    .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder);

            Glide.with(context)
                    .load(resizedUrl)
                    .apply(options)
                    .into(imgCover);

            // Set the tour details to the views
            tvTourName.setText(tour.getName());
            tvSummary.setText(tour.getSummary());
            tvDuration.setText(String.valueOf(tour.getDuration()));
            tvRating.setText(String.valueOf(tour.getRatingsAverage()));
            tvPrice.setText(Formatter.formatCurrency(tour.getPrice().intValue()));


            // Set the address, handling null values
            if (tour.getStartLocation() != null) {
                tvAddress.setText(tour.getStartLocation().getAddress());
            } else {
                tvAddress.setText("");
            }

            // Set click listener for the tour item
            itemView.setOnClickListener(v -> listener.onTourClick(tour));
        }
    }
}
