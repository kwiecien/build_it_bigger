package com.kk.androidjokes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class JokeFragment extends Fragment {

    private static final String ARG_JOKE = "joke";
    private String mJoke;

    public JokeFragment() {
        // Required empty public constructor
    }

    public static JokeFragment newInstance(String joke) {
        Bundle args = new Bundle();
        args.putString(ARG_JOKE, joke);
        JokeFragment fragment = new JokeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJoke = getArguments().getString(ARG_JOKE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, container, false);
        TextView jokeTextView = view.findViewById(R.id.joke_tv);
        jokeTextView.setText(mJoke);
        return view;
    }

}
