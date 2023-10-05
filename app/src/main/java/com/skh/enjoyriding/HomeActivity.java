package com.skh.enjoyriding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        setBottomNavigation();
        bottomNavView.setSelectedItemId(R.id.nav_bottom_search);
    }

    private void setBottomNavigation(){
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;

                switch (item.getItemId()){
                    case R.id.nav_bottom_search:
                        //Open find bike fragment
                        frag = new FindBikeMapFragment();
                        break;
                    case R.id.nav_bottom_account:
                        frag = new AccountFragment();
                        break;
                }

                if(frag != null){
                    FragmentTransaction frgTrans = getSupportFragmentManager().beginTransaction();
                    frgTrans.replace(R.id.frame, frag);
                    frgTrans.commit();

                    return true;
                }

                return false;
            }
        });
    }
}