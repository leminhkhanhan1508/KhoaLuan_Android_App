package com.example.attendence.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.attendence.Model.Information_Attend;
import com.example.attendence.R;

import java.util.ArrayList;
import java.util.List;

public class BitMap_Adapter extends ArrayAdapter<Information_Attend>  {
    Activity context;
    int resource;
    List<Information_Attend> itemsModel;
   List<Information_Attend> itemsModelListFiltered;

    public BitMap_Adapter(@NonNull Context context, int resource, @NonNull List<Information_Attend> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.itemsModel = objects;
        this.itemsModelListFiltered=objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        CheckBox checkBox = row.findViewById(R.id.check_attend);
        de.hdodenhof.circleimageview.CircleImageView report = row.findViewById(R.id.reportview);
        ImageView imgImage = row.findViewById(R.id.image);
        //who was attend
        if (itemsModelListFiltered.get(position).isCheck_Attend()) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }
        //did image report???
        if (itemsModelListFiltered.get(position).isReport()) {
            report.setVisibility(View.VISIBLE);
        }
        else {
            report.setVisibility(View.INVISIBLE);
        }
        imgImage.setImageBitmap(itemsModelListFiltered.get(position).getBitmap());
        return row;
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();
    }
    @Override
    public Information_Attend getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = itemsModel.size();
                    filterResults.values = itemsModel;

                }else{
                    List<Information_Attend> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Information_Attend itemAttendance: itemsModel){
                        if(itemAttendance.getStudentCode().contains(searchStr) ){
                            resultsModel.add(itemAttendance);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }



                }

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                itemsModelListFiltered = (ArrayList<Information_Attend>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
