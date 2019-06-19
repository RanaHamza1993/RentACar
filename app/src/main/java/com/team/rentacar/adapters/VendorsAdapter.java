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
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsDetailModel;
import com.team.rentacar.models.VendorsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.VendorsHolder> {

    private Context context;
    private List<VendorsModel> arrayList;
    private List<VendorsDetailModel> vendorsDetailArrayList;
    private Communicator.homeNavigator listener;
    public void setCommunicatorNavigator(Communicator.homeNavigator listener) {
        this.listener = listener;
    }

    private int modelFlag = 1;

    public VendorsAdapter(Context context, ArrayList<VendorsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public VendorsAdapter(Context context, ArrayList<VendorsDetailModel> vendorsDetailArrayList, int modelFlag) {
        this.context = context;
        this.vendorsDetailArrayList = vendorsDetailArrayList;
        this.modelFlag = modelFlag;
    }

    @NonNull
    @Override
    public VendorsAdapter.VendorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (modelFlag == 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_item_layout, parent, false);
        else if (modelFlag == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vedors_detail_item, parent, false);

        }
        return new VendorsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsAdapter.VendorsHolder holder, int position) {
        holder.setData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (modelFlag == 1)
            return arrayList.size();
        else if (modelFlag == 2) {
           return vendorsDetailArrayList.size();
//            int count=0;
//            for (int i = 0; i <vendorsDetailArrayList.size() ; i++) {
//                if(vendorsDetailArrayList.get(i).getId()==vendorID){
//
//                    vendorsDetailArrayList=vendorsDetailArrayList.subList(i,i+1);
//                    count++;
//                }
//
//            }
//           return count;
        }
        else
            return arrayList.size();
    }

    public class VendorsHolder extends RecyclerView.ViewHolder {
        ImageView vendorImage;
        TextView vendorName;
        ImageView carImage;
        TextView carName;
        TextView vendorAddress;
        TextView hourlyPrice;
        TextView rentIt;

        public VendorsHolder(@NonNull View itemView) {
            super(itemView);
            vendorImage = itemView.findViewById(R.id.vendor_image);
            vendorName = itemView.findViewById(R.id.vendor_name);
            carImage = itemView.findViewById(R.id.car_image);
            carName = itemView.findViewById(R.id.car_name);
            vendorAddress = itemView.findViewById(R.id.vendor_address);
            hourlyPrice = itemView.findViewById(R.id.hourly_rate);
            rentIt = itemView.findViewById(R.id.rent_car);


        }

        public void setData(VendorsAdapter.VendorsHolder holder, int position) {
            if (modelFlag == 1) {

                holder.vendorImage.setImageResource(arrayList.get(position).getImage());
                holder.vendorName.setText(arrayList.get(position).getName());
                holder.itemView.setOnClickListener(v -> {
                    listener.navigateToOtherActivities(arrayList.get(position).getId());
                });
            } else if (modelFlag == 2) {

                    holder.carImage.setImageResource(vendorsDetailArrayList.get(position).getImage());
                    holder.carName.setText(vendorsDetailArrayList.get(position).getCarName());
                    holder.vendorName.setText(vendorsDetailArrayList.get(position).getVendorName());
                    holder.vendorAddress.setText(vendorsDetailArrayList.get(position).getVendorAddress());
                    holder.hourlyPrice.setText(vendorsDetailArrayList.get(position).getHourlyPrice());

            }
        }
    }
}
