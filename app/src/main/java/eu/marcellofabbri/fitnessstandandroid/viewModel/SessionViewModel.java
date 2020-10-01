package eu.marcellofabbri.fitnessstandandroid.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.model.session.SessionRepository;

public class SessionViewModel extends AndroidViewModel {

  SessionRepository sessionRepository;
  LiveData<List<Session>> allSessions;

  public SessionViewModel(@NonNull Application application) {
    super(application);
    sessionRepository = new SessionRepository(application);
    allSessions = sessionRepository.getAllSessions();
  }

  public void insert(Session session) {
    sessionRepository.insert(session);
  }

  public void update(Session session) {
    sessionRepository.update(session);
  }

  public void delete(Session session) {
    sessionRepository.delete(session);
  }

  public LiveData<List<Session>> getAllSessions() {
    return sessionRepository.getAllSessions();
  }

  public void deleteSessionByWorkoutId(Long workoutId) {
    sessionRepository.deleteSessionByWorkoutId(workoutId);
  }

  public LiveData<List<Session>> getByWorkoutName(String workoutName) {
    return sessionRepository.getByWorkoutName(workoutName);
  }
}
