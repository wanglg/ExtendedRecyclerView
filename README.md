## Description##
ExtendedRecyclerView支持下拉刷新，空数据页面、进度条、错误等多状态页面切换，加载更多改用LoadMoreAdapter方式实现,封装了一个CommonAdapter便于快速编写RecyclerView的Adapter,我们只需要实现
```
     public abstract int getLayoutId(int viewType);//返回item 布局id
        
     public abstract onBindViewHolder(CommonViewHolder holder, int position);//给item绑定数据
```
具体怎么写可以看demo代码,很简单的封装让你的adapter不超过50行代码
加载更多的原理是根据getLayoutId的参数不同来返回不同的layout来实现的，我们可以通过setPageCount或构造方法来自定义每页item数量，加载更多回调是通过给LoadMoreAdapter设置ILoadMoreCallback回调
```
     public interface ILoadMoreCallback {
        void loadMore(CommonViewHolder holder, int position);
     }
```
参数position为即将要加载下一页的第一个item的position
## 效果Demo##
![image](https://github.com/wanglg/resource/blob/master/20150630112733.png)
## Using Gradle##
      ```
       repositories {
            maven {
                url 'https://dl.bintray.com/leowong/maven'
            }
        }
     ```

     ```
        compile 'com.leowong.library:extendedrecyclerview:1.0.2'
     ```
