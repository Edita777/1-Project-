package com.example.twf_final.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.twf_final.MainActivity;
import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.entity.UserEntity;

import at.wifi.swdev.noteapp.R;

public class SignupTabFragment extends Fragment {

    EditText mobile, email, password, passConf;
    Button button;
    AppDataBase db;
    ImageButton visibility01, visibility02;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_fragment, container, false);

        db = AppDataBase.getInstance(requireContext());

        mobile = root.findViewById(R.id.mobile);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.pass);
        passConf = root.findViewById(R.id.passConf);
        button = root.findViewById(R.id.button);
        visibility01 = root.findViewById(R.id.visibility01);
        visibility02 = root.findViewById(R.id.visibility02);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passConf.setTransformationMethod(PasswordTransformationMethod.getInstance());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = mobile.getText().toString();
                String emailAddress = email.getText().toString();
                String enteredPassword = password.getText().toString();
                String confirmPassword = passConf.getText().toString();

                if (mobileNumber.equals("0664123456") && emailAddress.equals("user@gmx.at")
                        && enteredPassword.equals("123456") && confirmPassword.equals("123456")) {

                    UserEntity userEntity = new UserEntity();
                    userEntity.setMobileNumber(mobileNumber);
                    userEntity.setEmailAddress(emailAddress);

                    new InsertUserTask(requireContext()).execute(userEntity);

                    Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visibility01.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        password.setTransformationMethod(null);
                        password.setSelection(password.getText().length());
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        password.setSelection(password.getText().length());
                        return true;
                }
                return false;
            }
        });

        visibility02.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passConf.setTransformationMethod(null);
                        passConf.setSelection(passConf.getText().length());
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        passConf.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passConf.setSelection(passConf.getText().length());
                        return true;
                }
                return false;
            }
        });

        return root;
    }

    private static class InsertUserTask extends AsyncTask<UserEntity, Void, Void> {
        private Context context;

        InsertUserTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(UserEntity... users) {
            AppDataBase.getInstance(context).userDao().insert(users[0]);
            return null;
        }
    }
}