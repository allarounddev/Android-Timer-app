package com.forixusa.scoretimer.android.graph;

import java.util.ArrayList;

import com.forixusa.scoretimer.android.ScoreTimerApplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class LineChartView extends View {
	private static final String TAG = LineChartView.class.getSimpleName();

	private static final float X_LEFT = 40;
	private static final float X_RIGHT = ScoreTimerApplication.getWidthScreen() - 50;
	private static final float Y_TOP = 30;
	private static final float Y_BOTTOM = 380;
	
	private float mTotalX, mTotalY;
	private ArrayList<Float> mScores;
	private Paint mPaint;
	
	public LineChartView(Context context) {
		super(context);
		Log.d(TAG, "LineChartView");
		
		mPaint = new Paint();
		mScores = new ArrayList<Float>();
		
		mTotalX = X_RIGHT - X_LEFT + 1;
		mTotalY = Y_BOTTOM - Y_TOP + 1;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw");		
		Log.d(TAG, "X_RIGHT:" + X_RIGHT);
		
//		getInputData();
		drawAxes(canvas);
		drawLineGraph(canvas);
	}
	
	//Get input from user
	public void setData(ArrayList<Float> data) {
		mScores.clear();
		mScores = data;
    }
	
  //Draw the line graph
   public void drawLineGraph(Canvas canvas) {
	   Log.d(TAG, "drawLineGraph");
	   mPaint.setStrokeWidth(2.5f);
	   mPaint.setColor(Color.LTGRAY);
	    
        float x1, y1, x2, y2, largestNumber, xIncrement, yIncrement;
        int i;
       //Compute the x and y increments
       largestNumber = findLargest (mScores);
       xIncrement = mTotalX / mScores.size();
       if (largestNumber ==0) {
    	   yIncrement = 0;
       } else {
           yIncrement = mTotalY / largestNumber;
       }
       
      //Set the initial origin point
       x1 = X_LEFT;
       y1 = Y_BOTTOM;

      //Compute and plot the data points
      for(i=0; i < mScores.size(); i++) {
          x2 = getXCoordinate(i+1, xIncrement);
          y2 = getYCoordinate(mScores.get(i), yIncrement);
    
          mPaint.setStrokeWidth(2.5f);
          mPaint.setColor(Color.LTGRAY);
          canvas.drawLine(x1, y1, x2, y2, mPaint);
          x1 = x2;
          y1 = y2;
          
          mPaint.setColor(Color.RED);
          mPaint.setStrokeWidth(5f);
          canvas.drawPoint(x2, y2, mPaint);
       }
      
//      //Label x - axes
//      for (i = 1; i <= mScores.size(); i++) {
//    	  canvas.drawText(String.valueOf(i), 40+ i*xIncrement, 400,mPaint);
//      }
//      
//      //Label y - axes with quantity of each grade
//      float topy;
//      if(largestNumber%10 ==0) {
//           topy = largestNumber;
//      } else {
//           topy = (largestNumber/10+1)*10;
//      }
//      
//      // i = i+5 controls y value label -- adjust for size of data
//      for (i = 0; i <= topy; i = i+20) {
//    	  canvas.drawText(String.valueOf(i), 10, Y_BOTTOM-i*yIncrement+5,mPaint);
//      }
   }

  //Draw the axes for the graph
   public void drawAxes (Canvas canvas) {
	   mPaint.setColor(Color.parseColor("#0A7BE4"));
	   mPaint.setStrokeWidth(3.5f);
	   canvas.drawLine(X_LEFT, Y_TOP, X_LEFT, Y_BOTTOM, mPaint);
	   canvas.drawLine(X_LEFT, Y_BOTTOM, X_RIGHT, Y_BOTTOM, mPaint);
   }
	   
	//Determining x coordinate
	public float getXCoordinate(float i, float xIncrement) {
       return X_LEFT + xIncrement *i;
	}

	//Determining y coordinate
	public float getYCoordinate(float numStudents, float yIncrement) {
       return Y_BOTTOM - yIncrement * numStudents;
	}

	//Finding the largest value in the array
	public float findLargest(ArrayList<Float> arr) {
		if(arr.isEmpty())
			return 0;
		
       int location = 0;
       for( int i = 1; i < arr.size(); i++) {
    	   if(arr.get(i) > arr.get(location)) {
               location = i;
    	   }
       }
       return arr.get(location);
	}

}
