package io.archylex.jsensei.lessons;

import android.os.Parcel;
import android.os.Parcelable;

public class LessonTheme implements Parcelable {
    private String theme;
    private String lesson;
    private String text;

    public LessonTheme() {}

    public LessonTheme(String text, String theme, String lesson) {
        this.theme = theme;
        this.lesson = lesson;
        this.theme = theme;
    }

    protected LessonTheme(Parcel in) {
        theme = in.readString();
        lesson = in.readString();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(theme);
        dest.writeString(lesson);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LessonTheme> CREATOR = new Creator<LessonTheme>() {
        @Override
        public LessonTheme createFromParcel(Parcel in) {
            return new LessonTheme(in);
        }

        @Override
        public LessonTheme[] newArray(int size) {
            return new LessonTheme[size];
        }
    };

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
