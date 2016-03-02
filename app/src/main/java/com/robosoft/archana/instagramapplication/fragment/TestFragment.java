package com.robosoft.archana.instagramapplication.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class TestFragment extends Fragment {

    private Context mContext;
    private View mOneRow;
    private EditText mEditComment;
    private Button mButtonOk;
    NoOfCommentInterface noOfCommentInterface;
    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = container.getContext();

            mOneRow = inflater.inflate(R.layout.fragment_test, container, false);
           // noOfCommentInterface = (NoOfCommentInterface) mContext;

           mEditComment = (EditText) mOneRow.findViewById(R.id.editc);
        mButtonOk = (Button)mOneRow.findViewById(R.id.btn);
         Log.i("Hello", "Context is" + mContext);
         Log.i("Hello","I am in onCreateView in TestFragment");

        return  mOneRow;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Hello", "NOFFFFFFFFFFFFFFFFFF" + noOfCommentInterface);
        mButtonOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Hello", "I AM IN ONCLICK OF BUTTON");
                int noOfComments = Integer.parseInt(mEditComment.getText().toString());
                //noOfCommentInterface.onClick(noOfComments);
                //  getActivity().getSupportFragmentManager().beginTransaction().remove(SettingFragment.this).commit();
           //     noOfCommentInterface.onClick(noOfComments);

                getActivity().getSupportFragmentManager().beginTransaction().remove(TestFragment.this).commit();
            }
        });
    }
}
