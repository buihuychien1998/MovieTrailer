package com.example.mvvmmovieapp.ui.account;


import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText et_name, et_re_email, et_re_password, et_re_repassword;
    private DBHelper mDataBase;
    private Button btn_register;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_name = view.findViewById(R.id.et_name);
        et_re_email = view.findViewById(R.id.et_re_email);
        et_re_password = view.findViewById(R.id.et_re_password);
        et_re_repassword = view.findViewById(R.id.et_re_repassword);
        btn_register = view.findViewById(R.id.btn_register);
        mDataBase = new DBHelper(getContext());

        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.btn_register) {
                if (et_re_password.getText().toString().length() > 5) {
                    try {
                        if (mDataBase.insertUser(et_re_email.getText().toString(),
                                et_name.getText().toString(), et_re_password.getText().toString()) != -1 && et_re_password.getText().toString().equals(et_re_repassword.getText().toString())) {
                            Toast.makeText(this.getContext(), "Create account success!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(this.getContext(), "Create account fail, please check again", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this.getContext(), "Password is weak, please check again ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
