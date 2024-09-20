package com.example.twf_final.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.twf_final.LoginMainActivity;
import com.example.twf_final.dataBase.entity.UserEntity;
import com.example.twf_final.viewModel.UserViewModel;
import java.io.FileNotFoundException;
import java.io.InputStream;
import at.wifi.swdev.noteapp.R;
public class AccountFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private ImageView pic, editPic;
    private TextView mobile, email, userName, lastName;
    private UserViewModel userViewModel;
    private UserEntity currentUser;
    private Button btn_delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        pic = view.findViewById(R.id.UserPic);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        userName = view.findViewById(R.id.UserName);
        lastName = view.findViewById(R.id.LastName);
        editPic = view.findViewById(R.id.editPic);
        btn_delete = view.findViewById(R.id.btn_delete);


        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        editPic.setOnClickListener(v -> showPopupMenu(v));

        pic.setOnClickListener(v -> chooseImage());

        userViewModel.getUser().observe(getViewLifecycleOwner(), this::updateUI);

        initializeViews(view);
        loadUserData();
        btn_delete.setOnClickListener(v -> confirmDelete());
        return view;
    }
    private void updateUI(UserEntity user) {
        if (user != null) {
            currentUser = user;
            mobile.setText(user.getMobileNumber());
            email.setText(user.getEmailAddress());
            userName.setText(user.getName());
            lastName.setText(user.getLastName());
            if (user.getProfilePicturePath() != null) {
                pic.setImageBitmap(BitmapFactory.decodeFile(user.getProfilePicturePath()));
            }
        }
    }

    private void initializeViews(View view) {
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);

    }

    private void loadUserData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = prefs.getString("Email", "Default Email");
        String userPassword = prefs.getString("Password", "Default Password");

        email.setText(userEmail);

    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.edit_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.edit_name) {
                    showEditNameDialog();
                    return true;
                } else if (itemId == R.id.logout) {
                    logout();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void showEditNameDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_edit_name, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);

        final EditText editFirstName = dialogView.findViewById(R.id.editFirstName);
        final EditText editLastName = dialogView.findViewById(R.id.editLastName);


        if (currentUser != null) {
            editFirstName.setText(currentUser.getName());
            editLastName.setText(currentUser.getLastName());
        }

        dialogBuilder.setPositiveButton("Save", (dialog, id) -> {
            String newFirstName = editFirstName.getText().toString();
            String newLastName = editLastName.getText().toString();
            if (!newFirstName.isEmpty() && !newLastName.isEmpty()) {
                if (currentUser == null) {

                    currentUser = new UserEntity();
                    currentUser.setMobileNumber("default");
                    currentUser.setEmailAddress("default@default.com");
                }
                currentUser.setName(newFirstName);
                currentUser.setLastName(newLastName);
                if (userViewModel != null) {
                    userViewModel.update(currentUser);
                } else {

                    Toast.makeText(getActivity(), "Error: User data could not be updated.", Toast.LENGTH_LONG).show();
                }
                userName.setText(newFirstName);
                lastName.setText(newLastName);
            } else {
                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserAccount();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUserAccount() {
        if (currentUser != null) {
            userViewModel.delete(currentUser);
            Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
            navigateToLoginMainActivity();
        } else {
            Toast.makeText(getActivity(), "Error: No user to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLoginMainActivity() {
        Intent intent = new Intent(getActivity(),LoginMainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void logout() {
        startActivity(new Intent(getActivity(), LoginMainActivity.class));
        getActivity().finish();
    }

    private void chooseImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                pic.setImageBitmap(bitmap);
                String imagePath = getPathFromUri(selectedImage);
                if (currentUser != null) {
                    currentUser.setProfilePicturePath(imagePath);
                    userViewModel.update(currentUser);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(), "Image load failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}



