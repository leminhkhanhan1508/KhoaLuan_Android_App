package com.example.attendence.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.attendence.Model.Information_Attend;
import com.example.attendence.R;

import java.util.List;

public class BitMap_Adapter extends ArrayAdapter<Information_Attend> {
    Activity context;
    int resource;
    List<Information_Attend> objects;

    public BitMap_Adapter(@NonNull Context context, int resource, @NonNull List<Information_Attend> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.objects = objects;

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
        if (this.objects.get(position).isCheck_Attend()) {
            checkBox.setChecked(true);
        }
        //did image report???
        if (this.objects.get(position).isReport()) {
            report.setVisibility(View.VISIBLE);
        }
        imgImage.setImageBitmap(this.objects.get(position).getBitmap());
        return row;
    }
}
