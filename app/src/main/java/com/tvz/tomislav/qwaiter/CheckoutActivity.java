package com.tvz.tomislav.qwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.tvz.tomislav.qwaiter.MainActivity.sObjectID;
import static com.tvz.tomislav.qwaiter.MainActivity.sObjectPath;
import static com.tvz.tomislav.qwaiter.MainActivity.sTable;

public class CheckoutActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout mDrawerLayout;
    private RecyclerView mRootView;
    private CheckoutAdapter mAdapter;
    public static View.OnClickListener sClickListener;
    public static List<FoodDrinkModel> sBasket = new ArrayList<>();
    private static TextView sSubtotal;
    public static Order sOrder;

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
        //appbarLayout.addOnOffsetChangedListener(this);
        //mMaxScrollSize = appbarLayout.getTotalScrollRange();
        //Setting back button on the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sSubtotal = (TextView)findViewById(R.id.checkout_subtotal);
        refreshSubTotal();
        mRootView = (RecyclerView) findViewById(R.id.checkout_recycler_view);

        mAdapter = new CheckoutAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(mAdapter);
        mRootView.addOnItemTouchListener(new RecyclerTouchListener(this, mRootView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FoodDrinkModel item = sBasket.get(position);
                Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
                intent.putExtra("Quantity",item.getQuantity());
                intent.putExtra("Name",item.getName());
                intent.putExtra("ImageURL", item.getImageURL());
                intent.putExtra("Category", item.getCategory());
                intent.putExtra("Price",item.getPrice());
                startActivity(intent);
            }
        }) );
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRootView);

        sClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProductDetailActivity.class);
                startActivity(intent);
            }
        };

        //Basket floating action buttton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.checkout_fab);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sOrder=new Order();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CheckoutActivity.this)
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                sendOrderToWaiter();
                                Toast.makeText(getApplicationContext(),"Order was placed succesfuly!",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .title("Payment method")
                        .items(R.array.items)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                sOrder.setPaymentModel(getResources().getStringArray(R.array.items)[which]);
                                return true;
                            }
                        })
                        .positiveText("PAY")
                        .cancelable(true)
                        .icon(getResources().getDrawable(R.drawable.ic_payment_black_24dp,getTheme()))
                        .show();
            }
        });
    }

    private void sendOrderToWaiter() {
        sOrder.setOrderList(sBasket);
        sOrder.setWaiter("waiter");
        sOrder.setUserID(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        sOrder.setSubtotal(refreshSubTotal());
        sOrder.setTable(sTable);
        sOrder.setOrderDate(Calendar.getInstance().getTime().toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(sObjectPath).child(sObjectID).child("orders");
        databaseReference.push().setValue(sOrder);
        sBasket.clear();
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

    private static int refreshSubTotal(){
        int subTotal=0;
        for (FoodDrinkModel item: sBasket){
            subTotal+=(item.getPrice()*item.getQuantity());
        }
        String sTotal = Integer.toString(subTotal);
        sSubtotal.setText(sTotal+" kn");
        return subTotal;
    }



    private static class CheckoutAdapter extends RecyclerView.Adapter<CheckoutViewHolder> implements ItemTouchHelperAdapter {

        Context mContext;
        CheckoutAdapter() {
        }

        @Override
        public CheckoutViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            mContext=viewGroup.getContext();
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_card, viewGroup, false);
            return new CheckoutViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(CheckoutViewHolder checkoutViewHolder, int i) {
            FoodDrinkModel item =sBasket.get(i);
            checkoutViewHolder.itemName.setText(item.getName());
            Picasso.with(mContext).load(item.getImageURL()).into(checkoutViewHolder.itemImage);
        }

        @Override
        public int getItemCount() {
            return sBasket.size();
        }

        @Override
        public void onItemDismiss(int position) {
           sBasket.remove(position);
           refreshSubTotal();
            notifyItemRemoved(position);
        }
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(sBasket, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(sBasket, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
    }

    private static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;
        CheckoutViewHolder(View itemView) {
            super(itemView);
            itemName=(TextView) itemView.findViewById(R.id.list_item_name);
            itemImage=(ImageView) itemView.findViewById(R.id.list_item_image);
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

    public interface ItemTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }

    public static int containsElement(String name){
        int position=-1,i=0;
        for (FoodDrinkModel item:sBasket){
            if (name.equals(item.getName())){
                return i;
            }
            i++;
        }
        return -1;
    }


}

