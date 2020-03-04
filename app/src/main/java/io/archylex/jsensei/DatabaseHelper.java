package io.archylex.jsensei;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.archylex.jsensei.kanji.Kanji;
import io.archylex.jsensei.kanji.KanjiContract.*;
import io.archylex.jsensei.quiz.Question;
import io.archylex.jsensei.quiz.QuizContract.*;
import io.archylex.jsensei.lessons.LessonTheme;
import io.archylex.jsensei.lessons.ReaderContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "data.db";
    public final static String DB_PATH_SUFFIX = "/databases/";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private static Context context;

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());

        return instance;
    }

    private void CopyDataBaseFromAsset() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        String outFilename = getDatabasePath();
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);

        if (!f.exists())
            f.mkdir();

        OutputStream output = new FileOutputStream(outFilename);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    private static String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void openDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (dbFile.exists()) {
            onUpgrade(db, 0, 1);
        } else {
            try {
                CopyDataBaseFromAsset();
            } catch (IOException e) {
                throw new RuntimeException("Can't create database", e);
            }
        }

        db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public synchronized void closeDataBase()throws SQLException {
        if(db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            File file = new File(getDatabasePath());

            if(file.exists())
                file.delete();

            try {
                CopyDataBaseFromAsset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getLevels() {
        List<String> result = new ArrayList<String>();
        String[] args = null;
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT " + QuestionTable.COLUMN_LEVEL + " FROM " + QuestionTable.TABLE_NAME + " GROUP BY "  + QuestionTable.COLUMN_LEVEL + " HAVING COUNT(*)>0", args);

        if (cur.moveToFirst()) {
            do {
                result.add(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_LEVEL)));
            } while (cur.moveToNext());
        }

        cur.close();

        return result;
    }

    public ArrayList<Question> getQuestions(String level) {
        ArrayList<Question> qList = new ArrayList<>();
        db = getReadableDatabase();
        String[] args = new String[]{level};
        Cursor cur = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME + " WHERE " + QuestionTable.COLUMN_LEVEL + "=?", args);

        if (cur.moveToFirst()) {
            do {
                Question q = new Question();
                q.setQuestion(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                q.setAnswerA(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_ANSWER_A)));
                q.setAnswerB(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_ANSWER_B)));
                q.setAnswerC(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_ANSWER_C)));
                q.setAnswerD(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_ANSWER_D)));
                q.setAnswerNum(cur.getInt(cur.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                q.setLevel(cur.getString(cur.getColumnIndex(QuestionTable.COLUMN_LEVEL)));
                qList.add(q);
            } while (cur.moveToNext());
        }

        cur.close();

        return qList;
    }

    public String[] getClassesKanji() {
        String[] result = new String[0];
        List<String> strResult = new ArrayList<>(Arrays.asList(result));
        String[] args = null;
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT " + KanjiTable.COLUMN_CLASS + " FROM " + KanjiTable.TABLE_NAME + " GROUP BY "  + KanjiTable.COLUMN_CLASS + " HAVING COUNT(*)>0", args);

        if (cur.moveToFirst()) {
            do {
                strResult.add(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_CLASS)));
            } while (cur.moveToNext());
        }

        cur.close();

        result = strResult.toArray(result);

        return result;
    }

    public String[] getKanjisByClass(String mClass) {
        String[] result = new String[0];
        List<String> strResult = new ArrayList<>(Arrays.asList(result));
        String[] args = {mClass};
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT " + KanjiTable.COLUMN_KANJI + " FROM " + KanjiTable.TABLE_NAME + " WHERE " + KanjiTable.COLUMN_CLASS + "=?", args);

        if (cur.moveToFirst()) {
            do {
                strResult.add(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_KANJI)));
            } while (cur.moveToNext());
        }

        cur.close();

        result = strResult.toArray(result);

        return result;
    }

    public Kanji getKanji(String kanji) {
        Kanji k = new Kanji();
        db = getReadableDatabase();
        String[] args = new String[]{kanji};
        Cursor cur = db.rawQuery("SELECT * FROM " + KanjiTable.TABLE_NAME + " WHERE " + KanjiTable.COLUMN_KANJI + "=?", args);

        if (cur.moveToFirst()) {
            do {
                k.setKanji(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_KANJI)));
                k.setTranslate(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_TRANSLATE)));
                k.setOn(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_ON)));
                k.setKun(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_KUN)));
                k.setWriting(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_WRITING)));
                k.setExamples(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_EXAMPLES)));
                k.setStrokes(cur.getInt(cur.getColumnIndex(KanjiTable.COLUMN_STROKES)));
                k.setmClass(cur.getString(cur.getColumnIndex(KanjiTable.COLUMN_CLASS)));
            } while (cur.moveToNext());
        }

        cur.close();

        return k;
    }

    public String[] getLessons() {
        String[] result = new String[0];
        List<String> strResult = new ArrayList<>(Arrays.asList(result));
        String[] args = null;
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT " + LessonTable.COLUMN_LESSON + " FROM " + LessonTable.TABLE_NAME + " GROUP BY "  + LessonTable.COLUMN_LESSON + " HAVING COUNT(*)>0", args);

        if (cur.moveToFirst()) {
            do {
                strResult.add(cur.getString(cur.getColumnIndex(LessonTable.COLUMN_LESSON)));
            } while (cur.moveToNext());
        }

        cur.close();

        result = strResult.toArray(result);

        return result;
    }

    public String[] getThemesByLesson(String lesson) {
        String[] result = new String[0];
        List<String> strResult = new ArrayList<>(Arrays.asList(result));
        String[] args = {lesson};

        db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT " + LessonTable.COLUMN_THEME + " FROM " + LessonTable.TABLE_NAME + " WHERE " + LessonTable.COLUMN_LESSON + "=?" , args);

        if (cur.moveToFirst()) {
            do {
                strResult.add(cur.getString(cur.getColumnIndex(LessonTable.COLUMN_THEME)));
            } while (cur.moveToNext());
        }

        cur.close();

        result = strResult.toArray(result);

        return result;
    }

    public LessonTheme getTheme(String theme, String lesson) {
        LessonTheme lessonTheme = new LessonTheme();
        db = getReadableDatabase();
        String[] args = new String[]{theme, lesson};
        Cursor cur = db.rawQuery("SELECT * FROM " + LessonTable.TABLE_NAME + " WHERE " + LessonTable.COLUMN_THEME + "=? AND " + LessonTable.COLUMN_LESSON + "=?", args);

        if (cur.moveToFirst()) {
            do {
                lessonTheme.setLesson(cur.getString(cur.getColumnIndex(LessonTable.COLUMN_LESSON)));
                lessonTheme.setTheme(cur.getString(cur.getColumnIndex(LessonTable.COLUMN_THEME)));
                lessonTheme.setText(cur.getString(cur.getColumnIndex(LessonTable.COLUMN_TEXT)));
            } while (cur.moveToNext());
        }

        cur.close();

        return lessonTheme;
    }

    @Override
    public synchronized void close () {
        if (db != null) {
            db.close();
            super.close();
        }
    }
}