package com.forixusa.scoretimer.android.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Chronometer.OnChronometerTickListener;

import com.forixusa.android.utils.NumberHelper;
import com.forixusa.scoretimer.android.ScoreTimerApplication;
import com.forixusa.scoretimer.android.models.BiologicalSciences;
import com.forixusa.scoretimer.android.models.PhysicalSciences;
import com.forixusa.scoretimer.android.models.ScoreResult;
import com.forixusa.scoretimer.android.models.VerbalReasoning;
import com.forixusa.scoretimer.android.services.ScoreResultService;
import com.forixusa.scoretimer.android.tabs.TabActivityGroup;

public class TimerActivity extends ScoreTimerBaseActivity implements OnClickListener {
	private static final String TAG = TimerActivity.class.getSimpleName();

	private static final String TIMER_FORMAT = "%02d:%02d:%02d";

	private Button mBtnStartReading;
	private Button mBtnStartQuestion;
	private Button mBtnDone;
	private Button mBtnGetResult;
	private Button mBtnResetData;
	private Chronometer mChrCountTimer;
	
	private Spinner mSpnNumOfQuestion;
	private Spinner mSpnNumOfCorrectAnswer;
	
	private int mNumberOfCorrect;
	private int mQuestionCompleted;
	
	private double mPassageTimerInput;
	private double mQuestionTimerInput;
	
	private boolean mIsStartReading;
	private long mElapsedTime;
	
	private ArrayAdapter<String> mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");
		View contentView = LayoutInflater.from(getParent()).inflate(R.layout.timer_activity, null);
		setContentView(contentView);

		//setContentView(R.layout.timer_activity);
		
		
		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		loadUIControls();
		listenerUIControls();
		
		String[] items = new String[25];
		for(int i = 0; i < 25; i++) {
			items[i] = String.valueOf(i + 1);
		}
		
		mAdapter = new ArrayAdapter<String>(getParent(),
			    android.R.layout.simple_spinner_item, items);    
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    mSpnNumOfQuestion.setAdapter(mAdapter);
	    mSpnNumOfCorrectAnswer.setAdapter(mAdapter);
	    
	    resetFields();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		if(ScoreTimerApplication.sIsBiological) {
			mBtnStartReading.setVisibility(View.GONE);
		} else {
			mBtnStartReading.setVisibility(View.VISIBLE);
		}
		resetFields();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Log.i(TAG, "onWindowFocusChanged");
	}


	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mBtnStartReading = (Button) findViewById(R.id.btnStartReading);
		mBtnStartQuestion = (Button) findViewById(R.id.btnStartQuestion);
		mBtnDone = (Button) findViewById(R.id.btnDone);
		mBtnGetResult = (Button) findViewById(R.id.btnGetResult);
		mBtnResetData = (Button) findViewById(R.id.btnResetData);

		mChrCountTimer = (Chronometer) findViewById(R.id.chrCountTimer);
		
		mSpnNumOfQuestion = (Spinner) findViewById(R.id.spnNumOfQuestion);
		mSpnNumOfCorrectAnswer = (Spinner) findViewById(R.id.spnNumOfCorrectAnswer);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnStartReading.setOnClickListener(this);
		mBtnStartQuestion.setOnClickListener(this);
		mBtnDone.setOnClickListener(this);
		mBtnGetResult.setOnClickListener(this);
		mBtnResetData.setOnClickListener(this);
		
		mSpnNumOfCorrectAnswer.setOnItemSelectedListener(new OnItemSelectedListener() {    
			 @Override
			 public void onItemSelected(AdapterView adapter, View v, int i, long lng) {	
				 mNumberOfCorrect = i+1;
			} 
			 
			@Override     
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		}); 
		
		mSpnNumOfQuestion.setOnItemSelectedListener(new OnItemSelectedListener() {    
			 @Override
			 public void onItemSelected(AdapterView adapter, View v, int i, long lng) {	
				 mQuestionCompleted = i+1;
			} 
			  @Override     
			  public void onNothingSelected(AdapterView<?> parentView) {         
			 }
		}); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnStartReading:
			mIsStartReading = true;
			
			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			mBtnStartReading.setEnabled(false);
			
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			mBtnStartQuestion.setEnabled(true);
			
			mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
		    mBtnDone.setEnabled(false);
			startCountTimer();
			break;
		case R.id.btnStartQuestion:
			if(mIsStartReading) {
				mPassageTimerInput = (double)mElapsedTime/(1000*60);
			}
			
			mIsStartReading = false;
			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			mBtnStartReading.setEnabled(false);
			
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
			
			mBtnDone.setBackgroundResource(R.drawable.btn_done);
		    mBtnDone.setEnabled(true);
			startCountTimer();
			break;
		case R.id.btnDone:		
			mChrCountTimer.stop();
			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading);
			mBtnStartReading.setEnabled(true);
			
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
			
			mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
		    mBtnDone.setEnabled(false);
		    
