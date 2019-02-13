package com.forixusa.scoretimer.android.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels. 
 * @author Arno den Hond
 *
 */
public class GraphView extends View {

	public static boolean BAR = true;
	public static boolean LINE = false;

	private Paint paint;
	private float[] values;
	private String[] horlabels;
	private String[] verlabels;
	private String title;
	private boolean type;

	public GraphView(Context context, float[] values, String title, String[] horlabels, String[] verlabels, boolean type) {
		super(context);
		
		if (values == null)
			values = new float[0];
		else
			this.values = values;
		
		if (title == null)
			title = "";
		else
			this.title = title;
		if (horlabels == null)
			this.horlabels = new String[0];
		else
			this.horlabels = horlabels;
		if (verlabels == null)
			this.verlabels = new String[0];
		else
			this.verlabels = verlabels;
		this.type = type;
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float border = 40;
		float horstart = border * 2;
		float height = getHeight();
		float width = getWidth() - 1;
		float max = getMax();
		float min = getMin();
		float diff = max - min;
		float graphheight = height - (2 * border);
		//float graphwidth = width - (2 * border);

		drawAxes(canvas,border,height,width);

		// Draw chart
		paint.setStrokeWidth(2.5f);
		paint.setColor(Color.LTGRAY);
		float datalength = values.length;
		float colwidth = (width - (2 * border)) / datalength;
		float halfcol = colwidth / 2;
		float lasth = 0;
		for (int i = 0; i < datalength; i++) {
			float val = values[i] - min;
			float rat = val / diff;
			float h = graphheight * rat;
			if (i > 0) {
				canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
			} else {
				canvas.drawLine(border+3, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
			}
			lasth = h;
		}
		
		
//		if (max != min) {
//			paint.setColor(Color.LTGRAY);
//			float datalength = values.length;
//			float colwidth = (width - (2 * border)) / datalength;
//			float halfcol = colwidth / 2;
//			float lasth = 0;
//			
//			for (int i = 0; i < values.length; i++) {
//				float val = values[i] - min;
//				float rat = val / diff;
//				float h = graphheight * rat;
//				if (i > 0)
//					canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
//				lasth = h;
//			}
//		}
	}
	
	//Draw the line graph
//	   public void drawLineGraph(Canvas canvas) {
//		   
//	       float i, x1, y1, x2, y2, largestNumber, xIncrement, yIncrement;
//	       //Compute the x and y increments
//	       largestNumber = getMax();
//	       xIncrement = totalX / numberOfScores;
//	       if (largestNumber ==0) {
//	               yIncrement = 0;
//	       } else {
//	               yIncrement = totalY / largestNumber;
//	       }
//	       
//	      //Set the initial origin point
//	       x1 = Xleft;
//	       y1 = Ybottom;
//
//	      //Compute and plot the data points
//	       for(i=0; i < numberOfScores; i++)
//	      {
//	          x2 = getXCoordinate(i+1, xIncrement);
//	          y2 = getYCoordinate(scores[i], yIncrement);
//	          g.fillOval(x2, y2, 5, 5);
//	          g.drawLine(x1, y1, x2, y2);
//	          x1 = x2;
//	          y1 = y2;
//	       }
//	      //Label x - axes with grade choices
//	      String [ ] label = {"A", "B", "C", "D", "F"};
//	      for (i=1; i<=numberOfScores; i++)
//	          g.drawString(label[i-1], 100+ i*xIncrement, 270);
//
//	      //Label y - axes with quantity of each grade
//	      int topy;
//	      if(largestNumber%10==0)
//	           topy=largestNumber;
//	      else
//	           topy = (largestNumber/10+1)*10;
//	     
//	      // i = i+5 controls y value label -- adjust for size of data
//	      for (i = 0; i <= topy; i = i+5)
//	      {
//	        g.drawString(String.valueOf(i), 70, Ybottom-i*yIncrement+5);
//	      }
//	   }
	   
	private void drawAxes (Canvas canvas, float border, float height, float width) {
	   paint.setTextAlign(Align.LEFT);		
	   paint.setColor(Color.parseColor("#0A7BE4"));
	   paint.setStrokeWidth(3.5f);
	
	   final float y = height - border + 2;
	   // Draw X
	   canvas.drawLine(border, y , width - border, y, paint);			
	   // Draw Y
	   canvas.drawLine(border, border, border, y, paint);
    }

	private float getMax() {
		float largest = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; i++)
			if (values[i] > largest)
				largest = values[i];
		return largest;
	}

	private float getMin() {
		float smallest = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; i++)
			if (values[i] < smallest)
				smallest = values[i];
		return smallest;
	}

}
