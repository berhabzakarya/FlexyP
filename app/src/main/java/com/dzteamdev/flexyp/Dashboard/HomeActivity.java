package com.dzteamdev.flexyp.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Dashboard.Products.BeinSport;
import com.dzteamdev.flexyp.Dashboard.Products.GiftCard;
import com.dzteamdev.flexyp.Dashboard.Products.RechargeMobile;
import com.dzteamdev.flexyp.Main.LoginActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.R;
import com.google.android.material.navigation.NavigationView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST = 1;
    private NavigationView navigationView;
    private String full_name;
    private TextView name;
    private CircleImageView photo;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/font.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_home);
        Paper.init(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        view.setBackground(getResources().getDrawable(R.drawable.header));
        name = view.findViewById(R.id.name_header);
        photo = view.findViewById(R.id.photo_header);
        if (CONSTANTS.user.getImg() != null) {
            Picasso.get().load(CONSTANTS.user.getImg()).into(photo);
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.rm, RechargeMobile.class)
                .add(R.string.bs, BeinSport.class)
                .add(R.string.gf, GiftCard.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        full_name = CONSTANTS.user.getFullName();
        name.setText(full_name);

    }

    @Override
    protected void onStart() {
        super.onStart();
        full_name = CONSTANTS.user.getFullName();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.put_money:
                showDialog();
                break;
            case R.id.nav_cart:
                navigationView.setCheckedItem(R.id.nav_cart);
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.nav_settings:
                navigationView.setCheckedItem(R.id.nav_settings);
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                Paper.book().destroy();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_send_msg, null);
        LinearLayout email = view.findViewById(R.id.email);
        LinearLayout messenger = view.findViewById(R.id.messenger);
        LinearLayout paypal = view.findViewById(R.id.paypal);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dzdev.tm@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Send Money");
                try {
                    startActivityForResult(Intent.createChooser(i, "Send mail..."), REQUEST);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + "4khalil"));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(view);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
