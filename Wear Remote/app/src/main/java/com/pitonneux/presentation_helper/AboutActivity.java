package com.pitonneux.presentation_helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private TextView mTextView;
    private BoxInsetLayout boxInsetLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

                mTextView = (TextView)findViewById(R.id.text);
                boxInsetLayout2=(BoxInsetLayout)findViewById(R.id.box_inset_layout_about);

    }
}
