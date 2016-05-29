package com.innovamos.btracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovamos.btracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class MessageFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Title";
    private static final String FRAGMENT_MESSAGE = "Message";

    private String title;
    private String message;

    private TextView messageTextView;
    private TextView titleTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title of the message.
     * @param message Message.
     * @return A new instance of fragment MessageFragment.
     */
    public static MessageFragment newInstance(String title, String message) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, title);
        args.putString(FRAGMENT_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            title = getArguments().getString(FRAGMENT_TITLE);
            message = getArguments().getString(FRAGMENT_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        messageTextView = (TextView) view.findViewById(R.id.TextMessage);
        titleTextView = (TextView) view.findViewById(R.id.TextTitle);

        messageTextView.setText(message);
        titleTextView.setText(title);

        return view;
    }
}
