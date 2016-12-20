package com.king.app.workhelper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.app.workhelper.R;

/**
 * @author Allen Wang
 *
 */
public class SecondTabFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_second, container,
				false);
		return contextView;
	}

}
