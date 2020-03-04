package io.archylex.jsensei.kanji;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import io.archylex.jsensei.DatabaseHelper;
import io.archylex.jsensei.R;
import io.archylex.jsensei.ui.kanji.KanjiFragment;

public class ViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        ImageView kanjiPic = findViewById(R.id.kanjiImageView);
        TextView kanji_main = findViewById(R.id.kanji_main);
        TextView translate = findViewById(R.id.kanji_translate);
        TextView kanji_on = findViewById(R.id.kanji_on);
        TextView kanji_kun = findViewById(R.id.kanji_kun);
        TextView kanji_strokes = findViewById(R.id.kanji_strokes);
        TextView kanji_examples = findViewById(R.id.kanji_examples);

        Intent intent = getIntent();
        String kanji = intent.getStringExtra(KanjiFragment.KANJI);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.openDataBase();
        Kanji cKanji = dbHelper.getKanji(kanji);
        dbHelper.closeDataBase();

        kanji_main.setText(cKanji.getKanji());
        translate.setText(cKanji.getTranslate());
        kanji_on.setText(cKanji.getOn());
        kanji_kun.setText(cKanji.getKun());
        kanji_strokes.setText(Integer.toString(cKanji.getStrokes()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            kanji_examples.setText(Html.fromHtml(cKanji.getExamples(), Html.FROM_HTML_MODE_COMPACT));
        else
            kanji_examples.setText(Html.fromHtml(cKanji.getExamples()));

        final String pureBase64Encoded = cKanji.getWriting().substring(cKanji.getWriting().indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

        Glide.with(this).load(decodedBytes).into(kanjiPic);
    }
}
