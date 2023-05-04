package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExhibitionCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_category);
        ListView listExhibitions = (ListView) findViewById(R.id.list_exhibitions);

        ArrayAdapter<Exhibition> listAdapter = new ArrayAdapter<Exhibition>(this, android.R.layout.simple_list_item_1, Exhibition.exhibitions);

        //listExhibitions.setAdapter(listAdapter);
        listExhibitions.setOnItemClickListener(itemClickListener);
    }

    //listener for the items
    AdapterView.OnItemClickListener itemClickListener =
            new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> listCoffees, View itemView, int position, long id){
                    Intent intent = new Intent (ExhibitionCategoryActivity.this, ExhibitionActivity.class);
                    intent.putExtra(ExhibitionActivity.EXTRA_EXHIBITIONID, (int)id);

                    startActivity(intent);
                }
            };
}