## Description##
1、多状态切换功能,ExtendedRecyclerView继承[MultiStateLayout][],拥有MultiStateLayout的所有功能,使用方法请参照demo
[MultiStateLayout]: https://github.com/wanglg/MultiStateLayout
2、封装了一个CommonAdapter便于快速编写RecyclerView的Adapter,我们只需要实现
```
     public abstract int getLayoutId(int viewType);//返回item 布局id
        
     public abstract onBindViewHolder(CommonViewHolder holder, int position);//给item绑定数据
```
具体怎么写可以看demo代码,很简单的封装让你的adapter不超过50行代码

3、加载更多改用LoadMoreAdapter方式实现,原理是根据getLayoutId的参数不同来返回不同的layout来实现的，我们可以通过setPageCount或构造方法来自定义每页item数量，加载更多回调是通过给LoadMoreAdapter设置ILoadMoreCallback回调
```
     public interface ILoadMoreCallback {
        void loadMore(CommonViewHolder holder, int position);
     }
```
参数position为即将要加载下一页的第一个item的position

4、完全可以自定义下拉刷新控件，ExtendedRecyclerView默认内部集成v4的SwipeRefreshLayout来做下拉刷新，如果你项目不是用SwipeRefreshLayout来做下拉刷新，你可以通过ExtendedRecyclerView的recyclerSwipe属性来指向你自己的下拉刷新控件，这里需要下拉刷新控件实现ExtendedRecyclerView.PullToRefreshHandler接口，具体可以参照CustomSwipeRefreshGridLayoutActivity来修改下拉刷新效果。

5、增加LoadMoreWrapper可以从原有adapter无改变代码支持加载更多

## 效果Demo##
![image](https://github.com/wanglg/resource/blob/master/Default.gif)
![image](https://github.com/wanglg/resource/blob/master/Custom.gif)
## Using Gradle##
```
       repositories {
            maven {
                url 'https://dl.bintray.com/leowong/maven'
            }
        }
    
        compile 'com.leowong.library:extendedrecyclerview:1.0.5'
```
