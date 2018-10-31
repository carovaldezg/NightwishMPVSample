package com.example.caro.MVPSample.mvp.view;

import android.support.annotation.NonNull;

public interface IBaseView {

    interface State {}

    void render(@NonNull State state);

}