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

    public void deleteSessionByWorkoutName(String workoutName) {
      new DeleteSessionByWorkoutNameAsyncTask(sessionDao).execute(workoutName);
    }

    public LiveData<List<Session>> getByWorkoutName(String workoutName) {
      return sessionDao.getByWorkoutName(workoutName);
    }

    public void deleteById(long id) {
      new DeleteSessionByIdAsyncTask(sessionDao).execute(id);
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

    private static class DeleteSessionByWorkoutNameAsyncTask extends AsyncTask<String, Void, Void> {
      private SessionDao sessionDao;

      DeleteSessionByWorkoutNameAsyncTask(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
      }
      @Override
      protected Void doInBackground(String... strings) {
        sessionDao.deleteSessionsByWorkoutName(strings[0]);
        return null;
      }
    }

  private static class DeleteSessionByIdAsyncTask extends AsyncTask<Long, Void, Void> {
    private SessionDao sessionDao;

    DeleteSessionByIdAsyncTask(SessionDao sessionDao) {
      this.sessionDao = sessionDao;
    }
    @Override
    protected Void doInBackground(Long... longs) {
      sessionDao.deleteById(longs[0]);
      return null;
    }
  }

}
