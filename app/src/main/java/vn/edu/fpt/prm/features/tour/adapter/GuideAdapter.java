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

import java.util.List;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.features.tour.Guide;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {

    private final Context context;
    private List<Guide> guideList;

    public GuideAdapter(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
    }

    public void setGuideList(List<Guide> guideList) {
        this.guideList = guideList;
        for (Guide guide : guideList) {
            Logger.debug("GuideAdapter", "Guide Name: " + guide.getName() + ", Role: " + guide.getRole() + ", Photo: " + guide.getPhoto());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        Guide guide = guideList.get(position);
        holder.tvGuideName.setText(guide.getName());
        holder.tvGuideRole.setText(formatRole(guide.getRole()));

        // Load avatar image using Glide
        Glide.with(context)
                .load(guide.getPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgGuide);
    }

    @Override
    public int getItemCount() {
        return guideList != null ? guideList.size() : 0;
    }

    public static class GuideViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGuide;
        TextView tvGuideName, tvGuideRole;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGuide = itemView.findViewById(R.id.img_guide);
            tvGuideName = itemView.findViewById(R.id.tv_guide_name);
            tvGuideRole = itemView.findViewById(R.id.tv_guide_role);
        }
    }

    private String formatRole(String role) {
        if (role == null) return "";
        switch (role) {
            case "lead-guide":
                return "Lead Guide";
            case "guide":
                return "Guide";
            case "intern":
                return "Intern";
            default:
                return role;
        }
    }
}