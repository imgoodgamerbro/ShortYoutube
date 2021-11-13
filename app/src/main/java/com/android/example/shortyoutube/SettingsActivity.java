package com.android.example.shortyoutube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences = null;
    private EditText et_channel, et_channel_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        boolean boolVal = mSharedPreferences.getBoolean(getString(R.string.shared_preference_night_mode), false);
        if(!boolVal){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView textView = findViewById(R.id.tv_settings);
        SwitchCompat switchCompat = findViewById(R.id.switcher);
        et_channel = findViewById(R.id.settings_et_channel_list);
        et_channel_video = findViewById(R.id.settings_et_channel_video_list);
        Button button = findViewById(R.id.save_button);

        if(boolVal){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
            textView.setText(getString(R.string.settings_activity_night_mode));
        }
        String et1 = mSharedPreferences.getString(getString(R.string.shared_preference_channel_list), getString(R.string.default_value_int));
        et_channel.setText(et1);
        String et2 = mSharedPreferences.getString(getString(R.string.shared_preference_channel_video_list), getString(R.string.default_value_int));
        et_channel_video.setText(et2);

        //set Limits on Edit Texts
        et_channel.setFilters(new InputFilter[]{new InputFilterMinMax("1", "55")});
        et_channel_video.setFilters(new InputFilter[]{new InputFilterMinMax("1", "55")});

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(getString(R.string.shared_preference_night_mode), true);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    textView.setText(R.string.settings_activity_night_mode);
                } else {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(getString(R.string.shared_preference_night_mode), false);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    textView.setText(R.string.settings_activity_light_mode);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(getString(R.string.shared_preference_channel_list), et_channel.getText().toString());
                editor.putString(getString(R.string.shared_preference_channel_video_list), et_channel_video.getText().toString());
                editor.apply();

                if(et_channel.getText().toString().isEmpty() && et_channel_video.getText().toString().isEmpty()){
                    Toast.makeText(SettingsActivity.this, getString(R.string.msg_1), Toast.LENGTH_SHORT).show();
                }
                else if(et_channel.getText().toString().isEmpty() && !et_channel_video.getText().toString().isEmpty()){
                    Toast.makeText(SettingsActivity.this, getString(R.string.msg_2), Toast.LENGTH_SHORT).show();
                }
                else if(!et_channel.getText().toString().isEmpty() && et_channel_video.getText().toString().isEmpty()){
                    Toast.makeText(SettingsActivity.this, getString(R.string.msg_3), Toast.LENGTH_SHORT).show();
                }  else {
                    new Intent(SettingsActivity.this, MainActivity.class);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(et_channel.getText().toString().isEmpty() && et_channel_video.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.msg_1), Toast.LENGTH_SHORT).show();
        }
        else if(et_channel.getText().toString().isEmpty() && !et_channel_video.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.msg_2), Toast.LENGTH_SHORT).show();
        }
        else if(!et_channel.getText().toString().isEmpty() && et_channel_video.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.msg_3), Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }

    private static class InputFilterMinMax implements InputFilter {

        private final int min;
        private final int max;

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}