package com.pethealthcompanion.app.UI;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.entities.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pet> mPets;
    private final Context context;
    private final LayoutInflater mInflater;

    class PetViewHolder extends RecyclerView.ViewHolder {
        private TextView petNameTextView = null;
        private TextView petBreedTextView = null;
        private PetViewHolder(View itemView) {
            super(itemView);
            petNameTextView = itemView.findViewById(R.id.petNameTextView);
            petBreedTextView = itemView.findViewById(R.id.petNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Pet current = mPets.get(position);
                    Intent intent = new Intent(context, PetProfile.class);
                    intent.putExtra("petID", current.getPetID());
                    intent.putExtra("petName", current.getPetName());
                    intent.putExtra("species", current.getSpecies());
                    intent.putExtra("breed", current.getBreed());
                    intent.putExtra("birthday", current.getBirthday());
                    context.startActivity(intent);
                }
            });
        }
    }

    public PetAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.pet_list_item, parent, false);
        return new PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        if (mPets != null && position < mPets.size()) {
            Pet current = mPets.get(position);
            String name = current.getPetName() + " - " + current.getBreed();
            holder.petNameTextView.setText(name);
        } else {
            holder.petNameTextView.setText("No pet name");
        }
    }

    public void setPets(List<Pet> pets) {
        mPets = pets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPets != null) return mPets.size();
        else return 0;
    }
}
