package com.example.mvvmmovieapp.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mvvmmovieapp.R;
import com.example.mvvmmovieapp.data.vo.Favourite;

import java.util.ArrayList;

public class WishListAdapter extends ArrayAdapter {
    private Context context;
    private int layout;
    private ArrayList<Favourite> favourites;

    public WishListAdapter(Context context, int layout, ArrayList<Favourite> favourites) {
        super(context, layout, favourites);
        this.context = context;
        this.layout = layout;
        this.favourites = favourites;
    }

    @Override
    public int getCount() {
        if (favourites == null) {
            return 0;
        } else {
            return favourites.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Favourite favourite = favourites.get(position);
        holder.tvTitle.setText(favourite.getMovieName());


        return view;
    }

    private class ViewHolder {
        TextView tvTitle;
    }
}
