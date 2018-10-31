package com.example.caro.helpdeskapp.mvp.view;

import android.support.annotation.NonNull;

public interface IBaseView {

    interface State {}

    void render(@NonNull State state);

}