package com.tvz.tomislav.qwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class CheckoutActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener{


    private DrawerLayout mDrawerLayout;
    private int mMaxScrollSize;
    private RecyclerView mRootView;
    private FakePageAdapter mAdapter;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    public static View.OnClickListener sClickListener;
    private ImageView mAvatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.checkout_appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.checkout_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("CHECKOUT");
       // mAvatarImage =(ImageView)findViewById(R.id.checkout_avatar) ;
        //appbarLayout.addOnOffsetChangedListener(this);
        //mMaxScrollSize = appbarLayout.getTotalScrollRange();

        //Setting back button on the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRootView = (RecyclerView) findViewById(R.id.checkout_recycler_view);

        mAdapter = new CheckoutActivity.FakePageAdapter(20);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(mAdapter);

        sClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProductDetailActivity.class);
                startActivity(intent);
            }
        };

        //Basket floating action buttton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.checkout_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Your order was paid and ordered!",Toast.LENGTH_LONG).show();
                 startActivity(new Intent(getBaseContext(),MainActivity.class));
                 finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance().signOut(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mAvatarImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mAvatarImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    private static class FakePageAdapter extends RecyclerView.Adapter<CheckoutActivity.FakePageVH> {
        private final int numItems;


        FakePageAdapter(int numItems) {
            this.numItems = numItems;

        }

        @Override
        public CheckoutActivity.FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_card, viewGroup, false);
            itemView.setOnClickListener(sClickListener);
            return new CheckoutActivity.FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(CheckoutActivity.FakePageVH fakePageVH, int i) {
            // do nothing
        }

        @Override
        public int getItemCount() {
            return numItems;
        }
    }

    private static class FakePageVH extends RecyclerView.ViewHolder {
        FakePageVH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }

}

