import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.king.applib.R
import org.jetbrains.annotations.NotNull

/**
 * RecyclerView Item 点击事件工具类。
 * 
 * @author huoguangxu
 * @since 2019/11/13.
 */
class ItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    private val childListenerMap = hashMapOf<Int, OnChildClickListener>()

    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            mOnItemClickListener?.let {
                view.setOnClickListener {
                    val holder = mRecyclerView.findContainingViewHolder(it)
                    if (holder != null && holder.adapterPosition != -1) {
                        mOnItemClickListener?.onItemClicked(mRecyclerView, holder.adapterPosition, it)
                    }
                }
            }

            if (childListenerMap.isNotEmpty()) {
                for (key in childListenerMap.keys) {
                    view.findViewById<View>(key)?.setOnClickListener {
                        val holder = mRecyclerView.findContainingViewHolder(it)
                        if (holder != null && holder.adapterPosition != -1) {
                            childListenerMap[key]?.onChildClicked(mRecyclerView, holder.adapterPosition, it)
                        }
                    }
                }
            }

            mOnItemLongClickListener?.let {
                view.setOnLongClickListener(View.OnLongClickListener {
                    if (mOnItemLongClickListener != null) {
                        val holder = mRecyclerView.getChildViewHolder(it)
                        return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(mRecyclerView, holder.adapterPosition, it)
                    }
                    false

                })
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }


    fun addOnChildClickListener(@IdRes resId: Int, @NotNull listener: OnChildClickListener): ItemClickSupport {
        childListenerMap[resId] = listener
        return this
    }

    fun addOnItemClickListener(@NotNull listener: OnItemClickListener): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun addOnItemLongClickListener(@NotNull listener: OnItemLongClickListener): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    // 子 View点击接口
    interface OnChildClickListener {
        fun onChildClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    // 点击接口
    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    // 长按接口
    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, v: View): Boolean
    }

    companion object {

        fun addTo(view: RecyclerView): ItemClickSupport {
            var support: ItemClickSupport? = view.getTag(R.id.item_click_support) as ItemClickSupport?
            if (support == null) {
                support = ItemClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport
            support.detach(view)
            return support
        }
    }
}