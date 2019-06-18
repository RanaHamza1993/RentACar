package com.team.rentacar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.team.rentacar.R;
import com.team.rentacar.activities.MainActivity;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.MainPageModel;

import java.util.ArrayList;

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.MainHolder> {
    private Context context;
    private ArrayList<MainPageModel> arrayList;
    private Communicator.homeNavigator listener;

    public void setCommunicatorNavigator(Communicator.homeNavigator listener){
        this.listener=listener;
    }
    public MainPageAdapter(Context context, ArrayList<MainPageModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {

        holder.cat_image.setImageResource(arrayList.get(position).getCatImage());
        holder.cat_text.setText(arrayList.get(position).getCatName());
        holder.itemView.setOnClickListener(v->{
            if(listener!=null)
            listener.navigateToOtherActivities(arrayList.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {

        private final ImageView cat_image;
        private final TextView cat_text;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_text = itemView.findViewById(R.id.cat_text);
        }
    }
}
