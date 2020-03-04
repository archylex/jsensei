package io.archylex.jsensei.quiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.archylex.jsensei.R;
import io.archylex.jsensei.ui.quiz.QuizFragment;

public class FinishQuiz extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HIGHSCORE[] = {"highscore_n1", "highscore_n2", "highscore_n3", "highscore_n4", "highscore_n5"};
    Button finish_button;
    private SharedPreferences sharedPreferences;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        finish_button = findViewById(R.id.finishButton);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Intent intent = getIntent();
        int score = intent.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
        int numquest = intent.getIntExtra(QuizActivity.NUM_QUESTS, 1);
        String str_level = intent.getStringExtra(QuizFragment.LEVEL).replaceAll("\\D+","");
        level = Integer.parseInt(str_level);
        int highscore = sharedPreferences.getInt(HIGHSCORE[level - 1], 0);

        int percent_result = score * 100 / numquest;

        TextView tvPercentResult = findViewById(R.id.percentResultText);
        TextView tvSuccess = findViewById(R.id.successText);
        TextView tvWrong = findViewById(R.id.wrongText);
        TextView tvHighscore = findViewById(R.id.highscoreText);
        TextView tvFinish = findViewById(R.id.finishText);

        tvPercentResult.setText(Integer.toString(percent_result) + "%");
        tvSuccess.setText(Integer.toString(score));
        tvWrong.setText(Integer.toString(numquest - score));

        if (percent_result > 70) {
            if (percent_result > highscore)
                tvFinish.setText("Поздравляем!\nВы еще лучще справились с заданием N" + Integer.toString(level) + ". Не забудьте опробовать свои силы на экзамене.");
            else
                tvFinish.setText("Поздравляем!\nВы справились с заданием N" + Integer.toString(level) + ". Теперь попробуйте сдать государственный экзамен.");
        } else if (percent_result > 50) {
            if (percent_result > highscore)
                tvFinish.setText("Получше, но чуть-чуть не дотянули на  N" + Integer.toString(level) + ". Подготовьтесь как следует.");
            else
                tvFinish.setText("Неплохо, но чуть-чуть не дотянули на N" + Integer.toString(level) + ". Подготовьтесь получше.");
        } else {
            if (percent_result > highscore)
                tvFinish.setText("Получше, но увы, не справились с заданием N" + Integer.toString(level) + ". Будьте внимательнее.");
            else
                tvFinish.setText("Увы, но вы с заданием N" + Integer.toString(level) + " не справились. Повторение - мать учения.");
        }

        if (percent_result > highscore)
            updateHighscore(percent_result);

        tvHighscore.setText(Integer.toString(highscore) + "%");
    }

    private void updateHighscore(int new_hscore) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putInt(HIGHSCORE[level - 1], new_hscore);
        spEditor.apply();
    }
}
