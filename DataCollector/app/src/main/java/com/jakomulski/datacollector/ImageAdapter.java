package com.jakomulski.datacollector;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jakomulski.datacollector.models.Photo;

import java.util.List;

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private List<Photo> photos;
    public ImageAdapter(Context c, List<Photo> photos)
    {
        context = c;
        this.photos = photos;
    }


    public int getCount() {
        return photos.size();
    }

    public Photo getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(BitmapFactory.decodeFile(photos.get(position).getUri()));
        return imageView;
    }
}