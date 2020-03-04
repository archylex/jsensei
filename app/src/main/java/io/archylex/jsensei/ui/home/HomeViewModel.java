package io.archylex.jsensei.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("<br>" +
                "<b>ようこそ。</b><br>" +
                "日本語を話し始めましょう。<br>" +
                "<br>" +
                "<b>Добро пожаловать!</b><br>" +
                "Давайте говорить по-японски.<br>" +
                "<br>" +
                "Данное приложение поможет изучить грамматику японского языка, иероглифы, а также протестировать свои знания.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}