package com.forixusa.scoretimersat.android.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.forixusa.android.quickaction.ActionItem;
import com.forixusa.android.quickaction.QuickAction;
import com.forixusa.android.utils.NumberHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimersat.android.ScoreTimerApplication;
import com.forixusa.scoretimersat.android.activities.R;
import com.forixusa.scoretimersat.android.adapter.SpinnerAdapter;
import com.forixusa.scoretimersat.android.models.SatMath;
import com.forixusa.scoretimersat.android.models.SatReading;
import com.forixusa.scoretimersat.android.models.SatWriting;
import com.forixusa.scoretimersat.android.models.ScoreResult;

public class TimerView extends ContentView implements OnClickListener {
	private static final String TAG = TimerView.class.getSimpleName();

	private static final String TIMER_FORMAT = "%02d:%02d:%02d";

	private Button mBtnTestOptions;
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

	private boolean mIsStartQuestion;
	private long mElapsedTime;

	private final SpinnerAdapter mAdapterNumberOfQuestion;
	private final SpinnerAdapter mAdapterCorrectAnswer;
	private final ArrayList<String> mSpinnerItems;

	private ImageView mImgTimerHint;
	private ImageView mImgQuestionHint;
	private ImageView mImgButtonHint;

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
		initTitle();

		if (!mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResume");

		initTitle();

		if (!mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}

		if (ScoreTimerApplication.sNeedToRefresh) {
			resetFields();
			ScoreTimerApplication.sNeedToRefresh = false;
		}

	}

