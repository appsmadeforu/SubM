package com.example.sofiy.subm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardFragment extends Fragment {

    private View DashboardView;
    private FirebaseAuth mAuth;

    public DashboardFragment()
    {


        //Empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth = FirebaseAuth.getInstance();
        return DashboardView;
    }
}
