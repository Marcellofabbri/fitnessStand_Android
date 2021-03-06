package eu.marcellofabbri.fitnessstandandroid.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.view.helpers.GridViewSetup;

public class GridViewAdapter extends BaseAdapter {
  private Context context;
  private List<String> monthDays;
  private LayoutInflater inflater;
  private List<Session> sessionsList;
  private Calendar calendarTool;

  public GridViewAdapter(Context context, List<String> monthDays, List<Session> sessionsList) {
    this.context = context;
    this.monthDays = monthDays;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.sessionsList = sessionsList;
    this.calendarTool = Calendar.getInstance();
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
    if (position < 7) {
      tv.setTextColor(context.getColor(R.color.greenTitles));
      tv.setTypeface(null, Typeface.BOLD);
    }
    if (daysWithSessions().contains(monthDays.get(position))) {
      img.setImageResource(R.drawable.full_squircle_blue);
      tv.setTextColor(Color.WHITE);
    }
    tv.setText(monthDays.get(position));

    return cell;
  }

  public List<String> daysWithSessions() {
    List<String> array = new ArrayList<String>();
    for (Session session : sessionsList) {
      Date sessionDate = session.getDate();
      calendarTool.setTime(sessionDate);
      int sessionDay = calendarTool.get(Calendar.DAY_OF_MONTH);
      array.add(String.valueOf(sessionDay));
    }
    return array;
  }
}
