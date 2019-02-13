package com.forixusa.scoretimer.android.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.forixusa.android.utils.NumberHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimer.android.activities.R;
import com.forixusa.scoretimer.android.adapter.SpinnerAdapter;
import com.forixusa.scoretimer.android.models.BiologicalSciences;
import com.forixusa.scoretimer.android.models.PhysicalSciences;
import com.forixusa.scoretimer.android.models.ScoreResult;
import com.forixusa.scoretimer.android.models.VerbalReasoning;

public class TimerView extends ContentView implements OnClickListener {
	private static final String TAG = TimerView.class.getSimpleName();

	private static final String TIMER_FORMAT = "%02d:%02d";

	private Button mBtnTestOptions;
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
	private boolean mIsStartQuestion;
	private long mElapsedTime;
	public static int sTestOption = TestOptionView.BIOLOGICAL_SCIENCES;
	public static boolean sIsFromTimer = false;

	private final SpinnerAdapter mAdapterNumberOfQuestion;
	private final SpinnerAdapter mAdapterCorrectAnswer;
	private final ArrayList<String> mSpinnerItems;

	public TimerView(Context context) {
		super(context);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.timer_activity, this, true);

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		loadUIControls();
		listenerUIControls();

		mSpinnerItems = new ArrayList<String>();
		mSpinnerItems.add("");
		for (int i = 1; i <= 99; i++) {
			mSpinnerItems.add(String.valueOf(i));
		}

		mAdapterNumberOfQuestion = new SpinnerAdapter(mContext, mSpinnerItems);
		mAdapterNumberOfQuestion.setDropDownViewResource(R.layout.custom_spinner_item);
		mSpnNumOfQuestion.setAdapter(mAdapterNumberOfQuestion);

		mAdapterCorrectAnswer = new SpinnerAdapter(mContext, new ArrayList<String>());
		mAdapterCorrectAnswer.setDropDownViewResource(R.layout.custom_spinner_item);
		mSpnNumOfCorrectAnswer.setAdapter(mAdapterCorrectAnswer);

		resetFields();
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResume");

		mBtnStartReading.setVisibility(View.VISIBLE);

