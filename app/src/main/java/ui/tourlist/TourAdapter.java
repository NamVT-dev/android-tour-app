package ui.tourlist;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_booking_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.model.Tour;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private final List<Tour> tourList;

    public TourAdapter(List<Tour> tourList) {
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);

        holder.txtName.setText(tour.getName());
        holder.txtPrice.setText("Giá: $" + tour.getPrice());
        holder.txtDescription.setText(tour.getDescription());

        if (tour.getImage() != null && !tour.getImage().isEmpty()) {
            // Nếu bạn đã thêm PicassoTransformations
            Picasso.get()
                    .load(tour.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .transform(new RoundedCornersTransformation(20, 0))
                    .into(holder.imgTour);
        } else {
            holder.imgTour.setImageResource(R.drawable.ic_launcher_foreground);
        }

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTour;
        TextView txtName, txtPrice, txtDescription;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTour = itemView.findViewById(R.id.imgTour);
            txtName = itemView.findViewById(R.id.txtTourName);
            txtPrice = itemView.findViewById(R.id.txtTourPrice);
            txtDescription = itemView.findViewById(R.id.txtTourDescription);
        }
    }
}