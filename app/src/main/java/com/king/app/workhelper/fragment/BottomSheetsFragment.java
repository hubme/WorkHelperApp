package com.king.app.workhelper.fragment;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.ui.dialog.FullSheetDialogFragment;
import com.king.applib.log.Logger;
import com.king.applib.util.ExtendUtil;

import butterknife.OnClick;

/**
 * BottomSheets控件配合NestedScrollView、RecyclerView使用效果会更好
 *
 * @author VanceKing
 * @since 2017/6/19 0019.
 */

public class BottomSheetsFragment extends AppBaseFragment {
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_bottom_sheets;
    }

    @Override
    protected void initContentView(View rootView) {
        super.initContentView(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(ExtendUtil.dp2px(100));
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet状态的改变
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING://被拖拽状态
                        Logger.i("STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING://拖拽松开之后到达终点位置（collapsed or expanded）前的状态。
                        Logger.i("STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED://完全展开的状态。
                        Logger.i("STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED://折叠关闭状态。可通过app:behavior_peekHeight来设置显示的高度,peekHeight默认是0。
                        Logger.i("STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN://隐藏状态。默认是false，可通过app:behavior_hideable属性设置。
                        Logger.i("STATE_HIDDEN");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });
    }

    @OnClick(R.id.tv_show_bottom_sheets)
    public void onBottomSheetsClick() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @OnClick(R.id.tv_bottom_sheets_dialog)
    public void onBottomSheetsDialogClick() {
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_bottom_sheets, null);
        dialog.setContentView(view);
        dialog.show();
    }

    @OnClick(R.id.tv_bottom_sheets_fragment)
    public void onBottomSheetsFragmentClick() {
        new FullSheetDialogFragment().show(getFragmentManager(), "SheetDialogFragment");
    }
}
