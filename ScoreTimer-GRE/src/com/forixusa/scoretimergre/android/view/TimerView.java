package com.forixusa.scoretimergre.android.view;

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
import com.forixusa.scoretimergre.android.ScoreTimerApplication;
import com.forixusa.scoretimergre.android.activities.R;
import com.forixusa.scoretimergre.android.adapter.SpinnerAdapter;
import com.forixusa.scoretimergre.android.models.GreMath;
import com.forixusa.scoretimergre.android.models.GreVerbal;
import com.forixusa.scoretimergre.android.models.ScoreResult;

public class TimerView extends ContentView implements OnClickListener {
	private static final String TAG = TimerView.class.getSimpleName();

	private static final String TIMER_FORMAT = "%02d:%02d:%02d";

	private Button mBtnTestOptions;
	// private Button mBtnStartReading;
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

	// private boolean mIsStartReading;
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

		// Log.i(TAG, "mIsStartReading:" + mIsStartReading);
		// if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.LOGICAL_REASONING && !mIsStartQuestion) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
		// mBtnStartQuestion.setEnabled(true);
		// } else {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
		// mBtnStartQuestion.setEnabled(false);
		// }

		if (!mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question_in);
			mBtnStartQuestion.setEnabled(false);
		}
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResume");

		initTitle();
		// Log.i(TAG, "mIsStartReading:" + mIsStartReading);
		// if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.LOGICAL_REASONING && !mIsStartQuestion) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
		// mBtnStartQuestion.setEnabled(true);
		// } else {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
		// mBtnStartQuestion.setEnabled(false);
		// }
		//
		// if (ScoreTimerApplication.sTimerTestOption !=
		// ScoreTimerApplication.LOGICAL_REASONING && mIsStartReading) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
		// mBtnStartQuestion.setEnabled(true);
		// }

		if (!mIsStartQuestion) {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
			mBtnStartQuestion.setEnabled(true);
		} else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question_in);
			mBtnStartQuestion.setEnabled(false);
		}

		if (ScoreTimerApplication.sNeedToRefresh) {
			resetFields();
			ScoreTimerApplication.sNeedToRefresh = false;
		}
	}

	private void initTitle() {
		Log.i(TAG, "initTitle");
		// Log.i(TAG, "ScoreTimerApplication.sTimerTestOption:" +
		// ScoreTimerApplication.sTimerTestOption);

		// mBtnStartReading.setVisibility(View.VISIBLE);

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.GRE_MATH) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt_test1);
			// mBtnStartReading.setVisibility(View.GONE);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.GRE_VERBAL) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt_test2);
		}
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		// mBtnStartReading = (Button) findViewById(R.id.btnStartReading);

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
		// mBtnStartReading.setOnClickListener(this);
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

		// case R.id.btnStartReading:
		// Log.i(TAG, "btnStartReading");
		// mIsStartReading = true;
		//
		// //
		// mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
		// // mBtnStartReading.setEnabled(false);
		//
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question);
		// mBtnStartQuestion.setEnabled(true);
		//
		// mBtnDone.setBackgroundResource(R.drawable.btn_done_in);
		// mBtnDone.setEnabled(false);
		// startCountTimer();
		// break;
		case R.id.btnStartQuestion:
			Log.i(TAG, "btnStartQuestion");
			mIsStartQuestion = true;
			// Log.i(TAG, "mIsStartReading:" + mIsStartReading);

			// if (mIsStartReading) {
			// mPassageTimerInput = (double) mElapsedTime / (1000 * 60);
			// }

			// if (!mIsStartReading) {
			// startCountTimer();
			// }

			// mIsStartReading = false;
			// mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			// mBtnStartReading.setEnabled(false);

			startCountTimer();

			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question_in);
			mBtnStartQuestion.setEnabled(false);

			mBtnDone.setBackgroundResource(R.drawable.btn_done);
			mBtnDone.setEnabled(true);

			// startCountTimer();
			break;
		case R.id.btnDone:
			mChrCountTimer.stop();
			// mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading_in);
			// mBtnStartReading.setEnabled(false);

			// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
			// mBtnStartQuestion.setEnabled(false);
			// if(sTestOption == TestOptionView.BIOLOGICAL_SCIENCES) {
			// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
			// mBtnStartQuestion.setEnabled(true);
			// } else {
			mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_question_in);
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

			// ActionItem timertItem = new ActionItem(1,
			// getResources().getString(R.string.hint_1), null);

			quickAction1.addActionItem(timertItem);

			quickAction1.show(v);
			closeToolTip(quickAction1);

			break;
		case R.id.imgQuestionHint:
			final QuickAction quickAction2 = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem questionItem = new ActionItem(1,
					"Grade your results, then ENTER the number of questions you did and the number you got correct.",
					null);

			// ActionItem questionItem = new ActionItem(1,
			// getResources().getString(R.string.hint_2), null);

			quickAction2.addActionItem(questionItem);

			quickAction2.show(v);
			closeToolTip(quickAction2);
			break;
		case R.id.imgButtonHint:
			final QuickAction quickAction3 = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem buttonItem = new ActionItem(1, "Hit GET RESULTS to get your summary.          ", null);

			// ActionItem buttonItem = new ActionItem(1,
			// getResources().getString(R.string.hint_3), null);

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

		mIsStartQuestion = false;
		// mBtnStartReading.setBackgroundResource(R.drawable.btn_start_reading);
		// mBtnStartReading.setEnabled(true);

		// if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.LOGICAL_REASONING) {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions);
		// mBtnStartQuestion.setEnabled(true);
		// } else {
		// mBtnStartQuestion.setBackgroundResource(R.drawable.btn_start_questions_in);
		// mBtnStartQuestion.setEnabled(false);
		// }

		mChrCountTimer.stop();

		// mIsStartReading = false;
		// mIsStartQuestion = false;
	}

	private void getResult() {
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
		// if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.BIOLOGICAL_SCIENCES) {
		// scoreResult = biologicalSciencesProcess();
		// scoreResult.testOption =
		// String.valueOf(ScoreTimerApplication.BIOLOGICAL_SCIENCES);
		// } else if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.VERBAL_REASONING) {
		// scoreResult = verbalReasoningProcess();
		// scoreResult.testOption =
		// String.valueOf(ScoreTimerApplication.VERBAL_REASONING);
		// } else if (ScoreTimerApplication.sTimerTestOption ==
		// ScoreTimerApplication.PHYSICAL_SCIENCES) {
		// scoreResult = physicalSciencesProcess();
		// scoreResult.testOption =
		// String.valueOf(ScoreTimerApplication.PHYSICAL_SCIENCES);
		// }

		if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.GRE_MATH) {
			scoreResult = greMathProcess();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.GRE_MATH);
		} else if (ScoreTimerApplication.sTimerTestOption == ScoreTimerApplication.GRE_VERBAL) {
			scoreResult = greVerbalProcess();
			scoreResult.testOption = String.valueOf(ScoreTimerApplication.GRE_VERBAL);
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

	private ScoreResult greMathProcess() {
		Log.d(TAG, "greMathProcess");
		// mPassageTimerInput = 0;
		// mQuestionTimerInput = 15;
		// mQuestionCompleted = 6;
		// mNumberOfCorrect = 5;

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;

		scoreResult.pace = GreMath.TOTAL_PASSAGES
				* GreMath.TIME
				/ (GreMath.TOTAL_PASSAGES * mPassageTimerInput + GreMath.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > GreMath.TOTAL_PASSAGES) {
			scoreResult.pace = GreMath.TOTAL_PASSAGES;
		}
		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * GreMath.TOTAL_QUESTIONS
				/ GreMath.TOTAL_PASSAGES + (1 - scoreResult.pace / GreMath.TOTAL_PASSAGES) * GreMath.GUESSING_ACCURACY
				* GreMath.TOTAL_QUESTIONS;

		scoreResult.estimationScore = GreMath.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ GreMath.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < GreMath.MINIMUM_SCORE) {
			scoreResult.estimationScore = GreMath.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > GreMath.MAXIMUM_SCORE) {
			scoreResult.estimationScore = GreMath.MAXIMUM_SCORE;
		}

		return scoreResult;
	}

	private ScoreResult greVerbalProcess() {
		Log.d(TAG, "greVerbalProcess");
		// mPassageTimerInput = 4.5;
		// mQuestionTimerInput = 5;
		// mQuestionCompleted = 6;
		// mNumberOfCorrect = 5;

		final ScoreResult scoreResult = new ScoreResult();

		scoreResult.accuracy = (double) mNumberOfCorrect / mQuestionCompleted;
		scoreResult.pace = GreVerbal.TOTAL_PASSAGES
				* GreVerbal.TIME
				/ (GreVerbal.TOTAL_PASSAGES * mPassageTimerInput + GreVerbal.TOTAL_QUESTIONS * mQuestionTimerInput
						/ mQuestionCompleted);

		if (scoreResult.pace > GreVerbal.TOTAL_PASSAGES) {
			scoreResult.pace = GreVerbal.TOTAL_PASSAGES;
		}

		scoreResult.estimationCorrect = scoreResult.pace * scoreResult.accuracy * GreVerbal.TOTAL_QUESTIONS
				/ GreVerbal.TOTAL_PASSAGES + (1 - scoreResult.pace / GreVerbal.TOTAL_PASSAGES)
				* GreVerbal.GUESSING_ACCURACY * GreVerbal.TOTAL_QUESTIONS;

		scoreResult.estimationScore = GreVerbal.FORMULA_CONSTANT1 * scoreResult.estimationCorrect
				+ GreVerbal.FORMULA_CONSTANT2;

		if (scoreResult.estimationScore < GreVerbal.MINIMUM_SCORE) {
			scoreResult.estimationScore = GreVerbal.MINIMUM_SCORE;
		} else if (scoreResult.estimationScore > GreVerbal.MAXIMUM_SCORE) {
			scoreResult.estimationScore = GreVerbal.MAXIMUM_SCORE;
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
