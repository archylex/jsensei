package io.archylex.jsensei.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.List;
import io.archylex.jsensei.DatabaseHelper;
import io.archylex.jsensei.R;
import io.archylex.jsensei.quiz.FinishQuiz;
import io.archylex.jsensei.quiz.QuizActivity;
import static android.app.Activity.RESULT_OK;

public class QuizFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CODE = 1;
    public static final String LEVEL = "level";
    private Spinner spinner;
    private String level;
    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        spinner = (Spinner) root.findViewById(R.id.spinner_level);
        spinner.setOnItemSelectedListener(this);

        dbHelper = DatabaseHelper.getInstance(this.getActivity());
        dbHelper.openDataBase();
        List<String> levels = dbHelper.getLevels();
        dbHelper.closeDataBase();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, levels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button start_btn = (Button)  root.findViewById(R.id.startQuiz);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void startQuiz() {
        level = spinner.getSelectedItem().toString();
        Intent intent = new Intent(this.getActivity(), QuizActivity.class);
        intent.putExtra(LEVEL, level);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                int numquests = data.getIntExtra(QuizActivity.NUM_QUESTS, 1);

                Intent intent = new Intent(this.getActivity(), FinishQuiz.class);
                intent.putExtra(QuizActivity.EXTRA_SCORE, score);
                intent.putExtra(QuizActivity.NUM_QUESTS, numquests);
                intent.putExtra(LEVEL, level);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null)
            dbHelper.close();
    }
}