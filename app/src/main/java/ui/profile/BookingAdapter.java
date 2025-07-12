package ui.profile;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_booking_system.R;

import java.util.List;

import data.model.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.txtTourName.setText(booking.getTourName());
        holder.txtPrice.setText(String.format("$%.2f", booking.getPrice()));
        holder.txtPaid.setText(booking.isPaid() ? "Yes" : "No");
        holder.txtCreatedAt.setText(booking.getCreatedAt());
        if (booking.isPaid()) {
            holder.txtPaid.setText("Paid: Yes");
            holder.txtPaid.setTextColor(Color.parseColor("#4CAF50")); // green
        } else {
            holder.txtPaid.setText("Paid: No");
            holder.txtPaid.setTextColor(Color.parseColor("#F44336")); // red
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtTourName, txtPrice, txtPaid, txtCreatedAt;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTourName = itemView.findViewById(R.id.textTourName);
            txtPrice = itemView.findViewById(R.id.textPrice);
            txtPaid = itemView.findViewById(R.id.textPaid);
            txtCreatedAt = itemView.findViewById(R.id.textCreatedAt);
        }
    }
}
