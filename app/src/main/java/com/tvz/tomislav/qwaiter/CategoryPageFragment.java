package com.tvz.tomislav.qwaiter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.tvz.tomislav.qwaiter.MainActivity.getDrinkCategories;
import static com.tvz.tomislav.qwaiter.MainActivity.getFoodCategories;
import static com.tvz.tomislav.qwaiter.MainActivity.sCategoriesDrinks;
import static com.tvz.tomislav.qwaiter.MainActivity.sCategoriesFood;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryPageFragment extends Fragment  {

    public RecyclerView mRootView;
    private OnFragmentInteractionListener mListener;
    private int mPosition;


    public CategoryPageFragment() {
        // Required empty public constructor

    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryPageFragment.
     */
    public static CategoryPageFragment newInstance(int position) {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            initRecyclerView();
        }

        private void initRecyclerView() {
            int position =this.getArguments().getInt("position");

            if (position==0) {
                getDrinkCategories(mRootView);
                mRootView.setAdapter(new CatergoryAdapter(sCategoriesDrinks));
                mRootView.addOnItemTouchListener(new RecyclerTouchListener(this, mRootView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getContext(),CategoryDetailActivity.class);
                        intent.putExtra("Position",position);
                        intent.putExtra("Category","drink");
                        startActivity(intent);
                    }
                }) );
            }
            else {
                getFoodCategories(mRootView);
                mRootView.setAdapter(new CatergoryAdapter(sCategoriesFood));
                mRootView.addOnItemTouchListener(new RecyclerTouchListener(this, mRootView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getContext(),CategoryDetailActivity.class);
                        intent.putExtra("Position",position);
                        intent.putExtra("Category","food");
                        startActivity(intent);
                    }
                }) );
            }

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = (RecyclerView) inflater.inflate(R.layout.fragment_page, container, false);
            return mRootView;
        }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface ClickListener{
         void onClick(View view,int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class CatergoryAdapter extends RecyclerView.Adapter<CatergoryViewHolder> {
        private List<Category> categories;
        private Context mContext;

        CatergoryAdapter(List<Category> categories) {
            this.categories=categories;

        }

        @Override
        public CatergoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_card, viewGroup, false);
            mContext=viewGroup.getContext();
            return new CatergoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CatergoryViewHolder catergoryViewHolder, int i) {
            Category category = categories.get(i);
            catergoryViewHolder.categoryName.setText(category.getName());
            Picasso.with(mContext).load(category.getImageURL()).into(catergoryViewHolder.categoryImage);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }

    public static class CatergoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryImage;
        CatergoryViewHolder(View itemView) {
            super(itemView);
            categoryName=(TextView) itemView.findViewById(R.id.list_item_name);
            categoryImage=(ImageView) itemView.findViewById(R.id.list_item_image);
        }
    }

     class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(CategoryPageFragment context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
