package com.example.b00063271.safesplit;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    ArrayList<String> result;
    Context context;
    ArrayList<Integer> imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(DashboardFragment mainActivity, ArrayList<String> prgmNameList, ArrayList<Integer> prgmImages) {
        result=prgmNameList;
        context=mainActivity.getContext();
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { return result.size(); }
    @Override
    public Object getItem(int position) { return position; }
    @Override
    public long getItemId(int position) { return position; }
    public class Holder {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.dashboard_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.dashboardItemTextView);
        holder.img=(ImageView) rowView.findViewById(R.id.dashboardItemImage);
        String activityStart = result.get(position).split("-")[0];
        String activityAmt = result.get(position).split("-")[1];
        String activityEnd = result.get(position).split("-")[2];
        String descriptionFinal = activityStart + activityAmt + activityEnd;
        Spannable spannable = new SpannableString(descriptionFinal);
        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), activityStart.length(), (activityStart + activityAmt).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.img.setImageResource(imageId.get(position));
        return rowView;
    }

}