package eu.marcellofabbri.fitnessstandandroid.view.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;

public class PeriodsAdapter extends RecyclerView.Adapter<PeriodsAdapter.PeriodHolder> {
  private List<String> periods = new ArrayList<>();
  private OnPeriodItemListener onPeriodItemListener;
  private int selectedPeriodIndex;

  public PeriodsAdapter(OnPeriodItemListener onPeriodItemListener, List<String> periods) {
    this.onPeriodItemListener = onPeriodItemListener;
    this.periods = periods;
  }

  public void setSelectedPeriodIndex(int selectedPeriodIndex) {
    this.selectedPeriodIndex = selectedPeriodIndex;
  }

  @NonNull
  @Override
  public PeriodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.period_item, parent, false);
    PeriodHolder periodHolder = new PeriodHolder(itemView, onPeriodItemListener);
    return periodHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull PeriodHolder holder, int position) {
    String currentPeriod = periods.get(position);
    holder.tv.setText(currentPeriod);
    setTextViewColor(holder.tv, position);
  }

  private void setTextViewColor(TextView textView, int position) {
    if (position == selectedPeriodIndex) {
      int color = textView.getResources().getColor(R.color.colorAccent);
      textView.setTextColor(Color.WHITE);
    } else {
      int color = textView.getResources().getColor(R.color.statsContainer);
      textView.setTextColor(Color.DKGRAY);
    }
  }

  @Override
  public int getItemCount() {
    return periods.size();
  }

  public class PeriodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tv;
    private View itemView;
    private OnPeriodItemListener onPeriodItemListener;

    public PeriodHolder(@NonNull View itemView, OnPeriodItemListener onPeriodItemListener) {
      super(itemView);
      this.itemView = itemView;
      this.tv = itemView.findViewById(R.id.period_item);
      this.onPeriodItemListener = onPeriodItemListener;

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
  }

  public interface OnPeriodItemListener {
    void onPeriodItemClick(int position);
  }
}
