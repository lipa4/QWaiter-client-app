package com.tvz.tomislav.qwaiter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tvz.tomislav.qwaiter.firebase.models.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener,CategoryPageFragment.OnFragmentInteractionListener {

    public static final int RC_SIGN_IN = 1;
    public static final int RC_SCAN = 2;
    public static final String ANONYMOUS = "anonymous";
    public static int sTAB_COUNT;
    public static List<Category> sCategoriesFood;
    public static List<Category> sCategoriesDrinks;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String mUsername=null;
    public static String sObjectID=null;
    private DrawerLayout mDrawerLayout;
    private ImageView mProfileImage;
    private int mMaxScrollSize;
    private FirebaseUser mFirebaseUser;
    private NavigationView mNavigationView;
    private Object mObject;
    private DataSnapshot mDataSnapshot;
    private TextView mCafeName;
    private TextView mCafeCategory;
    private ImageView mCafeAvatar;
    private ImageView mCafeBackground;
    public static String sObjectPath=null;
    public static int sTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(sObjectPath==null){
            String readValue = getIntent().getExtras().getString("readValue");
            if (readValue.substring(0, 3).equals("res")) {
                sTAB_COUNT = 2;
                sObjectPath = "restaurants";
            } else {
                sTAB_COUNT = 1;
                sObjectPath = "bars";
            }
            Log.d("readValue:", readValue);
            sObjectID = readValue.substring(6);
            Log.d("sObjectID: ",sObjectID);
            sTable = Character.getNumericValue(readValue.charAt(4));
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        //Listener for collapsing toolbar layout
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        sCategoriesDrinks=new ArrayList<>();
        sCategoriesFood=new ArrayList<>();

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        //Firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {

                    onSignedInInitialize(mFirebaseUser.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
//                                           , new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                                            ))
                                    .setLogo(R.drawable.logo)
                                    .setTheme(R.style.FirebaseUI)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };


        //Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CheckoutActivity.class);
                startActivity(intent);

            }
        });

        //Navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mCafeName = (TextView) findViewById(R.id.cafe_name);
        mCafeCategory=(TextView)findViewById(R.id.cafe_category);
        mCafeAvatar = (ImageView) findViewById(R.id.materialup_profile_image);
        mCafeBackground = (ImageView) findViewById(R.id.materialup_profile_backdrop);
        setImagesAndUserProfile();

        ImageView profilePhoto =(ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_photo);

        //new DownloadImageTask(profilePhoto,cafeAvatar,cafeBackground).execute(mFirebaseAuth.getCurrentUser().getPhotoUrl().toString(),imageURLs[0],imageURLs[1]);
        if (mFirebaseAuth.getCurrentUser().getPhotoUrl()!=null)
            Picasso.with(getBaseContext()).load(mFirebaseAuth.getCurrentUser().getPhotoUrl().toString()).into(profilePhoto);
        setProfileDataToNav();






        //Status bar -> transparent
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }




    private void setImagesAndUserProfile() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(sObjectPath).child(sObjectID).child("meta-data");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mObject =dataSnapshot.getValue(Object.class);
                mCafeName.setText(mObject.getName());
                mCafeCategory.setText(mObject.getObjectCategory());
                Picasso.with(getBaseContext()).load(mObject.getObjectAvatarImageURL()).fit().into(mCafeAvatar);
                Picasso.with(getBaseContext()).load(mObject.getObjectBackgroundImageURL()).fit().into(mCafeBackground);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    //Hiding and showing avatar while collapsing toolbar
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    private void setProfileDataToNav(){

        TextView userName = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        TextView mail = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_user_mail);
        userName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
        mail.setText(mFirebaseAuth.getCurrentUser().getEmail());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Intent intent = new Intent(this,ScanActivity.class);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    private void onSignedInInitialize(String username) {
        mUsername = username;
//        attachDatabaseReadListener();

    }
    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        sObjectPath=null;
       /* mMessageAdapter.clear();
        detachDatabaseReadListener();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        /*mMessageAdapter.clear();
        detachDatabaseReadListener();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private static class TabsAdapter extends FragmentPagerAdapter {

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return sTAB_COUNT;
        }

        @Override
        public Fragment getItem(int i) {
            return CategoryPageFragment.newInstance(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabTitle;
            if(position==0)
                tabTitle="DRINKS";
            else
                tabTitle="FOOD";
            return tabTitle;
        }
    }

    public static void getDrinkCategories(RecyclerView recyclerView){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("menus").child(sObjectID).child("categories").child("drinkCategory");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Category category = new Category();
                    category.setName(data.getKey());
                    sCategoriesDrinks.add(category);
                    getCategoryImage(data.getKey(),"drinks", sCategoriesDrinks.size()-1,recyclerView);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public static void getFoodCategories(RecyclerView recyclerView){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("menus").child(sObjectID).child("categories").child("foodCategory");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Category category = new Category();
                    category.setName(data.getKey());
                    sCategoriesFood.add(category);
                    getCategoryImage(data.getKey(),"food", sCategoriesFood.size()-1,recyclerView);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public static void getCategoryImage(String categoryName,String drinkOrFood, int position,RecyclerView recyclerView){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(drinkOrFood).child("Category").child(categoryName).child("ImageURL");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (drinkOrFood.equals("drinks"))
                    sCategoriesDrinks.get(position).setImageURL(dataSnapshot.getValue(String.class));
                else
                    sCategoriesFood.get(position).setImageURL(dataSnapshot.getValue(String.class));
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
