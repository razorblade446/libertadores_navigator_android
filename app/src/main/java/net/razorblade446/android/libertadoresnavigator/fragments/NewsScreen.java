package net.razorblade446.android.libertadoresnavigator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.razorblade446.android.libertadoresnavigator.R;

public class NewsScreen extends Fragment {

    public static NewsScreen newInstance() {
        return new NewsScreen();
    }

    public NewsScreen() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.news_screen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
