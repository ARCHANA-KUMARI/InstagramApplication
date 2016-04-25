package com.robosoft.archana.instagramapplication.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.robosoft.archana.instagramapplication.Interfaces.NoOfCommentInterface;
import com.robosoft.archana.instagramapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends DialogFragment{

    private EditText mEditComment;
    private Button mButtonOk,mButtonCancel;
    NoOfCommentInterface noOfCommentInterface;
    private static final String NO_OF_SETTING_COMMENTS = "noOfSetComments";
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

          View view  = inflater.inflate(R.layout.fragment_setting, container, false);
          mEditComment = (EditText) view.findViewById(R.id.editcomment);
          mButtonOk = (Button)view.findViewById(R.id.btn);
          mButtonCancel = (Button)view.findViewById(R.id.cancel);
          getDialog().setTitle(R.string.dialogtitle);
          return  view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        noOfCommentInterface = (NoOfCommentInterface) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditComment.setText(String.valueOf(getArguments().getInt(NO_OF_SETTING_COMMENTS)));
        mButtonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!mEditComment.getText().toString().isEmpty()) {
                    int noOfComments = Integer.parseInt(mEditComment.getText().toString());
                    noOfCommentInterface.onClick(noOfComments);
                    dismiss();
                }
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditComment.getText().toString().isEmpty())
                     mEditComment.setText(" ");
            }
        });
    }


}
