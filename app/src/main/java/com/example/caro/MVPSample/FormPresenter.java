package com.example.caro.MVPSample;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class FormPresenter {

    private final MainView mViewInstance;
    private Pattern mEmailPattern;
    private Pattern mPhoneNumberPattern;
    private String mUserName;
    private String mPhoneNumber;
    private String mUserEmail;

    public FormPresenter(MainView mainActivity) {
        mViewInstance = mainActivity;
        mEmailPattern = Patterns.EMAIL_ADDRESS;
        mPhoneNumberPattern = Patterns.PHONE;
        //Gets the build.prop file when the apps init as it must be included by default in the query
    }

    private boolean isInputValid() {
        return !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mUserEmail)
                && ((mEmailPattern.matcher(mUserEmail)).matches())
                && !TextUtils.isEmpty(mPhoneNumber)
                && ((mPhoneNumberPattern.matcher(mPhoneNumber)).matches());
    }

    public void onUserFirstNameChanged(@Nullable String name) {
        if (!TextUtils.isEmpty(name)) {
            mUserName = name;
            mViewInstance.render(new MainView.ToggleAcceptButtonState(isInputValid()));
        }
    }

    public void onUserPhoneNumberChanged(@Nullable String number) {
        mPhoneNumber = number;
        mViewInstance.render(new MainView.ToggleAcceptButtonState(isInputValid()));
    }

    public void onUserEmailChanged(@Nullable String email) {
        mUserEmail = email;
        mViewInstance.render(new MainView.ToggleAcceptButtonState(isInputValid()));
    }

    public void sendDataToServer(File mAttachmentFile) {
        mViewInstance.render(new MainView.OnSendDataToServerState(mUserName, mPhoneNumber, mUserEmail,
                mAttachmentFile));
    }

    public void onLoseFocusName(String name) {
        if (TextUtils.isEmpty(name))
            mViewInstance.render(new MainView.EmptyNameState());
    }

    public void onLoseFocusPhone(String number) {
        if (TextUtils.isEmpty(number) || (!(mPhoneNumberPattern.matcher(number)).matches()))
            mViewInstance.render(new MainView.EmptyOrWrongNumberState());
    }

    public void onLoseFocusEmail(String email) {
        if (TextUtils.isEmpty(email) || (!(mEmailPattern.matcher(email)).matches()))
            mViewInstance.render(new MainView.EmptyOrWrongEmailState());
    }

}
