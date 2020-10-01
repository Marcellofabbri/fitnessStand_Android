package eu.marcellofabbri.fitnessstandandroid.model.session;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.model.Database;

public class SessionRepository {

  private SessionDao sessionDao;
  private LiveData<List<Session>> allSessions;

  public SessionRepository(Application application) {
    Database database = Database.getInstance(application);
    sessionDao = database.sessionDao();
    allSessions = sessionDao.getAllSessions();
  }

    //database operations
    public void insert(Session session) {
      new InsertSessionAsyncTask(sessionDao).execute(session);
    }

    public void update(Session session) {
      new UpdateSessionAsyncTask(sessionDao).execute(session);
    }

    public void delete(Session session) {
      new DeleteSessionAsyncTask(sessionDao).execute(session);
    }

    public LiveData<List<Session>> getAllSessions() {
      return allSessions;
    }

    public void deleteSessionByWorkoutId(Long workoutId) {
      new DeleteSessionByWorkoutIdAsyncTask(sessionDao).execute(workoutId);
    }

    public LiveData<List<Session>> getByWorkoutName(String workoutName) {
      return sessionDao.getByWorkoutName(workoutName);
    }

    //asyncTask classes (each per method)

    private static class InsertSessionAsyncTask extends AsyncTask<Session, Void, Void> {
      private SessionDao sessionDao;

      InsertSessionAsyncTask(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
      }
      @Override
      protected Void doInBackground(Session... sessions) {
        sessionDao.insert(sessions[0]);
        return null;
      }
    }

    private static class UpdateSessionAsyncTask extends AsyncTask<Session, Void, Void> {
      private SessionDao sessionDao;

      UpdateSessionAsyncTask(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
      }
      @Override
      protected Void doInBackground(Session... sessions) {
        sessionDao.update(sessions[0]);
        return null;
      }
    }

    private static class DeleteSessionAsyncTask extends AsyncTask<Session, Void, Void> {
      private SessionDao sessionDao;

      DeleteSessionAsyncTask(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
      }
      @Override
      protected Void doInBackground(Session... sessions) {
        sessionDao.delete(sessions[0]);
        return null;
      }
    }

    private static class DeleteSessionByWorkoutIdAsyncTask extends AsyncTask<Long, Void, Void> {
      private SessionDao sessionDao;

      DeleteSessionByWorkoutIdAsyncTask(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
      }
      @Override
      protected Void doInBackground(Long... longs) {
        sessionDao.deleteSessionsByWorkoutId(longs[0]);
        return null;
      }
    }

}
