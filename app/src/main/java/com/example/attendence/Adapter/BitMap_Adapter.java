package com.example.attendence.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.attendence.R;

import java.util.List;

public class BitMap_Adapter extends ArrayAdapter<Bitmap> {
    Activity context;
    int resource;
    List<Bitmap> objects;
    public BitMap_Adapter(@NonNull Context context, int resource, @NonNull List<Bitmap> objects) {
        super(context, resource, objects);
        this.context= (Activity) context;
        this.resource=resource;
        this.objects=objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);

        ImageView imgImage=row.findViewById(R.id.image);

        imgImage.setImageBitmap(this.objects.get(position));
        return row;
    }
}
