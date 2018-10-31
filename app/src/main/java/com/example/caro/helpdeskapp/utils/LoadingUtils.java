package com.example.caro.helpdeskapp.utils;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.example.caro.helpdeskapp.R;

public class LoadingUtils {
    private LoadingUtils() {}

    @NonNull
    public static Dialog buildLoadingDialog(@NonNull Context context,
                                            @ColorRes int indeterminateColorRes) {
        return new BlockingProgressDialog(context, indeterminateColorRes);
    }

    @NonNull
    public static Dialog buildLoadingDialog(@NonNull Context context) {
        return new BlockingProgressDialog(context, R.color.denim);
    }

}