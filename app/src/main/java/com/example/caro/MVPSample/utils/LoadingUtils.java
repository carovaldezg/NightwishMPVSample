package com.example.caro.MVPSample.utils;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.example.caro.MVPSample.R;

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