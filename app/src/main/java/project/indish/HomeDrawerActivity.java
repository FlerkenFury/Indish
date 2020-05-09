package project.indish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import project.indish.model.User;

public class HomeDrawerActivity extends AppCompatActivity{

    private static final String TAG = "HomeDrawerActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    TextView drawerName;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        SharedPref sharedPref = new SharedPref(HomeDrawerActivity.this);
        User user = sharedPref.load();

        Log.d(TAG, "onCreate: userName: " + user.getName());

        View headerView = navigationView.getHeaderView(0);
        drawerName = headerView.findViewById(R.id.nav_head_name);
        profilePic = headerView.findViewById(R.id.nav_head_image);

        drawerName.setText(user.getName());

        if (user.getImage() != null && !user.getImage().trim().isEmpty()){
            Glide.with(HomeDrawerActivity.this)
                    .load(user.getImage())
                    .placeholder(R.drawable.image_loader)
                    .into(profilePic);
        }

        Intent intent = getIntent();
        final String recipeName =  intent.getStringExtra("name");

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (recipeName!= null && !recipeName.trim().equalsIgnoreCase("")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment(recipeName)).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_ingredient:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                            break;
                        case R.id.nav_read_recipe:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecipeFragment()).commit();
                            break;
                        case R.id.nav_add_recipe:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddRecipeFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                            break;
                        case R.id.nav_logout:
                            logout();
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);

                    return true;
                }
            });


            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_ingredient);
            }
        }

    }

    public void logout(){
        SharedPref sharedPref =  new SharedPref(HomeDrawerActivity.this);
        sharedPref.clearAll(HomeDrawerActivity.this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(HomeDrawerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
