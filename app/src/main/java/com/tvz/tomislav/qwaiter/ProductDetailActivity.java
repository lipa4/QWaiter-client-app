package com.tvz.tomislav.qwaiter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.firebase.ui.auth.AuthUI;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private View mFab;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private DrawerLayout mDrawerLayout;
    private static FoodDrinkModel sFoodDrink;
    private NumberPicker mNumberPicker;
    private TextSwitcher mPriceTextSwitcher;
    private int mQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.product_toolbar);
        mFab=(FloatingActionButton) findViewById(R.id.fab_product);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.flexible_appbar);
        appbar.addOnOffsetChangedListener(this);
        sFoodDrink = new FoodDrinkModel();
        sFoodDrink.setImageURL(getIntent().getExtras().getString("ImageURL"));
        Picasso.with(getBaseContext()).load(getIntent().getExtras().getString("ImageURL")).into((ImageView)findViewById(R.id.product_detail_image)
        );
        mQuantity=1;
        if (getIntent().hasExtra("Quantity"))
            mQuantity = getIntent().getIntExtra("Quantity", 1);

        mNumberPicker.setValue(mQuantity);
        String name = getIntent().getExtras().getString("Name");
        getSupportActionBar().setTitle(name);
        sFoodDrink.setName(name);
        String category = getIntent().getExtras().getString("Category");
        sFoodDrink.setCategory(category);
        TextView categoryTextView =(TextView)findViewById(R.id.product_detail_category);
        categoryTextView.setText(category.toUpperCase());

        int price = getIntent().getExtras().getInt("Price");
        sFoodDrink.setPrice(price);

        mPriceTextSwitcher = (TextSwitcher) findViewById(R.id.product_detail_price);
        mPriceTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {

                // Create a new TextView
                TextView t = new TextView(getBaseContext());
                t.setTextSize(getResources().getDimension(R.dimen.pricetextsize));
                t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return t;
            }
        });
        mPriceTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mPriceTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        mPriceTextSwitcher.setCurrentText(Integer.toString(price*mNumberPicker.getValue())+" kn");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.product_drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.product_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                int quantity =mNumberPicker.getValue();
                sFoodDrink.setQuantity(quantity);
                int index= CheckoutActivity.containsElement(sFoodDrink.getName());
                if (index != -1){
                    CheckoutActivity.sBasket.set(index,sFoodDrink);
                }
                else
                    CheckoutActivity.sBasket.add(sFoodDrink);
                //Notification
                String item;
                if (quantity>1)
                    item=" items are added to Basket!";
                else
                    item=" item is added to Basket";
                Snackbar.make(view,Integer.toString(quantity)+item,Snackbar.LENGTH_LONG)
                        .setAction("GOT IT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }).setActionTextColor(Color.CYAN).show();
            }
        });

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String newValue = Integer.toString(sFoodDrink.getPrice() * newVal);
                mPriceTextSwitcher.setText(newValue+" kn");
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

}
