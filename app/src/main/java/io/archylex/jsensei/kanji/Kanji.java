package io.archylex.jsensei.kanji;

import android.os.Parcel;
import android.os.Parcelable;

public class Kanji implements Parcelable {
    private String kanji;
    private String translate;
    private String on;
    private String kun;
    private int strokes;
    private String mClass;
    private String writing;
    private String examples;

    public Kanji() {}

    public Kanji(String kanji, String translate, String on, String kun, int strokes, String mClass, String writing, String examples) {
        this.kanji = kanji;
        this.translate = translate;
        this.on = on;
        this.kun = kun;
        this.strokes = strokes;
        this.mClass = mClass;
        this.writing = writing;
        this.examples = examples;
    }

    protected Kanji(Parcel in) {
        kanji = in.readString();
        translate = in.readString();
        on = in.readString();
        kun = in.readString();
        strokes = in.readInt();
        mClass = in.readString();
        writing = in.readString();
        examples = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kanji> CREATOR = new Creator<Kanji>() {
        @Override
        public Kanji createFromParcel(Parcel in) {
            return new Kanji(in);
        }

        @Override
        public Kanji[] newArray(int size) {
            return new Kanji[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kanji);
        dest.writeString(translate);
        dest.writeString(on);
        dest.writeString(kun);
        dest.writeInt(strokes);
        dest.writeString(mClass);
        dest.writeString(writing);
        dest.writeString(examples);
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getKun() {
        return kun;
    }

    public void setKun(String kun) {
        this.kun = kun;
    }

    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes = strokes;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }
}
