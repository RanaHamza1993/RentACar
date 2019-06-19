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
import com.team.rentacar.models.VendorsModel;

import java.util.ArrayList;

public class VendorsAdapter  extends RecyclerView.Adapter<VendorsAdapter.VendorsHolder> {

    private Context context;
    private ArrayList<VendorsModel> arrayList;
   public VendorsAdapter(Context context , ArrayList<VendorsModel> arrayList){
       this.context=context;
       this.arrayList=arrayList;
   }
    @NonNull
    @Override
    public VendorsAdapter.VendorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_item_layout, parent, false);
        return new VendorsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsAdapter.VendorsHolder holder, int position) {
       holder.setData(holder,position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorsHolder extends RecyclerView.ViewHolder {
       ImageView vendorImage;
       TextView vendorName;
        public VendorsHolder(@NonNull View itemView) {
            super(itemView);
            vendorImage=itemView.findViewById(R.id.image);
            vendorName=itemView.findViewById(R.id.name);
        }

      public void setData(VendorsAdapter.VendorsHolder holder,int position){
            holder.vendorImage.setImageResource(arrayList.get(position).getImage());
            holder.vendorName.setText(arrayList.get(position).getName());
      }
    }
}
