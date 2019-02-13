package com.forixusa.android.animations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
	public static Animation inFromRightAnimation() {
	    Animation inFromRight = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, +1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromRight.setDuration(500);
	    inFromRight.setInterpolator(new AccelerateInterpolator());
	    return inFromRight;
	}
	
	public static Animation inFromLeftAnimation() {
	    Animation inFromRight = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, -1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromRight.setDuration(500);
	    inFromRight.setInterpolator(new AccelerateInterpolator());
	    return inFromRight;
	}
	
	public static Animation outToLeftAnimation() {
	    Animation outtoLeft = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, -1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(500);
	    outtoLeft.setInterpolator(new AccelerateInterpolator());
	    return outtoLeft;
	}
	
	public static Animation outToRightAnimation() {
	    Animation outtoLeft = new TranslateAnimation(
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, +1.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f,
	            Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(500);
	    outtoLeft.setInterpolator(new AccelerateInterpolator());
	    return outtoLeft;
	}
}
