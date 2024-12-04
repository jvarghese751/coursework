package com.example.sportsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private List<Club> clubs;

    public ClubAdapter(List<Club> clubs) {
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_item, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = clubs.get(position);
        holder.nameTextView.setText(club.name);
        holder.leagueTextView.setText(club.strLeague);

        if (club.strTeamLogo != null && !club.strTeamLogo.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(club.strTeamLogo)
                    .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                    .error(R.drawable.error_image) // Error fallback
                    .into(holder.logoImageView);
        } else {
            holder.logoImageView.setImageResource(R.drawable.placeholder_image); // Default placeholder
        }
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, leagueTextView;
        ImageView logoImageView;

        public ClubViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            leagueTextView = itemView.findViewById(R.id.leagueTextView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
        }
    }
}