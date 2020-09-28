package eu.marcellofabbri.fitnessstandandroid.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import eu.marcellofabbri.fitnessstandandroid.R;

public class BackgroundGridViewAdapter extends BaseAdapter {
  private ArrayList<String> items;
  private LayoutInflater inflater;

  public BackgroundGridViewAdapter(Context context, ArrayList<String> items) {
    this.items = items;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public String getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.background_gridview_item_full, null);
      }
    return convertView;
  }
}
