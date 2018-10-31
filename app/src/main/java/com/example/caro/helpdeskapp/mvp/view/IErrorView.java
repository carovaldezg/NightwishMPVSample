package com.example.caro.helpdeskapp.mvp.view;

public interface IErrorView extends IBaseView {

    /**
     * Used to render unknown errors.
     */
    class UnknownErrorState implements State {}

    /**
     * Used to render no internet errors.
     */
    class NoInternetErrorState implements State {}

    /**
     * Used to render 'connect your device' state.
     */
    class OfflineDeviceErrorState implements State {}

    class ValidateSignInError implements State {}

}