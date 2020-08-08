package com.example.mvvmmovieapp.ui.account;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvvmmovieapp.R;
import com.example.mvvmmovieapp.data.repository.DBHelper;
import com.example.mvvmmovieapp.ui.popular_movie.MainActivity;
import com.example.mvvmmovieapp.utils.Const;
import com.example.mvvmmovieapp.utils.SharedPrefs;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private Button btn_login;
    private EditText et_email, et_password;

    private DBHelper mDataBase;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_login = view.findViewById(R.id.btn_login);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        mDataBase = new DBHelper(getContext());
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.btn_login) {
                if (mDataBase.validUser(et_email.getText().toString(), et_password.getText().toString())) {
                    Toast.makeText(this.getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    SharedPrefs.Companion.getInstance().put(Const.INSTANCE.getUSERID(), et_email.getText().toString());
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    if (getActivity() != null){
                        getActivity().finish();
                    }
                } else if (TextUtils.isEmpty(et_email.getText().toString()) && TextUtils.isEmpty(et_password.getText().toString())) {
                    Toast.makeText(this.getContext(), "Please enter your username and password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Login fail, please check again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
