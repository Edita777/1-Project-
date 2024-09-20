package com.example.twf_final.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.twf_final.MainActivity;
import at.wifi.swdev.noteapp.R;

public class LoginTabFragment extends Fragment {
    EditText email, password;
    TextView forgetPass;
    Button button;
    ImageButton visibility;
    ImageView emailIcon;
    CheckBox saveData;

    float m = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.pass);
        forgetPass = root.findViewById(R.id.forgetPass);
        button = root.findViewById(R.id.button);
        visibility = root.findViewById(R.id.visibility00);
        emailIcon = root.findViewById(R.id.emailIcon);
        saveData = root.findViewById(R.id.saveData);

        email.setTranslationY(800);
        emailIcon.setTranslationY(800);
        password.setTranslationY(800);
        visibility.setTranslationY(800);
        forgetPass.setTranslationY(800);
        saveData.setTranslationY(800);
        button.setTranslationY(800);

        email.setAlpha(m);
        emailIcon.setAlpha(m);
        password.setAlpha(m);
        visibility.setAlpha(m);
        forgetPass.setAlpha(m);
        saveData.setAlpha(m);
        button.setAlpha(m);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        emailIcon.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        visibility.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        forgetPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        saveData.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        loadSavedCredentials();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = email.getText().toString();
                String enteredPassword = password.getText().toString();

                if (emailAddress.equals("user@gmx.at") && enteredPassword.equals("123456")) {
                    Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                    if (saveData.isChecked()) {
                        saveCredentials(emailAddress, enteredPassword);
                    } else {
                        clearSavedCredentials();
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visibility.setOnTouchListener(new View.OnTouchListener() {
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
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void loadSavedCredentials() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            email.setText(savedEmail);
            password.setText(savedPassword);
            saveData.setChecked(true);
        }
    }

    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}


