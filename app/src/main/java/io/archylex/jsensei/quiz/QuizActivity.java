package io.archylex.jsensei.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import io.archylex.jsensei.DatabaseHelper;
import io.archylex.jsensei.R;
import io.archylex.jsensei.ui.quiz.QuizFragment;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String NUM_QUESTS = "numQuests";
    private static final String SCORE = "nowScore";
    private static final String QUESTION_NUM = "nowQuestionNum";
    private static final String TIME = "nowTime";
    private static final String ANSWERED = "nowAnswered";
    private static final String QUESTION_LIST = "nowQList";
    private static final long MILLIS = 50000;
    private TextView tvScore;
    private TextView tvQuestionNum;
    private TextView tvTimer;
    private TextView tvQuestion;
    private TextView tvLevel;
    private RadioGroup radioGroup;
    private RadioButton answer_a;
    private RadioButton answer_b;
    private RadioButton answer_c;
    private RadioButton answer_d;
    private Button next_button;
    private ArrayList<Question> questionList;
    private ColorStateList answersColor;
    private ColorStateList timerColor;
    private int questionCounter;
    private int questionTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private CountDownTimer cdTimer;
    private long millisTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvScore = findViewById(R.id.text_view_score);
        tvQuestionNum = findViewById(R.id.text_view_question_number);
        tvTimer = findViewById(R.id.text_view_timer);
        tvLevel = findViewById(R.id.text_view_level);
        tvQuestion = findViewById(R.id.text_view_question);
        answer_a = findViewById(R.id.radio_button_a);
        answer_b = findViewById(R.id.radio_button_b);
        answer_c = findViewById(R.id.radio_button_c);
        answer_d = findViewById(R.id.radio_button_d);
        next_button = findViewById(R.id.button_next);
        radioGroup = findViewById(R.id.radio_group);

        answersColor = answer_a.getTextColors();
        timerColor = tvTimer.getTextColors();

        Intent intent = getIntent();
        String level = intent.getStringExtra(QuizFragment.LEVEL);
        tvLevel.setText("レベル: " + level);

        if (savedInstanceState == null) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            dbHelper.openDataBase();
            questionList = dbHelper.getQuestions(level);
            dbHelper.closeDataBase();
            questionTotal = (questionList.size() < 20) ? questionList.size() : 20;
            Collections.shuffle(questionList);
            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(QUESTION_LIST);

            if (questionList == null)
                finish();

            questionTotal = (questionList.size() < 20) ? questionList.size() : 20;
            questionCounter = savedInstanceState.getInt(QUESTION_NUM);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(SCORE);
            millisTime = savedInstanceState.getLong(TIME);
            answered = savedInstanceState.getBoolean(ANSWERED);

            if (!answered) {
                startTimer();
            } else {
                updateTVTimer();
                showSolution();
            }
        }

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (answer_a.isChecked() || answer_b.isChecked() || answer_c.isChecked() || answer_d.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Выберите ответ!", Toast.LENGTH_LONG);
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        answer_a.setTextColor(answersColor);
        answer_b.setTextColor(answersColor);
        answer_c.setTextColor(answersColor);
        answer_d.setTextColor(answersColor);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            answer_a.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_b.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_c.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_d.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
        } else {
            answer_a.setBackground(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_b.setBackground(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_c.setBackground(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
            answer_d.setBackground(ContextCompat.getDrawable(this, R.drawable.round_rectangle));
        }

        radioGroup.clearCheck();

        if (questionCounter < questionTotal) {
            currentQuestion = questionList.get(questionCounter);
            tvQuestion.setText(currentQuestion.getQuestion());
            answer_a.setText(currentQuestion.getAnswerA());
            answer_b.setText(currentQuestion.getAnswerB());
            answer_c.setText(currentQuestion.getAnswerC());
            answer_d.setText(currentQuestion.getAnswerD());

            questionCounter++;

            tvQuestionNum.setText("質問: " + questionCounter + "/" + questionTotal);
            answered = false;
            next_button.setText("確認");

            millisTime = MILLIS;

            startTimer();
        } else {
            finishQuiz();
        }
    }

    private void startTimer() {
        cdTimer = new CountDownTimer(millisTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisTime = millisUntilFinished;
                updateTVTimer();
            }

            @Override
            public void onFinish() {
                millisTime = 0;
                updateTVTimer();
                checkAnswer();
            }
        }.start();
    }

    private void updateTVTimer() {
        int sec = (int) (millisTime / 1000 % 60);
        tvTimer.setText(Integer.toString(sec));

        if (millisTime < 15000)
            tvTimer.setTextColor(Color.RED);
        else
            tvTimer.setTextColor(timerColor);
    }

    private void finishQuiz() {
        Intent result = new Intent();
        result.putExtra(EXTRA_SCORE, score);
        result.putExtra(NUM_QUESTS, questionTotal);
        setResult(RESULT_OK, result);
        finish();
    }

    private void checkAnswer() {
        answered = true;
        cdTimer.cancel();
        RadioButton selected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answer = radioGroup.indexOfChild(selected) + 1;

        if (answer == currentQuestion.getAnswerNum()) {
            score++;
            tvScore.setText("正解: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        answer_a.setTextColor(Color.WHITE);
        answer_b.setTextColor(Color.WHITE);
        answer_c.setTextColor(Color.WHITE);
        answer_d.setTextColor(Color.WHITE);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            answer_a.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_b.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_c.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_d.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
        } else {
            answer_a.setBackground(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_b.setBackground(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_c.setBackground(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
            answer_d.setBackground(ContextCompat.getDrawable(this, R.drawable.red_round_rectangle));
        }


        switch (currentQuestion.getAnswerNum()) {
            case 1:
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
                    answer_a.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                else
                    answer_a.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                break;
            case 2:
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
                    answer_b.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                else
                    answer_b.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                break;
            case 3:
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
                    answer_c.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                else
                    answer_c.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                break;
            case 4:
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
                    answer_d.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                else
                    answer_d.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_round_rectangle));
                break;
        }

        if (questionCounter < questionTotal)
            next_button.setText("次へ");
        else
            next_button.setText("終了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cdTimer != null)
            cdTimer.cancel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(SCORE, score);
        outState.putInt(QUESTION_NUM, questionCounter);
        outState.putLong(TIME, millisTime);
        outState.putBoolean(ANSWERED, answered);
        outState.putParcelableArrayList(QUESTION_LIST, questionList);
    }
}
