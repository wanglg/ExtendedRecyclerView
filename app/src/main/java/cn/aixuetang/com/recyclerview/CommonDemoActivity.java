package cn.aixuetang.com.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.leowong.extendedrecyclerview.ExtendedRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.aixuetang.com.recyclerview.widgets.adapters.GridCommonAdapter;

/**
 * User: wanglg
 * Date: 2016-07-20
 * Time: 10:47
 * FIXME
 */
public class CommonDemoActivity extends AppCompatActivity {
    private ExtendedRecyclerView mRecycler;
    private GridCommonAdapter gridCommonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        mRecycler = (ExtendedRecyclerView) findViewById(R.id.list);
        gridCommonAdapter = new GridCommonAdapter(getList(15));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(gridCommonAdapter);
    }

    public List<String> getList(int length) {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            datas.add(i + " str");
        }
        return datas;
    }
}