		if (sTestOption == TestOptionView.BIOLOGICAL_SCIENCES) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt1);
			mBtnStartReading.setVisibility(View.GONE);
		} else if (sTestOption == TestOptionView.VERBAL_REASONING) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt2);
		} else if (sTestOption == TestOptionView.PHYSICAL_SCIENCES) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt3);
		}

		Log.i(TAG, "mIsStartReading:" + mIsStartReading);
		if (sTestOption == TestOptionView.BIOLOGICAL_SCIENCES && !mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}

		if (SummaryView.sNeedToRefresh) {
			resetFields();
			SummaryView.sNeedToRefresh = false;
		}

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

		mBtnTestOptions = (Button) findViewById(R.id.btnTestOptions);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnStartReading.setOnClickListener(this);
		mBtnStartQuestion.setOnClickListener(this);
		mBtnDone.setOnClickListener(this);
		mBtnGetResult.setOnClickListener(this);
		mBtnResetData.setOnClickListener(this);
		mBtnTestOptions.setOnClickListener(this);

		mSpnNumOfCorrectAnswer.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
				if (i == 0) {
					mNumberOfCorrect = 0;
				} else {
					mNumberOfCorrect = Integer.parseInt(mSpinnerItems.get(i));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		mSpnNumOfQuestion.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
				Log.i(TAG, "mSpnNumOfQuestion");
				if (i == 0) {
					mQuestionCompleted = 0;
					mAdapterCorrectAnswer.setData(new ArrayList<String>());
					mAdapterCorrectAnswer.notifyDataSetChanged();
				} else {
					mQuestionCompleted = Integer.parseInt(mSpinnerItems.get(i));
					Log.i(TAG, "mQuestionCompleted:" + mQuestionCompleted);
					ArrayList<String> list = new ArrayList<String>();
					list.add("");
					for (int index = 1; index <= mQuestionCompleted; index++) {
						list.add(String.valueOf(index));
					}

					mAdapterCorrectAnswer.setData(list);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mAdapterCorrectAnswer.notifyDataSetChanged();
						}
					});

				}

				mSpnNumOfCorrectAnswer.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");

		switch (v.getId()) {

		case R.id.btnStartReading:
			Log.i(TAG, "btnStartReading");
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
			Log.i(TAG, "btnStartQuestion");
			mIsStartQuestion = true;
			Log.i(TAG, "mIsStartReading:" + mIsStartReading);

			if (mIsStartReading) {
				mPassageTimerInput = (double) mElapsedTime / (1000 * 60);
			}

			if (!mIsStartReading) {
				startCountTimer();
			}
			mIsStartReading = false;
			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			mBtnStartReading.setEnabled(false);

			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);

			mBtnDone.setBackgroundResource(R.drawable.btn_done);
			mBtnDone.setEnabled(true);

			// startCountTimer();
			break;
		case R.id.btnDone:
			mChrCountTimer.stop();
			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			mBtnStartReading.setEnabled(false);

			// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			// mBtnStartQuestion.setEnabled(false);
			// if(sTestOption == TestOptionView.BIOLOGICAL_SCIENCES) {
			// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			// mBtnStartQuestion.setEnabled(true);
			// } else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
			// }

			// mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
			// mBtnDone.setEnabled(false);

			mQuestionTimerInput = (double) mElapsedTime / (1000 * 60);
			break;
		case R.id.btnGetResult:
			getResult();
			break;
		case R.id.btnResetData:
			resetConfirmMessage(mContext, "Are you sure you want to reset the data?");
			break;
		case R.id.btnTestOptions:
			resetFields();
			sIsFromTimer = true;
			final TestOptionView view = new TestOptionView(mContext, sTestOption);
			mActivity.setContentView(view);
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
		mChrCountTimer.setText("00:00");
		mNumberOfCorrect = 0;
		mQuestionCompleted = 0;
		mPassageTimerInput = 0;
		mQuestionTimerInput = 0;
		mSpnNumOfCorrectAnswer.setSelection(0);
		mSpnNumOfQuestion.setSelection(0);

		mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
		mBtnDone.setEnabled(false);

		mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading);
		mBtnStartReading.setEnabled(true);

		if (sTestOption == TestOptionView.BIOLOGICAL_SCIENCES) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}

		mChrCountTimer.stop();

		mIsStartReading = false;
		mIsStartQuestion = false;
	}

	private void getResult() {

		// Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		// Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		// Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		// Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);

		if (sTestOption != TestOptionView.BIOLOGICAL_SCIENCES && mPassageTimerInput == 0 || mQuestionTimerInput == 0) {
			final Toast msg = Toast.makeText(mContext, "You have to start the test in order to get the result.",
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}

		if (mNumberOfCorrect == 0 || mQuestionCompleted == 0) {
			final Toast msg = Toast.makeText(mContext, "Fields number of questions, correct answer are required.",
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}

		if (mNumberOfCorrect > mQuestionCompleted) {
			final Toast msg = Toast.makeText(mContext,
					"The number of correct answers can not exceed the total number of questions", Toast.LENGTH_LONG);
			msg.show();
			return;
		}

		ScoreResult scoreResult = null;
		if (sTestOption == TestOptionView.BIOLOGICAL_SCIENCES) {
			scoreResult = biologicalSciencesProcess();
			scoreResult.testOption = String.valueOf(TestOptionView.BIOLOGICAL_SCIENCES);
		} else if (sTestOption == TestOptionView.VERBAL_REASONING) {
			scoreResult = verbalReasoningProcess();
			scoreResult.testOption = String.valueOf(TestOptionView.VERBAL_REASONING);
		} else if (sTestOption == TestOptionView.PHYSICAL_SCIENCES) {
			scoreResult = physicalSciencesProcess();
			scoreResult.testOption = String.valueOf(TestOptionView.PHYSICAL_SCIENCES);
		}

		final Bundle bundle = new Bundle();
		bundle.putDouble("scoreResult.accuracy", NumberHelper.roundTwoDecimals(scoreResult.accuracy));
		bundle.putDouble("scoreResult.pace", NumberHelper.roundTwoDecimals(scoreResult.pace));
		bundle.putDouble("scoreResult.estimationCorrect", NumberHelper.roundTwoDecimals(scoreResult.estimationCorrect));
		bundle.putDouble("scoreResult.estimationScore", NumberHelper.roundTwoDecimals(scoreResult.estimationScore));
		bundle.putString("scoreResult.testOption", scoreResult.testOption);

		final ResultView view = new ResultView(mContext, bundle);
		mActivity.setContentView(view);
	}

	private ScoreResult biologicalSciencesProcess() {
		Log.d(TAG, "biologicalSciencesProcess");
		// mPassageTimerInput = 0;
		// mQuestionTimerInput = 15;
		// mQuestionCompleted = 6;
		// mNumberOfCorrect = 5;

		Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;

		scoreResult.pace = BiologicalSciences.TOTAL_PASSAGES
				* BiologicalSciences.TIME
				/ (BiologicalSciences.TOTAL_PASSAGES * mPassageTimerInput + BiologicalSciences.TOTAL_QUESTIONS
						* mQuestionTimerInput / mQuestionCompleted);

		if (scoreResult.pace > BiologicalSciences.TOTAL_PASSAGES) {
			scoreResult.pace = BiologicalSciences.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * BiologicalSciences.TOTAL_QUESTIONS
				/ BiologicalSciences.TOTAL_PASSAGES + (1 - scoreResult.pace / BiologicalSciences.TOTAL_PASSAGES)
				* BiologicalSciences.GUESSING_ACCURACY * BiologicalSciences.TOTAL_QUESTIONS;

		scoreResult.estimationScore = BiologicalSciences.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ BiologicalSciences.FORMULA_CONSTANT2;

		Log.d(TAG, "scoreResult.accuracy:" + scoreResult.accuracy);
		Log.d(TAG, "scoreResult.pace:" + scoreResult.pace);
		Log.d(TAG, "scoreResult.estimationCorrect:" + scoreResult.estimationCorrect);
		Log.d(TAG, "scoreResult.estimationScore:" + scoreResult.estimationScore);

		return scoreResult;
	}

	private ScoreResult verbalReasoningProcess() {
		Log.d(TAG, "verbalReasoningProcess");
		// mPassageTimerInput = 4.5;
		// mQuestionTimerInput = 5;
		// mQuestionCompleted = 6;
		// mNumberOfCorrect = 5;
		Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = VerbalReasoning.TOTAL_PASSAGES
				* VerbalReasoning.TIME
				/ (VerbalReasoning.TOTAL_PASSAGES * mPassageTimerInput + VerbalReasoning.TOTAL_QUESTIONS
						* mQuestionTimerInput / mQuestionCompleted);

		if (scoreResult.pace > VerbalReasoning.TOTAL_PASSAGES) {
			scoreResult.pace = VerbalReasoning.TOTAL_PASSAGES;
		}

		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * VerbalReasoning.TOTAL_QUESTIONS
				/ VerbalReasoning.TOTAL_PASSAGES + (1 - scoreResult.pace / VerbalReasoning.TOTAL_PASSAGES)
				* VerbalReasoning.GUESSING_ACCURACY * VerbalReasoning.TOTAL_QUESTIONS;

		scoreResult.estimationScore = VerbalReasoning.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ VerbalReasoning.FORMULA_CONSTANT2;

		return scoreResult;
	}

	private ScoreResult physicalSciencesProcess() {
		Log.d(TAG, "physicalSciencesProcess");
		// mPassageTimerInput = 4.5;
		// mQuestionTimerInput = 5;
		// mQuestionCompleted = 6;
		// mNumberOfCorrect = 5;
		Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = PhysicalSciences.TOTAL_PASSAGES
				* PhysicalSciences.TIME
				/ (PhysicalSciences.TOTAL_PASSAGES * mPassageTimerInput + PhysicalSciences.TOTAL_QUESTIONS
						* mQuestionTimerInput / mQuestionCompleted);
		if (scoreResult.pace > PhysicalSciences.TOTAL_PASSAGES) {
			scoreResult.pace = PhysicalSciences.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * PhysicalSciences.TOTAL_QUESTIONS
				/ PhysicalSciences.TOTAL_PASSAGES + (1 - scoreResult.pace / PhysicalSciences.TOTAL_PASSAGES)
				* PhysicalSciences.GUESSING_ACCURACY * PhysicalSciences.TOTAL_QUESTIONS;

		scoreResult.estimationScore = PhysicalSciences.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ PhysicalSciences.FORMULA_CONSTANT2;

		return scoreResult;
	}

	public void startCountTimer() {
		mElapsedTime = 0;
		mChrCountTimer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer arg0) {
				final long secondTime = (mElapsedTime - mChrCountTimer.getBase()) / 1000;
				final long lday = secondTime % (60 * 60 * 24);
				final long hours = (int) (lday / 3600);
				final long lhour = lday % (60 * 60);
				final long minutes = (int) (lhour / 60);
				final long lmin = lhour % 60;
				final long seconds = (int) lmin;

				final String currentTime = String.format(TIMER_FORMAT, minutes, seconds);
				arg0.setText(currentTime);

				mElapsedTime += 1000;
			}
		});

		mChrCountTimer.setBase(0);
		mChrCountTimer.start();

	}
}
