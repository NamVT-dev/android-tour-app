package vn.edu.fpt.prm.features.booking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.utils.Formatter;
import vn.edu.fpt.prm.features.booking.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public void setData(List<Booking> bookings) {
        this.bookingList = bookings;
        notifyDataSetChanged();
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
        holder.tvTourName.setText(booking.getTourName());
        holder.tvPrice.setText(Formatter.formatCurrency(booking.getPrice().intValue()));
        holder.tvPaid.setText(booking.isPaid() ? "Paid" : "Unpaid");
        holder.tvCreatedAt.setText(booking.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvPrice, tvPaid, tvCreatedAt;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPaid = itemView.findViewById(R.id.tvPaid);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
