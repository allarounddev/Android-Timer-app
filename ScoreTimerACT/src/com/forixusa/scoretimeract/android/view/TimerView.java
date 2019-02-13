package com.forixusa.scoretimeract.android.view;

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
import com.forixusa.scoretimeract.android.ScoreTimerApplication;
import com.forixusa.scoretimeract.android.activities.R;
import com.forixusa.scoretimeract.android.adapter.SpinnerAdapter;
import com.forixusa.scoretimeract.android.models.ActEnglish;
import com.forixusa.scoretimeract.android.models.ActMath;
import com.forixusa.scoretimeract.android.models.ActReading;
import com.forixusa.scoretimeract.android.models.ActScience;
import com.forixusa.scoretimeract.android.models.ScoreResult;

public class TimerView extends ContentView implements OnClickListener {
	private static final String TAG = TimerView.class.getSimpleName();

	private static final String TIMER_FORMAT = "%02d:%02d:%02d";

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

		// if (!mIsStartQuestion) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
		// mBtnStartQuestion.setEnabled(true);
		// } else {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
		// mBtnStartQuestion.setEnabled(false);
		// }

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH && !mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}

		if (ScoreTimerApplication.sTimerTestOption != ScoreTimerApplication.ACT_MATH && mIsStartReading) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		}
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResume");

		initTitle();

		// if (!mIsStartQuestion) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
		// mBtnStartQuestion.setEnabled(true);
		// } else {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
		// mBtnStartQuestion.setEnabled(false);
		// }

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH && !mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);
		}

		if (ScoreTimerApplication.sTimerTestOption != ScoreTimerApplication.ACT_MATH && mIsStartReading) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		}

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH) {
			mBtnStartReading.setVisibility(View.GONE);
		} else {
			mBtnStartReading.setVisibility(View.VISIBLE);
		}

		if (ScoreTimerApplication.sNeedToRefresh) {
			resetFields();
			ScoreTimerApplication.sNeedToRefresh = false;
		}

	}

	private void initTitle() {
		Log.i(TAG, "initTitle");

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_ENGLISH) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt1);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt2);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_READING) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt3);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_SCIENCE) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt4);
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

		mImgTimerHint = (ImageView) findViewById(R.id.imgTimerHint);
		mImgQuestionHint = (ImageView) findViewById(R.id.imgQuestionHint);
		mImgButtonHint = (ImageView) findViewById(R.id.imgButtonHint);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");

		mBtnStartReading.setOnClickListener(this);
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
					mNumberOfCorrect = 0;
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

			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);

			mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
			mBtnDone.setEnabled(false);
			startCountTimer();
			break;

		case R.id.btnStartQuestion:
			Log.i(TAG, "btnStartQuestion");
			mIsStartQuestion = true;

			if (mIsStartReading) {
				mPassageTimerInput = (double) mElapsedTime / (1000 * 60);
			}

			if (!mIsStartReading) {
				startCountTimer();
			}
			mIsStartReading = false;

			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			mBtnStartQuestion.setEnabled(false);

			mBtnDone.setBackgroundResource(R.drawable.btn_done);
			mBtnDone.setEnabled(true);

			// startCountTimer();
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
			ScoreTimerApplication.sNeedToRefresh = true;
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

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH) {
			mBtnStartReading.setVisibility(View.GONE);
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartReading.setVisibility(View.VISIBLE);

			mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading);
			mBtnStartReading.setEnabled(true);

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
		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_ENGLISH) {
			scoreResult = actEnglish();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.ACT_ENGLISH);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_MATH) {
			scoreResult = actMath();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.ACT_MATH);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_READING) {
			scoreResult = actReading();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.ACT_READING);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.ACT_SCIENCE) {
			scoreResult = actScience();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.ACT_SCIENCE);
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

	private ScoreResult actEnglish() {
		Log.d(TAG, "actEnglish");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = ActEnglish.TOTAL_PASSAGES
				* ActEnglish.TIME
				/ (ActEnglish.TOTAL_PASSAGES * mPassageTimerInput + ActEnglish.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);
		if (scoreResult.pace > ActEnglish.TOTAL_PASSAGES) {
			scoreResult.pace = ActEnglish.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * ActEnglish.TOTAL_QUESTIONS
				/ ActEnglish.TOTAL_PASSAGES + (1 - scoreResult.pace / ActEnglish.TOTAL_PASSAGES)
				* ActEnglish.GUESSING_ACCURACY * ActEnglish.TOTAL_QUESTIONS;

		scoreResult.estimationScore = ActEnglish.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ ActEnglish.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < ActEnglish.MINIMUM_SCORE) {
			scoreResult.estimationScore = ActEnglish.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > ActEnglish.MAXIMUM_SCORE) {
			scoreResult.estimationScore = ActEnglish.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult actMath() {
		Log.d(TAG, "actMath");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;

		scoreResult.pace = ActMath.TOTAL_PASSAGES
				* ActMath.TIME
				/ (ActMath.TOTAL_PASSAGES * mPassageTimerInput + ActMath.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > ActMath.TOTAL_PASSAGES) {
			scoreResult.pace = ActMath.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * ActMath.TOTAL_QUESTIONS
				/ ActMath.TOTAL_PASSAGES + (1 - scoreResult.pace / ActMath.TOTAL_PASSAGES) * ActMath.GUESSING_ACCURACY
				* ActMath.TOTAL_QUESTIONS;

		scoreResult.estimationScore = ActMath.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ ActMath.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < ActMath.MINIMUM_SCORE) {
			scoreResult.estimationScore = ActMath.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > ActMath.MAXIMUM_SCORE) {
			scoreResult.estimationScore = ActMath.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult actReading() {
		Log.d(TAG, "actReading");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = ActReading.TOTAL_PASSAGES
				* ActReading.TIME
				/ (ActReading.TOTAL_PASSAGES * mPassageTimerInput + ActReading.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);
		if (scoreResult.pace > ActReading.TOTAL_PASSAGES) {
			scoreResult.pace = ActReading.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * ActReading.TOTAL_QUESTIONS
				/ ActReading.TOTAL_PASSAGES + (1 - scoreResult.pace / ActReading.TOTAL_PASSAGES)
				* ActReading.GUESSING_ACCURACY * ActReading.TOTAL_QUESTIONS;

		scoreResult.estimationScore = ActReading.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ ActReading.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < ActReading.MINIMUM_SCORE) {
			scoreResult.estimationScore = ActReading.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > ActReading.MAXIMUM_SCORE) {
			scoreResult.estimationScore = ActReading.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult actScience() {
		Log.d(TAG, "actScience");

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = ActScience.TOTAL_PASSAGES
				* ActScience.TIME
				/ (ActScience.TOTAL_PASSAGES * mPassageTimerInput + ActScience.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > ActScience.TOTAL_PASSAGES) {
			scoreResult.pace = ActScience.TOTAL_PASSAGES;
		}

		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * ActScience.TOTAL_QUESTIONS
				/ ActScience.TOTAL_PASSAGES + (1 - scoreResult.pace / ActScience.TOTAL_PASSAGES)
				* ActScience.GUESSING_ACCURACY * ActScience.TOTAL_QUESTIONS;

		scoreResult.estimationScore = ActScience.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ ActScience.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < ActScience.MINIMUM_SCORE) {
			scoreResult.estimationScore = ActScience.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > ActScience.MAXIMUM_SCORE) {
			scoreResult.estimationScore = ActScience.MAXIMUM_SCORE;
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
