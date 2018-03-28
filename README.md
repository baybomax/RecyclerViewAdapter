# MultiRecycleViewAdapter
A recycleView XAdapter repository includes animation/draggable/section/multi item .etc multi type XAdapter to match or suit to recycle view.

[![](https://www.jitpack.io/v/baybomax/RecyclerViewAdapter.svg)](https://www.jitpack.io/#baybomax/RecyclerViewAdapter)

# How to use


	//project
	allprojects {
		repositories {
			...
			jcenter()
			maven { url "https://jitpack.io" }
		}
	}

	or
	
	//application
	repositories {
    	...
    	maven {
			url "https://jitpack.io"
    	}
	}

	dependencies {
		...
		implementation 'com.github.baybomax:MultiRecycleViewAdapter:1.4.0'
	}

# Doc

LoadMoreView:

    1.'loadMoreView' - Set this use custom view, default is SimpleLoadMoreView.
    2.'setLoadMoreRequestListener' - Use this method set listener will called when load more data.
    3.'isLoadMoreViewEnable' - Control whether load more enable, default is false.
    4.'setAutoPreLoadCount' - Set the position what mean leave count to preload data, default is leave 1.
	...Once setLoadMoreRequestListener done, isLoadMoreViewEnable will be true, so if you want to use
	   loadMoreView simply, only need set this listener. Besides there is many methods with word 'loadMore'
	   you can good to use loadMoreView.

ClickListener:

    1.'onItemClickListener' - Set this will called when item clicked.
    2.'onItemLongClickListener' - Set this will called when item long clicked.
    3.'onItemChildClickListener' - Set this will called when child item clicked.
    4.'onItemChildLongClickListener' - Set this will called when child item long clicked.

Animation:

    1.'animation' - This is the animation of item view, there is five base animation i write you can 
			use[AlphaInAnimation, ScaleInAnimation, SlideInLeftAnimation, SlideInRightAnimation,SlideInBottomAnimation], 
			you can also use custom view which extends BaseAnimation,default is null, no animation.

UpFetch:

    1.'setUpFetchListener' - Use this method set listener will called when up fetch data.
    2.'setStartUpFetchPosition' - Set the position where start up fetch data, default is 1.
    3.'isUpFetchEnable' - Control whether up fetch enable, default is false.
    4.'isUpFetching' - Notify that whether is up fetching.

EmptyView:

    1.'setEmptyView' - Set empty view provide by custom more than one time to do what you want, default is null.
    2.'isHeaderEnableWhenEmpty' - Control whether header enable when set empty view.
    3.'isFooterEnableWhenEmpty' - Control whether footer enable when set empty view.

Header/FooterView:

    To operate the header/footer view.
    1.'addHeaderView'
    2.'setHeaderView'
    3.'removeHeaderView'
    4.'addFooterView'
    5.'setFooterView'
    6.'removeFooterView'

GridLayout:

    1.'setSpanSizeLookup' - Set the span size according to the span count of your GridLayoutView, only
                            use with GridLayoutView, others does not work.

DataOperation:

    Follow methods are operate the data source defined in adapter, you can find them in 'BaseAdapter'
    1.'add'
    2.'remove'
    3.'set'
    4.'get'
    5.'notify'

MultiType:

    1.'BaseMultiTypeAdapter' - Use this adapter can easy implements multi type view, you only need extend
                               it and add item type use func 'addItemType', the entity you used must
                               extends 'MultiType'.

Expandable:

    1.'BaseMultiTypeAdapter' - Just extend this adapter or you can extend BaseAdapter to make custom,
                               then according to itemViewType and use func collapse/expand .etc in BaseAdapter
                               to operate the expandable list.
                               ...The entity used must extends 'AbstractExpandable' or implements 'IExpandable'.

Section:

    1.'BaseSectionAdapter' - Simply extend this adapter to implementation, override abstract method.

Drag/Swipe:

    1.'BaseItemDraggableAdapter' - Extends it to implement is ok, if need, you can set 'OnItemDragListener'
                                   or 'OnItemSwipeListener' with 'ItemDragAndSwipeCallback', need other
                                   configs please see the code in 'app/ItemDragAndSwipeUseActivity'.
				   
If you need item swipe with menu, you can have a look with my another libray.
[![](https://www.jitpack.io/v/baybomax/SwipeMenuLayout.svg)](https://www.jitpack.io/#baybomax/SwipeMenuLayout)

# Example
	Please clone to local, the example case in app moudle may satisfy your need, and the library named 'multiadapter'
	you can use immediately.
	
# Concluding
	If you like, you can have a star, or any questions please give me issues.
	Also can inquire into kotlin or others with me if you interest in what.
