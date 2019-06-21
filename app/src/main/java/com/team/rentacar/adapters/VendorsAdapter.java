package com.team.rentacar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.team.rentacar.R;
import com.team.rentacar.activities.VendorsActivity;
import com.team.rentacar.activities.VendorsDetailActivity;
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
    private Communicator.IRent rentListener;
    private int[] drawables;
    public VendorsAdapter(Context context, ArrayList<VendorsModel> arrayList, int[] drawables) {
        this.context=context;
        this.arrayList=arrayList;
        this.drawables=drawables;
    }

    public void setCommunicatorNavigator(Communicator.homeNavigator listener) {
        this.listener = listener;
    }

    public void setRentCommunicator(Communicator.IRent rentListener) {
        this.rentListener=rentListener;
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
        holder.setData( position);
    }

    @Override
    public int getItemCount() {
        if (modelFlag == 1)
            return arrayList.size();
        else if (modelFlag == 2) {
           return vendorsDetailArrayList.size();
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

        public void setData( int position) {
            if (modelFlag == 1) {

                vendorImage.setImageResource(drawables[position]);
                vendorName.setText(arrayList.get(position).getName());
                itemView.setOnClickListener(v -> {
                    listener.navigateToOtherActivities(arrayList.get(position).getId(),arrayList.get(position).getName());
                });
            } else if (modelFlag == 2) {
                String name=vendorsDetailArrayList.get(position).getImage();
                   // holder.carImage.setImageResource(vendorsDetailArrayList.get(position).getImage());
                if (name!=null&&name.equals("default_profile"))
                    Glide.with(context).load(R.drawable.cplaceholder).placeholder(R.drawable.cplaceholder).into(carImage);
                else {
                    Glide.with(context).load(vendorsDetailArrayList.get(position).getImage()).
                            placeholder(R.drawable.cplaceholder).into(carImage);
                }
                    carName.setText(vendorsDetailArrayList.get(position).getCarName());
                    vendorName.setText(vendorsDetailArrayList.get(position).getVendorName());
                    vendorAddress.setText(vendorsDetailArrayList.get(position).getVendorAddress());
                    hourlyPrice.setText(vendorsDetailArrayList.get(position).getHourlyPrice());
                    rentIt.setOnClickListener(v->{
                        rentListener.rentIt(vendorsDetailArrayList.get(position).getId());
                    });

            }
        }
    }
}
