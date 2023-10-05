package com.skh.enjoyriding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.skh.enjoyriding.utils.Constant;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    ViewPager2 viewPager2;
    Button btnNext, btnSkip;
    int[] layouts;
    ViewsSliderAdapter mAdapter;
    CheckBox cbNeverAsk;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Boolean isCheckedFromSharedPreference = sharedPreferences.getBoolean(Constant.NEVER_ASK_AGAIN, false);
        Log.d(TAG, "onResume isCheckedFromSharedPreference : " + isCheckedFromSharedPreference);
        if (isCheckedFromSharedPreference) {
            launchLoginScreen(false);
        }
    }



    private void init() {
        sharedPreferences = getSharedPreferences(Constant.NEVER_ASK_AGAIN, Context.MODE_PRIVATE);
        Boolean isCheckedFromSharedPreference = sharedPreferences.getBoolean(Constant.NEVER_ASK_AGAIN, false);
        layouts = new int[]{R.layout.slide_guide_1, R.layout.slide_guide_2, R.layout.slide_guide_3};
        viewPager2 = (ViewPager2) findViewById(R.id.view_paper);
        mAdapter = new ViewsSliderAdapter(layouts);
        viewPager2.setAdapter(mAdapter);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                if (current < layouts.length) {
                    viewPager2.setCurrentItem(current);
                    if (current == layouts.length - 1) {
                        btnNext.setText(getString(R.string.continue_str));
                    }
                } else {
                    launchLoginScreen();
                }
            }
        });
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginScreen();
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == layouts.length - 1) {
                    btnNext.setText(getString(R.string.continue_str));
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
        });

        cbNeverAsk = findViewById(R.id.cbNeverAsk);
        if (isCheckedFromSharedPreference) {
            cbNeverAsk.setChecked(true);
        }
        cbNeverAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cbNeverAsk.isChecked();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constant.NEVER_ASK_AGAIN, isChecked);
                editor.commit();
            }
        });
}

    private int getItem(int i) {
        return viewPager2.getCurrentItem() + i;
    }

    private void launchLoginScreen() {
        launchLoginScreen(true);
    }

    private void launchLoginScreen(boolean history) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        if (!history) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }

    class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int[] layoutScreens;

        public ViewsSliderAdapter(int[] layouts) {
            layoutScreens = layouts;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        }

        @Override
        public int getItemViewType(int position) {
            return layoutScreens[position];
        }

        @Override
        public int getItemCount() {
            return layoutScreens.length;
        }

        public class SliderViewHolder extends RecyclerView.ViewHolder {
            public SliderViewHolder (View view) {
                super(view);
            }
        }
    }
}