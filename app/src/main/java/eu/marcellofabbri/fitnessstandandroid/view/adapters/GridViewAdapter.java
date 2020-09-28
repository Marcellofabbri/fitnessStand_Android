package eu.marcellofabbri.fitnessstandandroid.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.view.helpers.GridViewSetup;

public class GridViewAdapter extends BaseAdapter {
  private Context context;
  private List<String> monthDays;
  private LayoutInflater inflater;

  public GridViewAdapter(Context context, List<String> monthDays) {
    this.context = context;
    this.monthDays = monthDays;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return monthDays.size();
  }

  @Override
  public Object getItem(int position) {
    return getItemId(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View cell = convertView;

    if (cell == null) {
      cell = inflater.inflate(R.layout.gridview_item, null);
    }

    ImageView img = cell.findViewById(R.id.item_imageView);
    TextView tv = cell.findViewById(R.id.item_textView);
    img.setImageResource(R.drawable.full_squircle);
    tv.setText(monthDays.get(position));

    return cell;
  }
}
