package io.archylex.jsensei.quiz;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {}

    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "jlpt";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER_A = "A";
        public static final String COLUMN_ANSWER_B = "B";
        public static final String COLUMN_ANSWER_C = "C";
        public static final String COLUMN_ANSWER_D = "D";
        public static final String COLUMN_ANSWER_NUM = "answer";
        public static final String COLUMN_LEVEL = "level";
    }
}
