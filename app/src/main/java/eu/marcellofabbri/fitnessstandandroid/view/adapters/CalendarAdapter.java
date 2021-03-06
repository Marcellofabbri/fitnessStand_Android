package eu.marcellofabbri.fitnessstandandroid.view.adapters;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eu.marcellofabbri.fitnessstandandroid.model.session.Session;

public class CalendarAdapter {

  private ArrayList<String> weekDays = new ArrayList(Arrays.asList("S", "M", "T", "W", "T", "F", "S"));
  public int currentMonth;
  private int emptyCells;
  private int finalDayOfTheMonth;

  public CalendarAdapter(Calendar calendar) {
    this.currentMonth = calendar.get(Calendar.MONTH);
    this.emptyCells = emptyCellsBefore1st(calendar);
    this.finalDayOfTheMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public int emptyCellsBefore1st(Calendar calendar) {
    Calendar cal = calendar;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    cal.set(year, month, 1);
    return (cal.get(Calendar.DAY_OF_WEEK) - 1);
  }

  public List<String> itemsForTheGridView() {
    ArrayList<String> finalArray = weekDays;
    for (String s : emptyCellsArray()) {
      finalArray.add(s);
    }
    for (String s : entireMonthArray()) {
      finalArray.add(s);
    }
    addEmptyCellsAfterEndOfMonth(finalArray);
    return finalArray;
  }

  private List<String> addEmptyCellsAfterEndOfMonth(List<String> array) {
    int size = array.size();
    int finalRowItems = size % 7;
    int finalRowEmptyCells = 7 - finalRowItems;
    if (finalRowItems != 0) {
      for (int i = 0; i < finalRowEmptyCells; i++) {
        array.add("");
      }
    }
    return array;
  }

  private List<String> emptyCellsArray() {
    ArrayList<String> array = new ArrayList<String>();
    for (int i = 0; i < emptyCells; i++) {
      array.add(" ");
    }
    return array;
  }

  private List<String> entireMonthArray() {
    ArrayList<String> array = new ArrayList<String>();
    for (int i = 1; i <= finalDayOfTheMonth; i++) {
      String s = String.valueOf(i);
      array.add(s);
    }
    return array;
  }
}
