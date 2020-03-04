package io.archylex.jsensei.ui.kanji;

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
import io.archylex.jsensei.kanji.ViewerActivity;

public class KanjiFragment extends Fragment {
    public static final String KANJI = "kanji_view";
    private ListView lvClass;
    private ListView lvKanji;
    private boolean isKanjiList;
    private DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kanji, container, false);

        lvClass = root.findViewById(R.id.class_list_view);
        dbHelper = DatabaseHelper.getInstance(this.getActivity());
        dbHelper.openDataBase();
        String[] classes = dbHelper.getClassesKanji();
        dbHelper.closeDataBase();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, classes);
        lvClass.setAdapter(myAdapter);

        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                goToKanjiList(item);
            }
        });

        lvKanji = root.findViewById(R.id.kanji_list_view);
        String[] kanji = {};

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, kanji);
        lvKanji.setAdapter(itemsAdapter);

        lvKanji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mClass = parent.getItemAtPosition(position).toString();
                getViwer(mClass);
            }
        });

        goToClassList();

        return root;
    }

    private void getViwer(String item) {
        Intent intent = new Intent(this.getActivity(), ViewerActivity.class);
        intent.putExtra(KANJI, item);
        startActivity(intent);
    }

    private void goToKanjiList(String mClass) {
        lvClass.setEnabled(false);
        lvClass.setVisibility(View.GONE);
        lvKanji.setEnabled(true);
        lvKanji.setVisibility(View.VISIBLE);
        isKanjiList = true;

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this.getActivity());
        dbHelper.openDataBase();
        String[] kanjis = dbHelper.getKanjisByClass(mClass);
        dbHelper.closeDataBase();

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, kanjis);
        lvKanji.setAdapter(itemsAdapter);
    }

    private void goToClassList() {
        lvKanji.setEnabled(false);
        lvKanji.setVisibility(View.GONE);
        lvClass.setEnabled(true);
        lvClass.setVisibility(View.VISIBLE);
        isKanjiList = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && isKanjiList){
                    goToClassList();
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