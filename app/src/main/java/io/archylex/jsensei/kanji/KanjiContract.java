package io.archylex.jsensei.kanji;

import android.provider.BaseColumns;

public final class KanjiContract {
    private KanjiContract() {}

    public static class KanjiTable implements BaseColumns {
        public static final String TABLE_NAME = "kanji";
        public static final String COLUMN_KANJI = "kanji";
        public static final String COLUMN_TRANSLATE = "translate";
        public static final String COLUMN_ON = "on";
        public static final String COLUMN_KUN = "kun";
        public static final String COLUMN_STROKES = "strokes";
        public static final String COLUMN_WRITING = "writing";
        public static final String COLUMN_EXAMPLES = "examples";
        public static final String COLUMN_CLASS = "class";
    }
}
