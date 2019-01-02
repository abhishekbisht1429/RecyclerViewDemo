package com.example.toni.recyclerviewdemo;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainACtivity";
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view_activity_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        //this retrieves the FirebaseDatabsae object
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //it retrieves an reference to the root of the realtime-database
        final DatabaseReference ref = database.getReference().child("dataList");

        //value event listener first calls when it is attached to the reference
        //after that whenever there is a change in the data it gets fired automatically

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    list.add((String)ds.getValue());
                    Log.i(TAG,ds.getChildren().toString());
                }
                recyclerViewAdapter.setDataList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        };
        ref.addValueEventListener(valueEventListener);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private List<String> dataList = new ArrayList<>();
        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_layout_recycler_view,viewGroup,false);
            Log.i(TAG,"onCreateViewHolder called");
            return new ItemViewHolder(view);
        }

        //bind the data with the view
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int index) {
            //get data from the datalist
            String data = dataList.get(index);

            itemViewHolder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        void setDataList(List<String> dataList) {
            if(dataList!=null) {
                this.dataList = dataList;
                notifyDataSetChanged();
            }
        }

        void addData(String data) {
            if(data!=null) {
                dataList.add(data);
                //this function will
                notifyItemInserted(dataList.size()-1);
            }
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_recycler_view);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void bindData(String data) {
            textView.setText(data);
        }
    }
}
