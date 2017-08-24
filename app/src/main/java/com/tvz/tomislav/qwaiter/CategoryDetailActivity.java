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
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.tvz.tomislav.qwaiter.MainActivity.sObjectID;

public class CategoryDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private RecyclerView mRootView;
    private CategoryDetailAdapter mAdapter;
    public static View.OnClickListener clickListener;
    private static List<FoodDrinkModel> sListItems=new ArrayList<>();;
    private static String sImageURL;
    private static String sName;
    private static String sCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.material_appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.category_detail_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        //Setting back button on the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        if(getIntent().getExtras()!=null){
            sListItems=new ArrayList<>();
            int position=getIntent().getExtras().getInt("Position");
            sCategory =getIntent().getExtras().getString("Category");
            if(sCategory.equals("drink")){
                sImageURL = MainActivity.sCategoriesDrinks.get(position).getImageURL();
                sName =MainActivity.sCategoriesDrinks.get(position).getName();
            }
            else{
                sImageURL = MainActivity.sCategoriesFood.get(position).getImageURL();
                sName =MainActivity.sCategoriesFood.get(position).getName();
            }

        }
        Picasso.with(getApplicationContext()).load(sImageURL).into((ImageView)findViewById(R.id.category_detail_image));
        if (sCategory.equals("drink"))
            getCategoryDrink(sName);
        else
            getCategoryFood(sName);
        getSupportActionBar().setTitle(sName.toUpperCase());


        mRootView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mAdapter = new CategoryDetailAdapter(sListItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(mAdapter);

        mRootView.addOnItemTouchListener(new RecyclerTouchListener(this, mRootView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FoodDrinkModel item = sListItems.get(position);
                Intent intent = new Intent(getBaseContext(),ProductDetailActivity.class);
                int index=CheckoutActivity.containsElement(item.getName());
                    if (index>=0 ){
                        int quantity = CheckoutActivity.sBasket.get(index).getQuantity();
                        intent.putExtra("Quantity",quantity);
                    }
                intent.putExtra("Name",item.getName());
                intent.putExtra("ImageURL", item.getImageURL());
                intent.putExtra("Category", sName);
                intent.putExtra("Price",item.getPrice());
                startActivity(intent);
            }
        }) );


        //Basket floating action buttton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CheckoutActivity.class);
                startActivity(intent);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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




    private static class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailViewHolder> {
        private List<FoodDrinkModel> items;
        private Context mContext;

        CategoryDetailAdapter(List<FoodDrinkModel> items) {
            this.items = items;

        }

        @Override
        public CategoryDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_card, viewGroup, false);
            mContext=viewGroup.getContext();
            return new CategoryDetailViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CategoryDetailViewHolder categoryDetailViewHolder, int i) {
            FoodDrinkModel item = items.get(i);
            categoryDetailViewHolder.categoryItemName.setText(item.getName());
            Picasso.with(mContext).load(item.getImageURL()).into(categoryDetailViewHolder.categoryItemImage);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class CategoryDetailViewHolder extends RecyclerView.ViewHolder {
        TextView categoryItemName;
        ImageView categoryItemImage;
        CategoryDetailViewHolder(View itemView) {
            super(itemView);
            categoryItemName=(TextView) itemView.findViewById(R.id.list_item_name);
            categoryItemImage=(ImageView) itemView.findViewById(R.id.list_item_image);
        }
    }

    private void getCategoryFood(String category){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("menus").child(sObjectID).child("categories").child("foodCategory").child(category);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    getCategoryDetails("food",category,data);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getCategoryDrink(String category){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("menus").child(sObjectID).child("categories").child("drinkCategory").child(category);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    getCategoryDetails("drinks",category,data);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getCategoryDetails(String foodOrDrink,String category,DataSnapshot snapshot){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(foodOrDrink).child("Category").child(category).child(snapshot.getKey());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FoodDrinkModel foodDrinkModel = dataSnapshot.getValue(FoodDrinkModel.class);
                foodDrinkModel.setPrice(snapshot.getValue(Integer.class));
                sListItems.add(foodDrinkModel);
                mRootView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }






}
