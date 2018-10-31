package com.example.caro.helpdeskapp.mvp.view;


/**
 * This view defines the states to provide feedback to the user when loading data.
 */
public interface ILoadingView extends IBaseView {

    class ShowLoadingState implements State {}

    class HideLoadingState implements State {}

}