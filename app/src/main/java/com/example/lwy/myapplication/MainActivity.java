package com.example.lwy.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lwy.paginationlib.PaginationRecycleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private PaginationRecycleView mPaginationRcv;
    private CustomAdapter mAdapter;
    private int[] perPageCountChoices = {10, 20, 30, 50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPaginationRcv = findViewById(R.id.pagination_rcv);
        mAdapter = new CustomAdapter(this, 99);
        mPaginationRcv.setAdapter(mAdapter);
        mPaginationRcv.setPerPageCountChoices(perPageCountChoices);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPaginationRcv.setLayoutManager(layoutManager);

        mPaginationRcv.setmListener(new PaginationRecycleView.Listener() {
            @Override
            public void loadMore(int currentPagePosition, int nextPagePosition, int perPageCount, int dataTotalCount) {
                mAdapter.setDatas(nextPagePosition, geneDatas(nextPagePosition, perPageCount));
                mPaginationRcv.setState(PaginationRecycleView.SUCCESS);
            }

            @Override
            public void onPerPageCountChanged(int perPageCount) {

            }
        });


    }

    public List<JSONObject> geneDatas(int currentPagePosition, int perPageCount) {
        int from = (currentPagePosition - 1) * perPageCount;
        List<JSONObject> datas = new ArrayList<>();
        try {
            for (int i = 0; i < perPageCount; i++) {
                JSONObject json = new JSONObject();

                json.put("name", "name<" + (from++) + ">");

                datas.add(json);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return datas;
    }

    class CustomAdapter extends PaginationRecycleView.Adapter<JSONObject, ViewHolder> {


        private Context mContext;

        public CustomAdapter(Context context, int dataTotalCount) {
            super(dataTotalCount);
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent,
                    false);
            return new ViewHolder(itemView);
        }

        @Override
        public void bindViewHolder(ViewHolder viewholder, JSONObject data) {
            viewholder.bindDatas(data);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);
        }

        public void bindDatas(JSONObject jsonObject) {
            mTextView.setText(jsonObject.optString("name"));
        }
    }


}