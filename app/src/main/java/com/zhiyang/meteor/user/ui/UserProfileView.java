package com.zhiyang.meteor.user.ui;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.zhiyang.meteor.Mysplash;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.ui.adapter.MiniTagAdapter;
import com.zhiyang.meteor.common.ui.adapter.MyPagerAdapter;
import com.zhiyang.meteor.common.ui.widget.rippleButton.RippleButton;
import com.zhiyang.meteor.common.utils.AnimUtils;
import com.zhiyang.meteor.common.utils.DisplayUtils;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.common.utils.manager.AuthManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User profile view.
 *
 * This view is used to show user's profile.
 *
 * */

public class UserProfileView extends FrameLayout {

    @BindView(R.id.container_user_profile_progressView) CircularProgressView progressView;
    @BindView(R.id.container_user_profile_profileContainer) LinearLayout profileContainer;

    @BindView(R.id.container_user_profile_tagList) RecyclerView tagList;
    @BindView(R.id.container_user_profile_bio) TextView bioTxt;

    @BindView(R.id.container_user_profile_locationContainer) RelativeLayout locationContainer;
    @OnClick(R.id.container_user_profile_locationContainer) void clickLocation() {
        if (!TextUtils.isEmpty(locationTxt.getText())) {
            IntentHelper.startSearchActivity(
                    Mysplash.getInstance().getTopActivity(),
                    locationTxt.getText().toString());
        }
    }

    @BindView(R.id.container_user_profile_locationTxt) TextView locationTxt;
    @BindView(R.id.container_user_profile_followBtn) RippleButton rippleButton;

    private MyPagerAdapter adapter;

    private OnRippleButtonSwitchedListener listener;

    @ProfileViewStateRule private int state;

    public static final int STATE_NORMAL = 1;
    public static final int STATE_LOADING = -1;
    @IntDef({STATE_NORMAL, STATE_LOADING})
    private @interface ProfileViewStateRule {}

    public UserProfileView(Context context) {
        super(context);
        this.init();
    }

    public UserProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public UserProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    // init.

    @SuppressLint("InflateParams")
    private void init() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.container_user_profile, null);
        addView(v);
        ButterKnife.bind(this, this);

        state = STATE_LOADING;

        progressView.setVisibility(VISIBLE);
        profileContainer.setVisibility(GONE);

        tagList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (AuthManager.getInstance().isAuthorized() && Mysplash.hasNode()) {
            rippleButton.setOnSwitchListener(current -> {
                if (listener != null) {
                    listener.onRippleButtonSwitched(current != RippleButton.State.ON);
                    rippleButton.setState(current == RippleButton.State.ON
                            ? RippleButton.State.TRANSFORM_TO_OFF : RippleButton.State.TRANSFORM_TO_ON);
                }
            });
        } else {
            rippleButton.setVisibility(GONE);
        }
    }

    // control.

    public void setAdapter(MyPagerAdapter adapter) {
        this.adapter = adapter;
    }

    public void setRippleButtonState(User user) {
        if (user.settingFollow) {
            rippleButton.setState(user.followed_by_user
                    ? RippleButton.State.TRANSFORM_TO_OFF : RippleButton.State.TRANSFORM_TO_ON);
        } else {
            rippleButton.setState(user.followed_by_user
                    ? RippleButton.State.ON : RippleButton.State.OFF);
        }
    }

    @SuppressLint("SetTextI18n")
    public void drawUserInfo(User u) {
        if (u.tags == null || u.tags.custom == null || u.tags.custom.size() == 0) {
            tagList.setVisibility(GONE);
        } else {
            tagList.setAdapter(new MiniTagAdapter(u.tags.custom));
        }

        if (!TextUtils.isEmpty(u.bio)) {
            bioTxt.setText(u.bio);
        } else {
            bioTxt.setVisibility(GONE);
        }

        if (TextUtils.isEmpty(u.location)) {
            locationContainer.setVisibility(GONE);
        } else {
            locationTxt.setText(u.location);
        }

        setRippleButtonState(u);

        List<String> titleList = new ArrayList<>();
        titleList.add(
                DisplayUtils.abridgeNumber(u.total_photos)
                        + " " + getResources().getStringArray(R.array.user_tabs)[0]);
        titleList.add(
                DisplayUtils.abridgeNumber(u.total_likes)
                        + " " + getResources().getStringArray(R.array.user_tabs)[1]);
        titleList.add(
                DisplayUtils.abridgeNumber(u.total_collections)
                        + " " + getResources().getStringArray(R.array.user_tabs)[2]);
        adapter.titleList = titleList;
        adapter.notifyDataSetChanged();

        setState(STATE_NORMAL);
    }

    @ProfileViewStateRule
    public int getState() {
        return state;
    }

    public void setState(@ProfileViewStateRule int state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case STATE_LOADING:
                    AnimUtils.animShow(progressView);
                    AnimUtils.animHide(profileContainer);
                    break;

                case STATE_NORMAL:
                    AnimUtils.animShow(profileContainer);
                    AnimUtils.animHide(progressView);
                    break;
            }
        }
    }

    // interface.

    // on request user listener.

    public interface OnRippleButtonSwitchedListener {
        void onRippleButtonSwitched(boolean switchTo);
    }

    public void setOnRippleButtonSwitchedListener(OnRippleButtonSwitchedListener l) {
        this.listener = l;
    }
}
