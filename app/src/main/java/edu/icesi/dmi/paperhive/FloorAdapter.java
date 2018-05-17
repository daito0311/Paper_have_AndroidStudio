package edu.icesi.dmi.paperhive;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by estudiante on 17/05/18.
 */

public class FloorAdapter extends BaseAdapter{
    ImageView floor_image;
    TextView title_tv, available_seats_tv, description_tv;

    ArrayList<Floor> floors;

    Activity activity;

    DatabaseReference reference;

    public FloorAdapter(Activity activity, DatabaseReference reference) {
        floors = new ArrayList<>();

        this.activity = activity;
        this.reference = reference;
    }

    public void addFloor(Floor floor){
        floors.add(floor);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return floors.size();
    }

    @Override
    public Object getItem(int i) {
        return floors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.floor, viewGroup, false);

        floor_image = view.findViewById(R.id.floor_image);
        title_tv = view.findViewById(R.id.floor_title_tv);
        available_seats_tv = view.findViewById(R.id.floor_available_seats_tv);
        description_tv = view.findViewById(R.id.floor_description_tv);

        return view;
    }
}
