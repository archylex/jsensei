package io.archylex.jsensei.lessons;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import io.archylex.jsensei.DatabaseHelper;
import io.archylex.jsensei.R;
import io.archylex.jsensei.ui.lessons.LessonsFragment;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        getSupportActionBar().hide();

        TextView tvContent = findViewById(R.id.content_lesson);
        TextView tvTitle = findViewById(R.id.title_lesson);

        Intent intent = getIntent();
        String theme = intent.getStringExtra(LessonsFragment._THEME);
        String lesson = intent.getStringExtra(LessonsFragment._LESSON);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.openDataBase();
        LessonTheme lessonTheme = dbHelper.getTheme(theme, lesson);
        dbHelper.closeDataBase();

        tvTitle.setText(lessonTheme.getTheme());

        tvContent.setMovementMethod(new ScrollingMovementMethod());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvContent.setText(Html.fromHtml(lessonTheme.getText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvContent.setText(Html.fromHtml(lessonTheme.getText()));
        }
    }
}