	private void initTitle() {
		Log.i(TAG, "initTitle");

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_MATH) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt1);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_READING) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt2);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_WRITING) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt3);
		}

	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mBtnStartQuestion = (Button) findViewById(R.id.btnStartQuestion);
		mBtnDone = (Button) findViewById(R.id.btnDone);
		mBtnGetResult = (Button) findViewById(R.id.btnGetResult);
		mBtnResetData = (Button) findViewById(R.id.btnResetData);

		mChrCountTimer = (Chronometer) findViewById(R.id.chrCountTimer);

		mSpnNumOfQuestion = (Spinner) findViewById(R.id.spnNumOfQuestion);
		mSpnNumOfCorrectAnswer = (Spinner) findViewById(R.id.spnNumOfCorrectAnswer);

		mBtnTestOptions = (Button) findViewById(R.id.btnTestOptions);

		mImgTimerHint = (ImageView) findViewById(R.id.imgTimerHint);
		mImgQuestionHint = (ImageView) findViewById(R.id.imgQuestionHint);
		mImgButtonHint = (ImageView) findViewById(R.id.imgButtonHint);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnStartQuestion.setOnClickListener(this);
		mBtnDone.setOnClickListener(this);
		mBtnGetResult.setOnClickListener(this);
		mBtnResetData.setOnClickListener(this);
		mBtnTestOptions.setOnClickListener(this);

		mImgTimerHint.setOnClickListener(this);
		mImgQuestionHint.setOnClickListener(this);
		mImgButtonHint.setOnClickListener(this);

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

		case R.id.btnStartQuestion:
			Log.i(TAG, "btnStartQuestion");
			mIsStartQuestion = true;

			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);

			mBtnDone.setBackgroundResource(R.drawable.btn_done);
			mBtnDone.setEnabled(true);

			startCountTimer();
			break;
		case R.id.btnDone:
			mChrCountTimer.stop();
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);

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
			ScoreTimerApplication.sIsFromTimer = true;
			final TestOptionView view = new TestOptionView(mContext, ScoreTimerApplication.sTimerTestOption);
			mActivity.setContentView(view);
			break;

		case R.id.imgTimerHint:
			final QuickAction quickAction1 = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem timertItem = new ActionItem(1,
					"To begin, hit the appropriate timer button(s) to track your time, then hit DONE when finished.",
					null);

			quickAction1.addActionItem(timertItem);

			quickAction1.show(v);
			closeToolTip(quickAction1);

			break;
		case R.id.imgQuestionHint:
			final QuickAction quickAction2 = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem questionItem = new ActionItem(1,
					"Grade your results, then ENTER the number of questions you did and the number you got correct.",
					null);

			quickAction2.addActionItem(questionItem);
			quickAction2.show(v);
			closeToolTip(quickAction2);
			break;
		case R.id.imgButtonHint:
			final QuickAction quickAction3 = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem buttonItem = new ActionItem(1, "Hit GET RESULTS to get your summary.          ", null);

			quickAction3.addActionItem(buttonItem);

			quickAction3.show(v);
			closeToolTip(quickAction3);

			break;
		}
	}

	private void closeToolTip(final QuickAction quickAction) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				quickAction.dismiss();
			}
		}, 3000);
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
		mChrCountTimer.setText("00:00:00");
		mNumberOfCorrect = 0;
		mQuestionCompleted = 0;
		mPassageTimerInput = 0;
		mQuestionTimerInput = 0;
		mSpnNumOfCorrectAnswer.setSelection(0);
		mSpnNumOfQuestion.setSelection(0);

		mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
		mBtnDone.setEnabled(false);

		mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
		mBtnStartQuestion.setEnabled(true);

		mChrCountTimer.stop();
		mIsStartQuestion = false;
	}

	private void getResult() {

		// Log.d(TAG, "mPassageTimerInput:" + mPassageTimerInput);
		// Log.d(TAG, "mQuestionTimerInput:" + mQuestionTimerInput);
		// Log.d(TAG, "mQuestionCompleted:" + mQuestionCompleted);
		// Log.d(TAG, "mNumberOfCorrect:" + mNumberOfCorrect);

		if (mQuestionTimerInput == 0) {
			final Toast msg = Toast.makeText(mContext, "You have to start the test in order to get the result.",
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}

		if (mNumberOfCorrect == 0 && mQuestionCompleted == 0) {
			final Toast msg = Toast.makeText(mContext, "Fields number of questions, correct answer are required.",
					Toast.LENGTH_LONG);
			msg.show();
			return;
		}

		if (mNumberOfCorrect == 0) {
			final Toast msg = Toast.makeText(mContext, "Fields Correct Answer are required.", Toast.LENGTH_LONG);
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

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_MATH) {
			scoreResult = satMath();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.SAT_MATH);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_READING) {
			scoreResult = satReading();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.SAT_READING);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.SAT_WRITING) {
			scoreResult = satWriting();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.SAT_WRITING);
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

	private ScoreResult satMath() {
		Log.d(TAG, "satMath");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;

		scoreResult.pace = SatMath.TOTAL_PASSAGES
				* SatMath.TIME
				/ (SatMath.TOTAL_PASSAGES * mPassageTimerInput + SatMath.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > SatMath.TOTAL_PASSAGES) {
			scoreResult.pace = SatMath.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * SatMath.TOTAL_QUESTIONS
				/ SatMath.TOTAL_PASSAGES + (1 - scoreResult.pace / SatMath.TOTAL_PASSAGES) * SatMath.GUESSING_ACCURACY
				* SatMath.TOTAL_QUESTIONS;

		scoreResult.estimationScore = SatMath.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ SatMath.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < SatMath.MINIMUM_SCORE) {
			scoreResult.estimationScore = SatMath.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > SatMath.MAXIMUM_SCORE) {
			scoreResult.estimationScore = SatMath.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult satWriting() {
		Log.d(TAG, "satWriting");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = SatWriting.TOTAL_PASSAGES
				* SatWriting.TIME
				/ (SatWriting.TOTAL_PASSAGES * mPassageTimerInput + SatWriting.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > SatWriting.TOTAL_PASSAGES) {
			scoreResult.pace = SatWriting.TOTAL_PASSAGES;
		}

		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * SatWriting.TOTAL_QUESTIONS
				/ SatWriting.TOTAL_PASSAGES + (1 - scoreResult.pace / SatWriting.TOTAL_PASSAGES)
				* SatWriting.GUESSING_ACCURACY * SatWriting.TOTAL_QUESTIONS;

		scoreResult.estimationScore = SatWriting.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ SatWriting.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < SatWriting.MINIMUM_SCORE) {
			scoreResult.estimationScore = SatWriting.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > SatWriting.MAXIMUM_SCORE) {
			scoreResult.estimationScore = SatWriting.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult satReading() {
		Log.d(TAG, "satReading");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = SatReading.TOTAL_PASSAGES
				* SatReading.TIME
				/ (SatReading.TOTAL_PASSAGES * mPassageTimerInput + SatReading.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);
		if (scoreResult.pace > SatReading.TOTAL_PASSAGES) {
			scoreResult.pace = SatReading.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * SatReading.TOTAL_QUESTIONS
				/ SatReading.TOTAL_PASSAGES + (1 - scoreResult.pace / SatReading.TOTAL_PASSAGES)
				* SatReading.GUESSING_ACCURACY * SatReading.TOTAL_QUESTIONS;

		scoreResult.estimationScore = SatReading.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ SatReading.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < SatReading.MINIMUM_SCORE) {
			scoreResult.estimationScore = SatReading.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > SatReading.MAXIMUM_SCORE) {
			scoreResult.estimationScore = SatReading.MAXIMUM_SCORE;
		}

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

				final String currentTime = String.format(TIMER_FORMAT, hours, minutes, seconds);
				arg0.setText(currentTime);

				mElapsedTime += 1000;
			}
		});

		mChrCountTimer.setBase(0);
		mChrCountTimer.start();

	}
}
