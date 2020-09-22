package eu.marcellofabbri.fitnessstandandroid.utils;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.util.Date;

public class EntityFieldConverter {
  @TypeConverter
  public String convertDateToString(Date date) {
    return date.toString();
  }

  @TypeConverter
  public Date convertStringtoDate(String string) {
    long instant = Date.parse(string);
    return new Date(instant);
  }
}
