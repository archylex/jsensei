package io.archylex.jsensei.ui.lessons;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.archylex.jsensei.DatabaseHelper;
import io.archylex.jsensei.R;
import io.archylex.jsensei.lessons.ReaderActivity;

public class LessonsFragment extends Fragment {
    public static final String _LESSON = "_lesson";
    public static final String _THEME = "_theme";
    private ListView lessons_list_view;
    private ListView themes_list_view;
    private boolean isItemsList;
    private String selected_lesson;
    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lessons, container, false);

        lessons_list_view = root.findViewById(R.id.lessons_list_view);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this.getActivity());
        dbHelper.openDataBase();
        String[] lessons = dbHelper.getLessons();
        dbHelper.closeDataBase();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>( this.getContext(), android.R.layout.simple_list_item_1, lessons );
        lessons_list_view.setAdapter(myAdapter);

        lessons_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                goToThemesList(item);
            }
        });

        themes_list_view = root.findViewById(R.id.lesson_items_list_view);
        themes_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                getReader(item);
            }
        });

        goToLessonsList();

        return root;
    }

    private void getReader(String theme) {
        Intent intent = new Intent(this.getActivity(), ReaderActivity.class);
        intent.putExtra(_LESSON, selected_lesson);
        intent.putExtra(_THEME, theme);
        startActivity(intent);
    }

    private void goToThemesList(String lesson) {
        lessons_list_view.setEnabled(false);
        lessons_list_view.setVisibility(View.GONE);
        themes_list_view.setEnabled(true);
        themes_list_view.setVisibility(View.VISIBLE);
        isItemsList = true;

        selected_lesson = lesson;

        dbHelper = DatabaseHelper.getInstance(this.getActivity());
        dbHelper.openDataBase();
        String[] themes = dbHelper.getThemesByLesson(lesson);
        dbHelper.closeDataBase();

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, themes);
        themes_list_view.setAdapter(itemsAdapter);
    }

    private void goToLessonsList() {
        themes_list_view.setEnabled(false);
        themes_list_view.setVisibility(View.GONE);
        lessons_list_view.setEnabled(true);
        lessons_list_view.setVisibility(View.VISIBLE);
        isItemsList = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null)
            return;

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && isItemsList){
                    goToLessonsList();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null)
            dbHelper.close();
    }
}