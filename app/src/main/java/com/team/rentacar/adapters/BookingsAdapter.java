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
import com.team.rentacar.activities.BookingActivity;
import com.team.rentacar.activities.NavigationActivity;
import com.team.rentacar.models.VendorsDetailModel;
import com.team.rentacar.models.VendorsModel;
import com.team.rentacar.utilities.StartNewActivity;

import java.util.ArrayList;
import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsHolder> {
    private Context context;
    int flag=1;
    private List<VendorsDetailModel> bookingList;
    public BookingsAdapter(Context context, ArrayList<VendorsDetailModel> bookingList,int flag) {
        this.context=context;
        this.bookingList=bookingList;
        this.flag=flag;
    }

    @NonNull
    @Override
    public BookingsAdapter.BookingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vedors_detail_item, parent, false);
        return new BookingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.BookingsHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingsHolder extends RecyclerView.ViewHolder {
        TextView vendorName;
        ImageView carImage;
        TextView carName;
        TextView vendorAddress;
        TextView hourlyPrice;
        TextView rentIt;
        TextView sendCar;
        public BookingsHolder(@NonNull View itemView) {
            super(itemView);
            vendorName = itemView.findViewById(R.id.vendor_name);
            carImage = itemView.findViewById(R.id.car_image);
            carName = itemView.findViewById(R.id.car_name);
            vendorAddress = itemView.findViewById(R.id.vendor_address);
            hourlyPrice = itemView.findViewById(R.id.hourly_rate);
            rentIt = itemView.findViewById(R.id.rent_car);
            sendCar = itemView.findViewById(R.id.send_car);
        }
        void setData(int position){
            String name=bookingList.get(position).getImage();
            // holder.carImage.setImageResource(bookingList.get(position).getImage());
            if (name!=null&&name.equals("default_profile"))
                Glide.with(context).load(R.drawable.cplaceholder).placeholder(R.drawable.cplaceholder).into(carImage);
            else {
                Glide.with(context).load(bookingList.get(position).getImage()).
                        placeholder(R.drawable.cplaceholder).into(carImage);
            }
            carName.setText(bookingList.get(position).getCarName());
            vendorName.setText(bookingList.get(position).getVendorName());
            vendorAddress.setText(bookingList.get(position).getVendorAddress());
            if(flag==1) {
                hourlyPrice.setText(bookingList.get(position).getHourlyPrice());
                sendCar.setVisibility(View.GONE);

            }
            else {
                hourlyPrice.setText("Booked by: " + bookingList.get(position).getBookedBy());
                rentIt.setText("Cancel");
                rentIt.setOnClickListener(v->{
                    
                });
                sendCar.setVisibility(View.VISIBLE);
                sendCar.setOnClickListener(v->{
                    new StartNewActivity<NavigationActivity>(context,NavigationActivity.class);
                });
            }

//            rentIt.setVisibility(View.GONE);

        }
    }
}
