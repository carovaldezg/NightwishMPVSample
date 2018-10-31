package com.example.caro.helpdeskapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.caro.helpdeskapp.mvp.view.IBaseView;
import com.example.caro.helpdeskapp.mvp.view.IErrorView;
import com.example.caro.helpdeskapp.utils.LoadingUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements MainView {

    int PICK_IMAGE_REQUEST = 1;

    @BindView(R.id.activity_form_name_edit_text)
    EditText mNameEditText;
    @BindView(R.id.activity_form_phone_number_edit_text)
    EditText mPhoneNumberEditText;
    @BindView(R.id.activity_form_email_edit_text)
    EditText mEmailEditText;
    @BindView(R.id.activity_form_send_button)
    Button mSendButton;
    @BindView(R.id.activity_form_attachment_linear_layout)
    LinearLayout mAttachmentButton;
    @BindView(R.id.activity_form_delete_image_view)
    ImageView mRemoveAttachment;
    @BindView(R.id.activity_form_attachment_title_text_view)
    TextView mFileTitle;

    private FormPresenter mFormPresenter;
    private Dialog mLoadingDialog;
    private File mAttachmentFile;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
        toggleAcceptButton(false);
        mFormPresenter = new FormPresenter(this);
        mLoadingDialog = LoadingUtils.buildLoadingDialog(this);
        setListeners();
    }

    TextWatcher mNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           mFormPresenter.onUserFirstNameChanged(mNameEditText.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    TextWatcher mPhoneNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mFormPresenter.onUserPhoneNumberChanged(mPhoneNumberEditText.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    TextWatcher mEmailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mFormPresenter.onUserEmailChanged(mEmailEditText.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void setListeners() {
        mNameEditText.addTextChangedListener(mNameTextWatcher);
        mPhoneNumberEditText.addTextChangedListener(mPhoneNumberTextWatcher);
        mEmailEditText.addTextChangedListener(mEmailTextWatcher);

        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mFormPresenter.onLoseFocusName(mNameEditText.getText().toString());
                }
            }
        });

        mPhoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mFormPresenter.onLoseFocusPhone(mPhoneNumberEditText.getText().toString());
                }
            }
        });

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mFormPresenter.onLoseFocusEmail(mEmailEditText.getText().toString());
                }
            }
        });
    }

    @Override
    public void render(@NonNull State state) {
        if (state instanceof ToggleAcceptButtonState) {
            renderToggleButtonState((ToggleAcceptButtonState) state);
        } else if (state instanceof UnknownErrorState) {
            renderUnknownErrorState((UnknownErrorState) state);
        } else if (state instanceof OnSendDataToServerState) {
            renderOnSendDataToServerState((OnSendDataToServerState) state);
        } else if (state instanceof EmptyNameState) {
            renderOnEmptyNameState((EmptyNameState) state);
        } else if (state instanceof EmptyOrWrongNumberState) {
            renderEmptyOrWrongNumberState((EmptyOrWrongNumberState) state);
        } else if (state instanceof EmptyOrWrongEmailState) {
            renderEmptyOrWrongEmailState((EmptyOrWrongEmailState) state);
        }
    }

    private void renderEmptyOrWrongEmailState(EmptyOrWrongEmailState state) {
        mEmailEditText.setError("The text is empty or does not match valid e-mail address");
    }

    private void renderEmptyOrWrongNumberState(EmptyOrWrongNumberState state) {
        mPhoneNumberEditText.setError("The text is empty or does not match valid number");
    }

    private void renderOnEmptyNameState(EmptyNameState state) {
        mNameEditText.setError(getResources().getString(R.string.empty_field_error));
    }

    //TODO: find a free server to send this data using retrofit
    private void renderOnSendDataToServerState(OnSendDataToServerState state) {
        String data = state.mUserName +", "+state.mPhoneNumber+", "+state.mUserEmail;
        if (state.mAttachmentFile != null){
            data += state.mAttachmentFile.getAbsolutePath();
        }
        Toast.makeText(this, "The input data is: " + data,
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MessageSentActivity.class));
    }

    private void renderToggleButtonState(ToggleAcceptButtonState state) {
        toggleAcceptButton(state.isInputValid);
    }

    public void toggleAcceptButton(boolean isInputValid) {
        mSendButton.setClickable(isInputValid);
        if (isInputValid)
            mSendButton.setBackgroundResource(R.drawable.red_rounded_button);
        else {
            mSendButton.setBackgroundResource(R.drawable.grey_rounded_button);
        }
    }

    private void renderUnknownErrorState(IErrorView.UnknownErrorState state) {
        renderHideLoadingState();
        mSendButton.setClickable(true);
        Toast.makeText(this, getResources().getString(R.string.unknown_error),
                Toast.LENGTH_LONG).show();
    }

    private void rendershowLoadingState(ShowLoadingState showLoadingState) {
        mLoadingDialog.show();
    }

    private void renderHideLoadingState() {
        mLoadingDialog.dismiss();
    }

    @OnClick (R.id.activity_form_send_button)
    public void onSendButtonClicked() {
        mSendButton.setClickable(false);
        mFormPresenter.sendDataToServer(mAttachmentFile);
    }

    @OnClick(R.id.activity_form_attachment_linear_layout)
    public void onClickAddAttachment(){
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PICK_IMAGE_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            Uri uri = data.getData();

            Cursor returnCursor =
                    getContentResolver().query(uri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            try {
                mAttachmentFile = new File(uri.getPath());
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mFileTitle.setText(returnCursor.getString(nameIndex));
                mFileTitle.setVisibility(View.VISIBLE);
                mRemoveAttachment.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.activity_form_delete_image_view)
    public void onRemoveImage() {
        mFileTitle.setVisibility(View.INVISIBLE);
        mRemoveAttachment.setVisibility(View.INVISIBLE);
        mFileTitle.setText("");
        mBitmap = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", mNameEditText.getText().toString());
        outState.putString("phone", mPhoneNumberEditText.getText().toString());
        outState.putString("email", mEmailEditText.getText().toString());
        outState.putString("file", mFileTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNameEditText.setText(savedInstanceState.get("name").toString());
        mPhoneNumberEditText.setText(savedInstanceState.get("phone").toString());
        mEmailEditText.setText(savedInstanceState.get("email").toString());
        if (!TextUtils.isEmpty(savedInstanceState.get("file").toString())) {
            mFileTitle.setText(savedInstanceState.get("file").toString());
            mFileTitle.setVisibility(View.VISIBLE);
            mRemoveAttachment.setVisibility(View.VISIBLE);
        }
    }

}
