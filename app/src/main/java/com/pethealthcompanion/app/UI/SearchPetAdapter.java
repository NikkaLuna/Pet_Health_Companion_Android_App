package com.pethealthcompanion.app.UI;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.entities.Pet;

import java.util.ArrayList;
import java.util.List;

public class SearchPetAdapter extends RecyclerView.Adapter<SearchPetAdapter.SearchPetViewHolder> {

    private List<Pet> mPets;
    private final Context context;
    private final LayoutInflater mInflater;

    class SearchPetViewHolder extends RecyclerView.ViewHolder {

        private TextView petNameTextView2 = null;

        public SearchPetViewHolder(@NonNull View itemView) {
            super(itemView);
            petNameTextView2 = itemView.findViewById(R.id.petNameTextView2);
        }

    }

    public SearchPetAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public SearchPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.petsearch_list_item, parent, false);
        return new SearchPetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPetViewHolder holder, int position) {
        if (mPets != null && position < mPets.size()) {
            Log.d("Adapter", "mPets size: " + (mPets != null ? mPets.size() : 0));

            Pet current = mPets.get(position);
            Log.d("Adapter", "Position: " + position);

            String name = current.getPetName() + " - " + current.getSpecies() + " - " + current.getBreed();
            Log.d("Adapter", "Current pet: " + current);

            holder.petNameTextView2.setText(name);
            Log.d("Adapter", "mPets size: " + (mPets != null ? mPets.size() : 0));
        } else {
            holder.petNameTextView2.setText("No pet name");
        }
    }

    public void setPets(List<Pet> pets) {
        if (mPets == null) {
            mPets = new ArrayList<>();
        }
        mPets.addAll(pets);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPets!=null) {
            return mPets.size();
        }
        else return 0;
    }
}