//			if(mIsStartReading) {
//				mPassageTimerInput = (double)mElapsedTime/(1000*60);
//			} else {
				mQuestionTimerInput  = (double)mElapsedTime/(1000*60);
//			}
			break;
		case R.id.btnGetResult:
			getResult();			
			break;
		case R.id.btnResetData:
			resetConfirmMessage(getParent(), "Are you sure you want to reset the data?");
			break;
		}
	}
	
	private void resetConfirmMessage(Context context, String message) {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle("Message");
		alertbox.setMessage(message);

		alertbox.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				mChrCountTimer.stop();
			
				resetFields();
				dialog.dismiss();
			}
		});
		
		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		alertbox.show();
	}
	
	private void resetFields() {
		mChrCountTimer.setText("00:00:00");
		mNumberOfCorrect = 1;
		mQuestionCompleted = 1;
		mPassageTimerInput = 0;
		mQuestionTimerInput = 0;
		mSpnNumOfCorrectAnswer.setSelection(0);
		mSpnNumOfQuestion.setSelection(0);
		
		mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
	    mBtnDone.setEnabled(false);
	    
	    mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading);
		mBtnStartReading.setEnabled(true);
		
		if(ScoreTimerApplication.sIsBiological) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}
	}
	
	private void getResult() {
		
		Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);
		
		if(mNumberOfCorrect > mQuestionCompleted) {
			Toast msg = Toast.makeText(getParent(),
					"The number of question must be larger than number of correct answres", 
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}
		
		if(mPassageTimerInput == 0 && mQuestionTimerInput == 0) {
			Toast msg = Toast.makeText(getParent(),
					"You have to start the test in order to get the result.", 
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}
		
		ScoreResult scoreResult = null;
		if(ScoreTimerApplication.sIsBiological) {
			scoreResult =  biologicalSciencesProcess();
		} else if(ScoreTimerApplication.sIsVerbal) {
			scoreResult = verbalReasoningProcess();
		} else if(ScoreTimerApplication.sIsPhysical) {
			scoreResult = physicalSciencesProcess();
		}
		
		final Intent intentResult = new Intent(getParent(), ResultActivity.class);
		final TabActivityGroup parentActivity = (TabActivityGroup) getParent();
		
		final Bundle bundle = new Bundle();
		bundle.putDouble("scoreResult.accuracy", NumberHelper.roundTwoDecimals(scoreResult.accuracy));
		bundle.putDouble("scoreResult.pace", NumberHelper.roundTwoDecimals(scoreResult.pace));
		bundle.putDouble("scoreResult.estimationCorrect", NumberHelper.roundTwoDecimals(scoreResult.estimationCorrect));
		bundle.putDouble("scoreResult.estimationScore", NumberHelper.roundTwoDecimals(scoreResult.estimationScore));
		
		intentResult.putExtras(bundle);
		
		parentActivity.startChildActivity("ResultActivity", intentResult);
	}
	
	private ScoreResult biologicalSciencesProcess() {
		
//		mPassageTimerInput = 0;
//		mQuestionTimerInput = 10;
//		mQuestionCompleted = 6;
//		mNumberOfCorrect = 5;
			
		ScoreResult scoreResult = new ScoreResult();
		
		scoreResult.accuracy = (double)mNumberOfCorrect/ mQuestionCompleted;
		scoreResult.pace = BiologicalSciences.TOTAL_PASSAGES * BiologicalSciences.TIME / 
							(BiologicalSciences.TOTAL_PASSAGES * mPassageTimerInput + BiologicalSciences.TOTAL_QUESTIONS * mQuestionTimerInput / mQuestionCompleted) ;
		
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * BiologicalSciences.TOTAL_QUESTIONS / BiologicalSciences.TOTAL_PASSAGES + 
										(1 - scoreResult.pace/BiologicalSciences.TOTAL_PASSAGES)*BiologicalSciences.GUESSING_ACCURACY * BiologicalSciences.TOTAL_QUESTIONS;
		
		scoreResult.estimationScore = BiologicalSciences.FORMULA_CONSTANT1 * scoreResult.estimationCorrect + BiologicalSciences.FORMULA_CONSTANT2 ;
		
		return scoreResult;
	}
	
	private ScoreResult verbalReasoningProcess() {
		
//		mPassageTimerInput = 4.5;
//		mQuestionTimerInput = 5;
//		mQuestionCompleted = 6;
//		mNumberOfCorrect = 5;
			
		ScoreResult scoreResult = new ScoreResult();
		
		scoreResult.accuracy = (double)mNumberOfCorrect/ mQuestionCompleted;
		scoreResult.pace = VerbalReasoning.TOTAL_PASSAGES * VerbalReasoning.TIME / 
							(VerbalReasoning.TOTAL_PASSAGES * mPassageTimerInput + VerbalReasoning.TOTAL_QUESTIONS * mQuestionTimerInput / mQuestionCompleted) ;
		
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * VerbalReasoning.TOTAL_QUESTIONS / VerbalReasoning.TOTAL_PASSAGES + 
										(1 - scoreResult.pace/VerbalReasoning.TOTAL_PASSAGES)*VerbalReasoning.GUESSING_ACCURACY * VerbalReasoning.TOTAL_QUESTIONS;
		
		scoreResult.estimationScore = VerbalReasoning.FORMULA_CONSTANT1 * scoreResult.estimationCorrect + VerbalReasoning.FORMULA_CONSTANT2 ;
		
		return scoreResult;
	}

	private ScoreResult physicalSciencesProcess() {
		
//		mPassageTimerInput = 4.5;
//		mQuestionTimerInput = 5;
//		mQuestionCompleted = 6;
//		mNumberOfCorrect = 5;
			
		ScoreResult scoreResult = new ScoreResult();
		
		scoreResult.accuracy = (double)mNumberOfCorrect/ mQuestionCompleted;
		scoreResult.pace = PhysicalSciences.TOTAL_PASSAGES * PhysicalSciences.TIME / 
							(PhysicalSciences.TOTAL_PASSAGES * mPassageTimerInput + PhysicalSciences.TOTAL_QUESTIONS * mQuestionTimerInput / mQuestionCompleted) ;
		
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * PhysicalSciences.TOTAL_QUESTIONS / PhysicalSciences.TOTAL_PASSAGES + 
										(1 - scoreResult.pace/PhysicalSciences.TOTAL_PASSAGES)*PhysicalSciences.GUESSING_ACCURACY * PhysicalSciences.TOTAL_QUESTIONS;
		
		scoreResult.estimationScore = PhysicalSciences.FORMULA_CONSTANT1 * scoreResult.estimationCorrect + PhysicalSciences.FORMULA_CONSTANT2 ;
		
		return scoreResult;
	}
	
	public void startCountTimer() {
		mElapsedTime = 0;
		mChrCountTimer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer arg0) {
				long secondTime = ((mElapsedTime - mChrCountTimer.getBase()) / 1000);
				long lday = (secondTime % (60 * 60 * 24));
				long hours = (int) (lday / 3600);
				long lhour = (lday % (60 * 60));
				long minutes = (int) (lhour / 60);
				long lmin = (lhour % (60));
				long seconds = (int) (lmin);

				String currentTime = String.format(TIMER_FORMAT, hours, minutes, seconds);
				arg0.setText(currentTime);

				mElapsedTime += 1000;
			}
		});

		mChrCountTimer.setBase(0);
		mChrCountTimer.start();

	}
}
