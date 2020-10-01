package eu.marcellofabbri.fitnessstandandroid.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.model.session.SessionDao;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.model.workout.WorkoutDao;


@androidx.room.Database(entities = {Workout.class, Session.class}, version = 2)
public abstract class Database extends RoomDatabase {

  private static Database instance;

  public abstract WorkoutDao workoutDao();
  public abstract SessionDao sessionDao();

  //synchronized: there can only be one thread accessing the database
  public static synchronized Database getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "fitnessStand_database")
              .addCallback(roomCallback) //to call the onCreate method
              .fallbackToDestructiveMigration()
              .build();
    }
    return instance;
  }

  private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      //new PopulateDbAsyncTask(instance).execute();
    }
  };

  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
    private WorkoutDao workoutDao;
    private SessionDao sessionDao;

    private PopulateDbAsyncTask(Database database) {
      workoutDao = database.workoutDao();
      sessionDao = database.sessionDao();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void...voids) {
//      workoutDao.insert(new Workout("workoutName"));
//      sessionDao.insert(new Session(45, new Date(), "workoutName"));
      return null;
    }
  }
}