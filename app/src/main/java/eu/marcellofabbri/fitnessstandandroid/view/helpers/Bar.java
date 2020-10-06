package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;

public class Bar extends View {

  private int height;
  private int unit = height / 150;
  private int width;
  private List<MyPoint> points;
  private Paint paint;
  private double avgDuration;
  private double target;

  public Bar(int height, int width, Context context, double avgDuration, double target) {
    super(context);
    this.height = height;
    this.width = width;
    this.points = new ArrayList<MyPoint>();
    this.avgDuration = avgDuration;
    this.target = target;
    this.paint = createPaintObject(getColorDarkerGrey());
    createPoints();
  }

  public Bar(int height, int width, Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.height = height;
    this.width = width;
  }

  public Bar(int height, int width, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.height = height;
    this.width = width;
  }

  public Bar(int height, int width, Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.height = height;
    this.width = width;
  }

  private Paint createPaintObject(int color) {
    Paint paint = new Paint();
    paint.setStrokeWidth(width/6);
    paint.setStrokeCap(Paint.Cap.SQUARE);
    paint.setColor(color);
    paint.setFakeBoldText(true);
    paint.setTextSize(35);
    return paint;
  }

  private int getColorDarkerGrey() {
    return getResources().getColor(android.R.color.darker_gray);
  }

  @Override
  public void onDraw(Canvas canvas) {
    drawBaseBar(canvas);
    drawColoredBar(canvas);
    printTarget(canvas);
  }

  private void drawBaseBar(Canvas canvas) {
    for (MyPoint point : points) {
      canvas.drawLine(point.x, point.y, point.x + unit, point.y + unit, paint);
    }
  }

  private int convertAvgDurationToPointId() {
    double minutesPerPoint = 55 / target;
    int pointId = (int) (avgDuration * minutesPerPoint);
    return pointId;
  }

  private void drawColoredBar(Canvas canvas) {
    int durationId = convertAvgDurationToPointId();
    Paint greenPaint = createPaintObject(getResources().getColor(R.color.greenGauge));
    Paint redPaint = createPaintObject(getResources().getColor(R.color.redGauge));
    Paint amberPaint = createPaintObject(getResources().getColor(R.color.amberGauge));
    Paint requiredPaint = durationId < 28 ? redPaint : durationId >= 28 && durationId < 55 ? amberPaint : greenPaint;
    for (MyPoint point : points) {
      if (point.id <= durationId) {
        canvas.drawPoint(point.x, point.y, requiredPaint);
      }
    }
    if (durationId >= 110) {
      MyPoint topPoint = points.get(109);
      canvas.drawText(" over", topPoint.x, topPoint.y + 50*unit, greenPaint);
    }
  }

  private void printTarget(Canvas canvas) {
    String targetText = String.valueOf("----" + (int) target);
    MyPoint midPoint = points.get(55);
    Paint blackPaint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setTextSize(35);
    paint.setFakeBoldText(true);
    canvas.drawText(targetText, midPoint.x - width/12, midPoint.y, paint);
  }

  private void createPoints() {
    // POINTS IN A VERTICAL LINE
    // height of container divided in 150 units
    // first 20 units empty
    // 110 units filled with the drawn line
    // last 20 units empty
    int unit = height / 120;
    int halfWidth = width / 2;
    for (int i = 1; i <= 110; i++) {
      MyPoint point = new MyPoint(halfWidth, 110 * unit - (unit * i), i);
      points.add(point);
    }

  }

  private class MyPoint extends Point {
    int x;
    int y;
    int id;

    public MyPoint(int x, int y, int id) {
      this.x = x;
      this.y = y;
      this.id = id;
    }
  }

}
