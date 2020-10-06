package eu.marcellofabbri.fitnessstandandroid.view.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.viewModel.SessionViewModel;

public class IndividualSessionOverviewFragment extends AppCompatDialogFragment {
  private TextView workoutName;
  private TextView date;
  private TextView duration;
  private ImageButton deleteButton;
  private SessionViewModel sessionViewModel;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.session_overview_layout, null);
    findViews(view);
    final Bundle args = getArguments();
    workoutName.setText(args.getString("selectedWorkout"));
    date.setText(args.getString("currentDate"));
    duration.setText(args.getString("duration") + " min.");

    long sessionId = args.getLong("id");
    deleteButton.setOnClickListener(onClickListenerForDeletion(sessionId));

    builder.setView(view).setNegativeButton("RETURN", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // DO NOTHING, JUST RETURN
      }
    });
    return builder.create();
  }

  private void findViews(View view) {
    workoutName = view.findViewById(R.id.overview_workoutName);
    date = view.findViewById(R.id.overview_date);
    duration = view.findViewById(R.id.overview_duration);
    deleteButton = view.findViewById(R.id.delete_session);
  }

  private View.OnClickListener onClickListenerForDeletion(final long id) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Delete this session?")
                .setNegativeButton("RETURN", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        }).setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            sessionViewModel = new SessionViewModel(getActivity().getApplication());
            sessionViewModel.deleteById(id);
            dismissFragment();
          }
        });
        builder.create().show();
      }
    };
  }

  public void dismissFragment() {
    dismiss();
  }

}
