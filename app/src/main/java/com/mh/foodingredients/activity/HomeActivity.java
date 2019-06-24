package com.mh.foodingredients.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.adapter.HomeRecyclerViewAdapter;
import com.mh.foodingredients.model.IngredientItem;

import java.util.ArrayList;

public class HomeActivity extends Fragment {

    //recyclerView
    RecyclerView mCloseDtRecyclerView;
    RecyclerView.Adapter mCloseDtRecyclerAdapter;
    RecyclerView.LayoutManager mCloseDtLayoutManager;

    RecyclerView mRunningDaysRecyclerView;
    RecyclerView.Adapter mRunningDaysRecyclerAdapter;
    RecyclerView.LayoutManager mRunningDaysLayoutManager;

    RecyclerView mLastSaveRecyclerView;
    RecyclerView.Adapter mLastSaveRecyclerAdapter;
    RecyclerView.LayoutManager mLastSaveLayoutManager;

    LinearLayout mCloseDtDefaultLayout;
    TextView mCloseDtEmptyTextView;
    LinearLayout mRunningDaysDefaultLayout;
    TextView mRunningDaysEmptyTextView;
    LinearLayout mLastSaveDefaultLayout;
    TextView mLastSaveEmptyTextView;

    ArrayList<IngredientItem> closeDtContents = new ArrayList<>();
    ArrayList<IngredientItem> runningDasContentss = new ArrayList<>();
    ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        //유통기한임박
        mCloseDtDefaultLayout = view.findViewById(R.id.defaultLayoutCloseDt);
        mCloseDtEmptyTextView = view.findViewById(R.id.emptyCloseDtTextView);

        mCloseDtRecyclerView = view.findViewById(R.id.closeDtRecyclerView);
        mCloseDtRecyclerView.setHasFixedSize(true);

        //경과일순
        mRunningDaysDefaultLayout = view.findViewById(R.id.defaultLayoutRunningDays);
        mRunningDaysEmptyTextView = view.findViewById(R.id.emptyRunningDaysTextView);

        mRunningDaysRecyclerView = view.findViewById(R.id.runningDaysRecyclerView);
        mRunningDaysRecyclerView.setHasFixedSize(true);

        //최근등록일
        mLastSaveDefaultLayout = view.findViewById(R.id.defaultLayoutLastSave);
        mLastSaveEmptyTextView = view.findViewById(R.id.emptyLastSaveTextView);

        mLastSaveRecyclerView = view.findViewById(R.id.lastSaveRecyclerView);
        mLastSaveRecyclerView.setHasFixedSize(true);

        // 데이터 유무체크
        if (items == null) {
            mCloseDtDefaultLayout.setVisibility(View.GONE);
            mCloseDtEmptyTextView.setVisibility(View.VISIBLE);

            mRunningDaysDefaultLayout.setVisibility(View.GONE);
            mRunningDaysEmptyTextView.setVisibility(View.VISIBLE);

            mLastSaveDefaultLayout.setVisibility(View.GONE);
            mLastSaveEmptyTextView.setVisibility(View.VISIBLE);

        } else {
            mCloseDtDefaultLayout.setVisibility(View.VISIBLE);
            mCloseDtEmptyTextView.setVisibility(View.GONE);

            mRunningDaysDefaultLayout.setVisibility(View.VISIBLE);
            mRunningDaysEmptyTextView.setVisibility(View.GONE);

            mLastSaveDefaultLayout.setVisibility(View.VISIBLE);
            mLastSaveEmptyTextView.setVisibility(View.GONE);

            mCloseDtLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            mCloseDtLayoutManager.scrollToPosition(items.size());
            mCloseDtRecyclerView.setLayoutManager(mCloseDtLayoutManager);

            mCloseDtRecyclerAdapter = new HomeRecyclerViewAdapter(getContext(), items);
            mCloseDtRecyclerView.setAdapter(mCloseDtRecyclerAdapter);

            //유통기한 adapter
            Query closeDtQuery = FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).child("ingredientItems").orderByChild("expireDate");
            closeDtQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        closeDtContents.add(item.getValue(IngredientItem.class));
                    }

                    mCloseDtRecyclerAdapter = new HomeRecyclerViewAdapter(getContext(), closeDtContents);
                    mCloseDtRecyclerView.setAdapter(mCloseDtRecyclerAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //경과일기준 adapter
            mRunningDaysLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            mRunningDaysLayoutManager.scrollToPosition(items.size() - 1);
            mRunningDaysRecyclerView.setLayoutManager(mRunningDaysLayoutManager);

            mRunningDaysRecyclerAdapter = new HomeRecyclerViewAdapter(getContext(), items);
            mRunningDaysRecyclerView.setAdapter(mRunningDaysRecyclerAdapter);

            Query mRunningDays = FoodIngreInfoApplication.mDatabase.child("users").child(FoodIngreInfoApplication.mUserItem.uid).child("ingredientItems").orderByChild("runningDays");
            mRunningDays.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        runningDasContentss.add(item.getValue(IngredientItem.class));
                    }

                    mRunningDaysRecyclerAdapter = new HomeRecyclerViewAdapter(getContext(), runningDasContentss);
                    mRunningDaysRecyclerView.setAdapter(mRunningDaysRecyclerAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //최근구입기준 adapter
            mLastSaveLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            mLastSaveLayoutManager.scrollToPosition(items.size() - 1);
            mLastSaveRecyclerView.setLayoutManager(mLastSaveLayoutManager);

            mLastSaveRecyclerAdapter = new HomeRecyclerViewAdapter(getContext(), items);
            mLastSaveRecyclerView.setAdapter(mLastSaveRecyclerAdapter);

        }
        return view;
    }

}

