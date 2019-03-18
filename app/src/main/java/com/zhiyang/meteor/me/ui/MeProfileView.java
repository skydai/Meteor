package com.zhiyang.meteor.me.ui;

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
import com.zhiyang.meteor.common.network.json.Tag;
import com.zhiyang.meteor.common.network.json.User;
import com.zhiyang.meteor.common.ui.adapter.MiniTagAdapter;
import com.zhiyang.meteor.common.ui.widget.rippleButton.RippleButton;
import com.zhiyang.meteor.common.utils.helper.IntentHelper;
import com.zhiyang.meteor.common.utils.manager.AuthManager;
import com.zhiyang.meteor.common.utils.AnimUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Me profile view.
 *
 * This view is used to show application's profile.
 *
 * */

public class MeProfileView extends FrameLayout {

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

    private List<Tag> tags;
    @ProfileViewStateRule private int state;

    public static final int STATE_NORMAL = 1;
    public static final int STATE_LOADING = -1;
    @IntDef({STATE_NORMAL, STATE_LOADING})
    private @interface ProfileViewStateRule {}

    public MeProfileView(Context context) {
        super(context);
        this.init();
    }

    public MeProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public MeProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        rippleButton.setState(RippleButton.State.ON);
        rippleButton.setOnSwitchListener(current -> {
            if (AuthManager.getInstance().isAuthorized()) {
                IntentHelper.startMyFollowActivity(Mysplash.getInstance().getTopActivity());
            }
        });
    }

    // control.

    @SuppressLint("SetTextI18n")
    public void drawMeProfile(User u) {
        if (tags == null
                || (u.tags != null && !isSameTags(u.tags.custom, tags))) {
            if (u.tags == null || u.tags.custom == null || u.tags.custom.size() == 0) {
                tagList.setVisibility(GONE);
            } else {
                tags = u.tags.custom;
                tagList.setAdapter(new MiniTagAdapter(u.tags.custom));
            }
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

        rippleButton.setButtonTitles(
                getContext().getString(R.string.my_follow).toUpperCase(),
                getContext().getString(R.string.my_follow).toUpperCase());
        rippleButton.setState(RippleButton.State.ON);

        setState(STATE_NORMAL);
    }

    private boolean isSameTags(List<Tag> a, List<Tag> b) {
        if ((a == null || a.size() == 0) && (b == null || b.size() == 0)) {
            return true;
        } else if (a != null && a.size() != 0 && b != null && b.size() != 0 && a.size() == b.size()) {
            for (int i = 0; i < a.size(); i ++) {
                if (!TextUtils.equals(a.get(i).getTitle(), b.get(i).getTitle())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
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
}
