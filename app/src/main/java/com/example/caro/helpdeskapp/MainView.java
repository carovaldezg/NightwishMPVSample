package com.example.caro.helpdeskapp;


import com.example.caro.helpdeskapp.mvp.view.IBaseView;
import com.example.caro.helpdeskapp.mvp.view.IErrorView;
import com.example.caro.helpdeskapp.mvp.view.ILoadingView;

import java.io.File;

public interface MainView extends IBaseView, IErrorView, ILoadingView {

    class ToggleAcceptButtonState implements State {

        final boolean isInputValid;

        public ToggleAcceptButtonState(boolean inputValid) {
            this.isInputValid = inputValid;
        }

    }

    class OnSendDataToServerState implements State {

        String mUserName;
        String mPhoneNumber;
        String mUserEmail;
        File mAttachmentFile;

        public OnSendDataToServerState(String mUserName, String mPhoneNumber, String mUserEmail,
                                       File mAttachmentFile) {
            this.mUserEmail = mUserEmail;
            this.mUserName = mUserName;
            this.mPhoneNumber = mPhoneNumber;
            this.mAttachmentFile = mAttachmentFile;
        }

    }

    public class EmptyNameState implements State {

        public EmptyNameState(){}
    }

    public class EmptyOrWrongNumberState implements State {

        public EmptyOrWrongNumberState(){}
    }

    public class EmptyOrWrongEmailState implements State {

        public EmptyOrWrongEmailState(){}

    }

}
