// HomeActivity.java
package com.example.phoneapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.phoneapp.Fragment.AndroidFragment;
import com.example.phoneapp.Fragment.BodyFragment;
import com.example.phoneapp.Fragment.CartFragment;
import com.example.phoneapp.Fragment.IphoneFragment;
import com.example.phoneapp.Fragment.ProfileFragment;
import com.example.phoneapp.Fragment.ItemManagementFragment;
import com.example.phoneapp.Fragment.DashboardFragment;
import com.example.phoneapp.Fragment.UserManagementFragment;
import com.example.phoneapp.Model.User;
import com.example.phoneapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private String userRole, username;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawerLayout and BottomNavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(this::onBottomNavigationItemSelected);

        navigationView = findViewById(R.id.navigation_view);

        // Fetch user role and user ID
        fetchUserRole();
    }

    private void fetchUserRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Store user ID
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().exists()) {
                        User user = task.getResult().getValue(User.class);
                        if (user != null) {
                            userRole = user.getRole();
                            username = user.getName(); // Get username

                            setupNavigationDrawer();
                            updateUsernameInHeader(username);
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch user role", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to fetch user role", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUsernameInHeader(String username) {
        // Get header view from the NavigationView
        View headerView = navigationView.getHeaderView(0);
        // Find the TextView in the header view
        TextView userNameView = headerView.findViewById(R.id.username);
        // Set the username
        userNameView.setText(username);
    }

    private void setupNavigationDrawer() {
        navigationView = findViewById(R.id.navigation_view);
        if ("admin".equals(userRole)) {
            navigationView.inflateMenu(R.menu.admin_drawer_menu);
            loadFragment(new DashboardFragment());
        } else {
            navigationView.inflateMenu(R.menu.menu_drawer);
            loadFragment(new BodyFragment());
        }

        navigationView.setNavigationItemSelectedListener(item -> onDrawerItemSelected(item));
    }

    private boolean onBottomNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            selectedFragment = new BodyFragment();
        } else if (itemId == R.id.nav_cart) {
            selectedFragment = new CartFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private boolean onDrawerItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (item.getItemId() == R.id.nav_logout) {
            logout();
        } else if (itemId == R.id.nav_item_manager) {
            selectedFragment = new ItemManagementFragment();
        } else if (itemId == R.id.nav_iphone) {
            selectedFragment = new IphoneFragment();
        } else if (itemId == R.id.nav_android) {
            selectedFragment = new AndroidFragment();
        } else if (itemId == R.id.nav_dashboard) {
            selectedFragment = new DashboardFragment();
        } else if (itemId == R.id.nav_user_manager) {
            selectedFragment = new UserManagementFragment();
        }
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    // Helper method to load the fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Public getter for userId
    public String getUserId() {
        return userId;
    }
}
