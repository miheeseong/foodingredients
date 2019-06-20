package com.mh.foodingredients.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.adapter.SearchAdapter;
import com.mh.foodingredients.model.IngredientItem;

import java.util.ArrayList;

public class SearchActivity extends Fragment {

    ListView listView;          // 검색을 보여줄 리스트변수
    EditText editSearch;        // 검색어를 입력할 Input 창
    SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    ArrayList<IngredientItem> items = FoodIngreInfoApplication.mUserItem.ingredientItems;
    ArrayList<IngredientItem> viewItems = new ArrayList<>();


    Button searchButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

/*
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("ActionBar Title by setTitle()") ;
*/
        editSearch = view.findViewById(R.id.editSearch);
        listView =  view.findViewById(R.id.listView);


        if (FoodIngreInfoApplication.mUserItem.ingredientItems == null) {

        }else {
            // 리스트에 연동될 아답터를 생성한다.
            adapter = new SearchAdapter(getActivity(), viewItems);

            // 리스트뷰에 아답터를 연결한다.
            listView.setAdapter(adapter);

        }

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        return view;
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        viewItems.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            viewItems.addAll(items);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < items.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (items.get(i).ingredientName.toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    viewItems.add(items.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

}


