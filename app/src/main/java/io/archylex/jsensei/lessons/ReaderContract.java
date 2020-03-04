package io.archylex.jsensei.lessons;

import android.provider.BaseColumns;

public final class ReaderContract {
    private ReaderContract() {}

    public static class LessonTable implements BaseColumns {
        public static final String TABLE_NAME = "lessons";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_THEME = "theme";
        public static final String COLUMN_LESSON = "lesson";
    }
}
