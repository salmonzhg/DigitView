package com.salmonzhg.digitview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.salmonzhg.digitview.views.DigitalGroupView;

public class MainActivity extends AppCompatActivity {

    Button buttonPlay, buttonAddView;
    AppCompatSeekBar seekInterval, seekFigureCount, seekSize;
    DigitalGroupView digitalGroupView;
    EditText editDigit;
    private boolean hasViewAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = (Button) findViewById(R.id.button_play);
        buttonAddView = (Button) findViewById(R.id.button_add_view);
        seekInterval = (AppCompatSeekBar) findViewById(R.id.seek_interval);
        seekFigureCount = (AppCompatSeekBar) findViewById(R.id.seek_figure_count);
        seekSize = (AppCompatSeekBar) findViewById(R.id.seek_size);
        digitalGroupView = (DigitalGroupView) findViewById(R.id.digital);
        editDigit = (EditText) findViewById(R.id.edit_digital);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 0;
                try {
                    num = Integer.parseInt(editDigit.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                digitalGroupView.setDigits(num);
            }
        });


        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    return;
                }
                switch (seekBar.getId()) {
                    case R.id.seek_interval:
                        digitalGroupView.setInterval(progress);
                        break;
                    case R.id.seek_figure_count:
                        digitalGroupView.setFigureCount(progress);
                        break;
                    case R.id.seek_size:
                        digitalGroupView.setTextSize(progress);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekFigureCount.setOnSeekBarChangeListener(seekBarChangeListener);
        seekSize.setOnSeekBarChangeListener(seekBarChangeListener);
        seekInterval.setOnSeekBarChangeListener(seekBarChangeListener);

        hasViewAdded = false;
        buttonAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasViewAdded)
                    return;
                addView();
                hasViewAdded = true;
            }
        });
    }

    private void addView() {
        DigitalGroupView view = new DigitalGroupView(this);
        view.setTextSize(14);
        view.setFigureCount(3);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        view.setLayoutParams(params);

        ViewGroup vg = (ViewGroup) buttonPlay.getParent();
        vg.addView(view);
    }
}
