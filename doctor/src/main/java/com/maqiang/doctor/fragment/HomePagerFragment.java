package com.maqiang.doctor.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import cn.bmob.v3.BmobUser;
import com.google.android.flexbox.FlexboxLayout;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.activity.EditPersionalInfo;
import com.maqiang.doctor.adapter.BaseRecyclerAdapter;
import com.maqiang.doctor.adapter.BloodAdapter;
import com.maqiang.doctor.adapter.RecommondAdapter;
import com.maqiang.doctor.activity.InfoDetailAty;
import com.maqiang.doctor.activity.WriteRecpieActivity;
import com.maqiang.doctor.bean.Advice;
import com.maqiang.doctor.bean.Baby;
import com.maqiang.doctor.bean.Blood;
import com.maqiang.doctor.bean.InfoItem;
import com.maqiang.doctor.bean.InfoList;
import com.maqiang.doctor.bean.Recipe;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.net.APIGenerator;
import com.maqiang.doctor.net.APIService;
import com.maqiang.doctor.rxadapter.rxcompat.SchedulersCompat;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ConstantUtil;
import com.maqiang.doctor.utils.DateUtils;
import com.maqiang.doctor.utils.DensityUtil;
import com.maqiang.doctor.utils.ToastUtils;
import com.maqiang.doctor.view.RootNestedScrollView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import io.reactivex.observers.DisposableObserver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * Created by maqiang on 2017/5/3.
 *
 * Function:
 */

public class HomePagerFragment extends Fragment implements BaseRecyclerAdapter.OnItemClickListener {

  public static final String TAG = "HomePagerFragment";

  @BindView(R.id.coord) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.nest) RootNestedScrollView mScrollView;
  @BindView(R.id.recycler_article) RecyclerView mRecyclerView;
  @BindView(R.id.recycler) RecyclerView mBloodRecyclerView;
  @BindView(R.id.progress) ProgressBar mProgressBar;
  @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
  @BindView(R.id.flexbox) FlexboxLayout mFlexboxLayout;
  @BindView(R.id.blood_exception) LinearLayout mLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.ctl) CollapsingToolbarLayout mCTL;
  @BindView(R.id.breakfast) TextView mBreakfast;
  @BindView(R.id.lunch) TextView mLunch;
  @BindView(R.id.dinner) TextView mDinner;
  @BindView(R.id.add) TextView mAdd;
  @BindView(R.id.remark) TextView mRemark;
  @BindView(R.id.rl_none) RelativeLayout mNone;
  @BindView(R.id.tv_none) TextView mTextView;
  @BindView(R.id.tv_babysay) TextView mBabySay;
  @BindView(R.id.fit) ImageView mFit;
  //@BindView(R.id.blood_tag) LinearLayout mTag;
  @BindString(R.string.date_none) String none;
  @BindString(R.string.breakfast_item) String breakfast;
  @BindString(R.string.lunch_item) String lunch;
  @BindString(R.string.dinner_item) String dinner;
  @BindString(R.string.add_item) String add;
  @BindArray(R.array.item) String[] items;

  public static final int REQUEST_REFRESH = 1;
  private int remarkSelectIndex = 0;
  private User mUser;
  private volatile String mDate;
  private RecommondAdapter mRecommondAdapter;
  private BloodAdapter mBloodAdapter;
  private List<InfoItem> mInfoItems = new ArrayList<InfoItem>();
  private List<Blood> mBloods = new ArrayList<Blood>();
  private List<Recipe> mLasteRecipe;
  private List<Advice> mLasteAdvice;
  private List<Blood> mLastBlood;
  private List<BloodAdapter.ItemHolder> mItemHolders = new ArrayList<>();
  private static final String WXAPP_ID = "wx988019db68e5fbd3";
  public static IWXAPI api;


  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.RECIPE_WHAT:
          mLasteRecipe = (List<Recipe>) msg.obj;

          if (mLasteRecipe == null || mLasteRecipe.size() == 0) {
            mBreakfast.setText(String.format(breakfast, none));
            mLunch.setText(String.format(lunch, none));
            mDinner.setText(String.format(dinner, none));
            mAdd.setText(String.format(add, none));
          } else {
            Recipe recipe = mLasteRecipe.get(0);
            if (TextUtils.isEmpty(recipe.getBreakfast())) {
              mBreakfast.setText(String.format(breakfast, none));
            } else {
              mBreakfast.setText(String.format(breakfast, recipe.getBreakfast()));
            }

            if (TextUtils.isEmpty(recipe.getAfternoon())) {
              mLunch.setText(String.format(lunch, none));
            } else {
              mLunch.setText(String.format(lunch, recipe.getAfternoon()));
            }

            if (TextUtils.isEmpty(recipe.getDinner())) {
              mDinner.setText(String.format(dinner, none));
            } else {
              mDinner.setText(String.format(dinner, recipe.getDinner()));
            }

            if (TextUtils.isEmpty(recipe.getAdd())) {
              mAdd.setText(String.format(add, none));
            } else {
              mAdd.setText(String.format(add, recipe.getAdd()));
            }
          }

          mBreakfast.setText(splitTextViewColor(mBreakfast, 0, 3, mBreakfast.getText().length(),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.text_gray),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.black_textcolor)));

          mLunch.setText(splitTextViewColor(mLunch, 0, 3, mLunch.getText().length(),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.text_gray),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.black_textcolor)));

          mDinner.setText(splitTextViewColor(mDinner, 0, 3, mDinner.getText().length(),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.text_gray),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.black_textcolor)));

          mAdd.setText(splitTextViewColor(mAdd, 0, 3, mAdd.getText().length(),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.text_gray),
              ContextCompat.getColor(MyApplication.getInstance(), R.color.black_textcolor)));

          break;
        case BmobManager.ADVICE_WHAT:
          mLasteAdvice = (List<Advice>) msg.obj;
          if (mLasteAdvice == null || mLasteAdvice.size() == 0) {
            mRemark.setText(none);
            mFlexboxLayout.removeAllViews();
            ImageView imageView = new ImageView(MyApplication.getInstance());
            imageView.setImageResource(R.drawable.tag);
            mFlexboxLayout.addView(imageView);
          } else {
            Advice advice = mLasteAdvice.get(0);
            mRemark.setText(advice.getContent());
            mFlexboxLayout.removeAllViews();
            ImageView imageView = new ImageView(MyApplication.getInstance());
            imageView.setImageResource(R.drawable.tag);
            mFlexboxLayout.addView(imageView);
            String[] strings = advice.getCheck().split(",");
            for (int i = 0; i < strings.length; i++) {
              TextView text = new TextView(MyApplication.getInstance());
              text.setText(strings[i]);
              text.setTextColor(
                  ContextCompat.getColor(MyApplication.getInstance(), R.color.check_textcolor));
              text.setTextSize(15);
              text.setPadding(17, 8, 0, 0);
              text.setGravity(Gravity.CENTER);
              mFlexboxLayout.addView(text);
            }
          }
          break;
        case BmobManager.BLOOD:
          mLastBlood = (List<Blood>) msg.obj;
          if (mLastBlood == null || mLastBlood.size() == 0) {
            mNone.setVisibility(View.VISIBLE);
            mTextView.setText(getResources().getString(R.string.blood_none));
            //mTag.setVisibility(View.GONE);
            return;
          } else {
            mNone.setVisibility(View.GONE);
            //mTag.setVisibility(View.VISIBLE);
            mBloods.clear();
            for (Blood blood : mLastBlood) {
              mBloods.add(blood);
            }
            mBloodAdapter.notifyDataSetChanged();
          }
          break;
        case BmobManager.BABYSAY:
          List<Baby> babies = (List<Baby>) msg.obj;
          if (babies == null || babies.size() == 0) {
            mBabySay.setText(getResources().getString(R.string.text));
          } else {
            Baby baby = babies.get(0);
            mBabySay.setText(baby.getContent());
          }
          break;
        case BmobManager.BLOOD_UPDATE:
          initRecipeAndRemark();
          break;
      }
      super.handleMessage(msg);
    }
  };

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View mView = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, mView);
    mUser = BmobUser.getCurrentUser(User.class);
    initHintImageView();
    initRecycleView();
    initRecipeAndRemark();
    return mView;
  }

  private void initHintImageView() {
    int state = MyApplication.getInstance().getBloodState(mUser);
    if (state == 0) {
      mFit.setImageResource(R.drawable.nice);
    } else if (state == 1) {
      mFit.setImageResource(R.drawable.high_blood);
    } else {
      mFit.setImageResource(R.drawable.low_blood);
    }

    if (MyApplication.getInstance().getFirstLogin(mUser) == 0 && TextUtils.isEmpty(
        mUser.getDuedate())) {
      openDueDate();
      MyApplication.getInstance().saveFirstLogin(1, mUser);
    }
  }

  /**
   * 从服务器抓取信息
   */
  private void initRecipeAndRemark() {
    if (mUser != null && !TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      BmobManager.getBloodSugar(mUser.getMobilePhoneNumber(), mHandler);
      BmobManager.getRecipe(mUser.getMobilePhoneNumber(), mHandler);
      BmobManager.getAdvice(mUser.getMobilePhoneNumber(), mHandler);
      if (!TextUtils.isEmpty(mUser.getDuedate())) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
          int day = getGapCount(sdf.parse(BmobManager.getToday()), sdf.parse(mUser.getDuedate()));
          if (day > 0 && day < 280) {
            int week = 40 - day / 7;
            BmobManager.getBaby(280 - day, mHandler);
            if (day % 7 == 0) {
              mDate = "孕第" + week + "周" + "+" + 7;
            } else {
              mDate = "孕第" + week + "周" + "+" + (7 - day % 7);
            }
          } else {
            if (day <= 0){
              mBabySay.setText(R.string.product_done);
            }
            mDate = "备孕中";
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }else {
        mBabySay.setText(R.string.undata);
      }
    }
  }

  private void updateRecipeAndRemark(String date) {
    if (mUser != null && !TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      BmobManager.getRecipe(mUser.getMobilePhoneNumber(), date, mHandler);
      BmobManager.getAdvice(mUser.getMobilePhoneNumber(), date, mHandler);
    }
  }

  /**
   * 获取两个日期之间的间隔天数
   */
  public static int getGapCount(Date startDate, Date endDate) {
    Calendar fromCalendar = Calendar.getInstance();
    fromCalendar.setTime(startDate);
    fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
    fromCalendar.set(Calendar.MINUTE, 0);
    fromCalendar.set(Calendar.SECOND, 0);
    fromCalendar.set(Calendar.MILLISECOND, 0);

    Calendar toCalendar = Calendar.getInstance();
    toCalendar.setTime(endDate);
    toCalendar.set(Calendar.HOUR_OF_DAY, 0);
    toCalendar.set(Calendar.MINUTE, 0);
    toCalendar.set(Calendar.SECOND, 0);
    toCalendar.set(Calendar.MILLISECOND, 0);

    return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000
        * 60
        * 60
        * 24));
  }

  /**
   * 初始化菜单
   */
  private void initRecycleView() {
    setHasOptionsMenu(true);
    mToolbar.setTitle("");
    ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

    mRecyclerView.setVisibility(View.INVISIBLE);
    mRecommondAdapter = new RecommondAdapter(getActivity(), mInfoItems, mRecyclerView);
    mBloodAdapter = new BloodAdapter(getActivity(), mBloods, mBloodRecyclerView);
    mBloodAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
      @Override public void onItemClick(View itemView, int position, Object item) {
        // TODO: 2017/5/21 点击事件
        updateRecipeAndRemark(mBloods.get(position).getDate());
        updateTipLayout(mBloods.get(position));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        try {
          day = getGapCount(sdf.parse(mBloods.get(position).getDate()),
              sdf.parse(mUser.getDuedate()));
          Log.d(TAG, "onScrollStateChanged: " + day);
          if (day > 0 && day < 280) {
            int week = 40 - day / 7;
            if (day % 7 == 0) {
              mDate = "孕第" + week + "周" + "+" + 7;
            } else {
              mDate = "孕第" + week + "周" + "+" + (7 - day % 7);
            }
            mCTL.setTitle(mDate);
          } else {
            mCTL.setTitle("备孕中");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    mRecommondAdapter.setOnItemClickListener(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mRecommondAdapter);
    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
        int visibleItemCount = recyclerView.getChildCount();

        if (newState == RecyclerView.SCROLL_STATE_IDLE
            && lastVisibleItemPosition == totalItemCount - 1
            && visibleItemCount > 0) {
          ToastUtils.show("已经没有更多内容了");
        }
      }
    });

    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
    layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
    mBloodRecyclerView.setLayoutManager(layoutManager2);
    mBloodRecyclerView.setNestedScrollingEnabled(false);
    mBloodRecyclerView.setAdapter(mBloodAdapter);

    mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() * 2 / 3) {
          if (mToolbar.getVisibility() == View.INVISIBLE) {
            mToolbar.setVisibility(View.VISIBLE);
            mCTL.setTitle(mDate);
          }
        } else {
          if (mToolbar.getVisibility() == View.VISIBLE) {
            mToolbar.setVisibility(View.INVISIBLE);
            mCTL.setTitle("");
          }
        }
      }
    });
    initInfoData(6);
  }

  /**
   * 更新提示信息
   */
  private void updateTipLayout(Blood blood) {
    mLayout.removeAllViews();
    String str = getString(R.string.blood_exception);

    //早餐前
    if (BloodAdapter.pre(blood.getPreBreakfastValue()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.pre(blood.getPreBreakfastValue()) == -1) {
        text.setText(String.format(str, items[0], blood.getPreBreakfastValue(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[0], blood.getPreBreakfastValue(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      mLayout.addView(view);
    }
    //早餐后
    if (BloodAdapter.after(blood.getAfterBreakfastValue()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.after(blood.getAfterBreakfastValue()) == -1) {
        text.setText(String.format(str, items[1], blood.getAfterBreakfastValue(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[1], blood.getAfterBreakfastValue(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }

    //午餐前
    if (BloodAdapter.pre(blood.getPreLunchValue()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.pre(blood.getPreLunchValue()) == -1) {
        text.setText(String.format(str, items[2], blood.getPreLunchValue(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[2], blood.getPreLunchValue(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }
    //午餐后
    if (BloodAdapter.after(blood.getAfterLunchValue()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.after(blood.getAfterLunchValue()) == -1) {
        text.setText(String.format(str, items[3], blood.getAfterLunchValue(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[3], blood.getAfterLunchValue(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }

    //晚餐前
    if (BloodAdapter.pre(blood.getPreDinner()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.pre(blood.getPreDinner()) == -1) {
        text.setText(String.format(str, items[4], blood.getPreDinner(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[4], blood.getPreDinner(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }
    //晚餐后
    if (BloodAdapter.after(blood.getAfterDinner()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.after(blood.getAfterDinner()) == -1) {
        text.setText(String.format(str, items[5], blood.getAfterDinner(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[5], blood.getAfterDinner(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }

    //睡前
    if (BloodAdapter.after(blood.getSleep()) != 0) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.blood_exception_item, null);
      TextView text = (TextView) view.findViewById(R.id.blood_text);
      ImageView image = (ImageView) view.findViewById(R.id.blood_pic);

      if (BloodAdapter.after(blood.getSleep()) == -1) {
        text.setText(String.format(str, items[6], blood.getSleep(), "低血糖"));
        image.setImageResource(R.drawable.bloodl);
      } else {
        text.setText(String.format(str, items[6], blood.getSleep(), "高血糖"));
        image.setImageResource(R.drawable.bloodh);
      }
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null) {
        parent.removeAllViews();
      }
      mLayout.addView(view);
    }
  }

  private void initInfoData(final int id) {
    APIGenerator.createService(APIService.class)
        .getInfo(ConstantUtil.JUHE_KEY, id, ConstantUtil.PAGE, ConstantUtil.LIMIT)
        .compose(SchedulersCompat.<InfoList>applyExecutorSchedulers())
        .subscribe(new DisposableObserver<InfoList>() {
          @Override public void onNext(InfoList value) {
            Log.d(TAG, "onNext: " + value);
            mInfoItems = value.getResult().getData();
            mRecommondAdapter.setIndex(id);
            mRecommondAdapter.setData(mInfoItems);
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
          }

          @Override public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            ToastUtils.show(R.string.data_failed);
          }

          @Override public void onComplete() {
          }
        });
  }

  @Override public void onItemClick(View itemView, int position, Object item) {
    Intent intent = new Intent(getActivity(), InfoDetailAty.class);
    intent.putExtra("info", mInfoItems.get(position));
    intent.putExtra("url", mInfoItems.get(position).getImg());
    intent.putExtra("flag", "0");
    //当Android5.0以开启共享动画
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
          new Pair<View, String>(itemView.findViewById(R.id.pic), "image"));
      ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    } else {
      //当Android5.0以下取消共享动画
      startActivity(intent);
    }
  }

  /**
   * 分割字符串 分为两部分 两种颜色
   */
  private SpannableStringBuilder splitTextViewColor(TextView textView, int start, int mid, int end,
      int leftColor, int rightColor) {
    SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());

    ForegroundColorSpan tips = new ForegroundColorSpan(leftColor);
    ForegroundColorSpan msg = new ForegroundColorSpan(rightColor);

    builder.setSpan(tips, start, mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    builder.setSpan(msg, mid, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    return builder;
  }

  @OnClick(R.id.share) public void shareToWeiXin(View view) {
    final AlertDialog dialog =
        new AlertDialog.Builder(getActivity()).setView(R.layout.sharetoweixin).create();
    dialog.show();
    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
    layoutParams.width = DensityUtil.dip2px(getActivity(), 266);
    dialog.getWindow().setAttributes(layoutParams);
    if (dialog.isShowing()) {
      dialog.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2017/5/31 微信分享
          dialog.dismiss();
          regTowx();
          //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.add);
          //
          //WXImageObject wxImageObject = new WXImageObject(bitmap);
          //WXMediaMessage mediaMessage = new WXMediaMessage();
          //mediaMessage.mediaObject = wxImageObject;
          //
          ////设置微缩图
          //Bitmap bm = Bitmap.createScaledBitmap(bitmap,120,120,true);
          //bitmap.recycle();
          //mediaMessage.thumbData = com.maqiang.doctor.utils.Util.bitmapToByteArray(bm);
          //
          //SendMessageToWX.Req req = new SendMessageToWX.Req();
          //req.transaction = BmobManager.getToday()+System.currentTimeMillis();
          //req.message = mediaMessage;
          //req.scene = SendMessageToWX.Req.WXSceneTimeline;
          //
          //api.sendReq(req);
          //
          //ToastUtils.show(api.sendReq(req)+"");

          WXTextObject textObject = new WXTextObject();
          textObject.text = "hello";
          WXMediaMessage mediaMessage = new WXMediaMessage();
          mediaMessage.mediaObject = textObject;
          mediaMessage.description = "hello";
          SendMessageToWX.Req req = new SendMessageToWX.Req();
          req.transaction = BmobManager.getToday()+System.currentTimeMillis();
          req.message = mediaMessage;
          req.scene = SendMessageToWX.Req.WXSceneTimeline;

          api.sendReq(req);

        }
      });
    }
  }

  @OnClick({ R.id.advice_next, R.id.recipe_next }) public void recordRecipeAndRemark(View view) {
    Intent intent = new Intent(getActivity(), WriteRecpieActivity.class);
    this.startActivityForResult(intent, REQUEST_REFRESH);
  }

  @OnClick(R.id.update_article) public void randomArticle(View view) {
    int[] values = new int[] { 2, 3, 5, 6, 8 };
    int random = new Random().nextInt(5);
    initInfoData(values[random]);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_REFRESH && resultCode == RESULT_OK) {
      mHandler.postDelayed(new Runnable() {
        @Override public void run() {
          initRecipeAndRemark();
        }
      }, 300);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.toolbar, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit, null);
    TextInputEditText te = (TextInputEditText) view.findViewById(R.id.edit_avoid);
    te.setHint("请输入血糖值");
    te.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    final AlertDialog ad = new AlertDialog.Builder(getActivity()).setView(view)
        .setNegativeButton("取消", null)
        .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            TextInputEditText et = (TextInputEditText) view.findViewById(R.id.edit_avoid);
            final String value = et.getText().toString();
            if (!TextUtils.isEmpty(value)) {
              try {
                if (Float.valueOf(value) < 0f || Float.valueOf(value) > 15.0f) {
                  ToastUtils.show("请输入正确的血糖值");
                  return;
                }
              } catch (Exception e) {
                ToastUtils.show("请输入正确的血糖值");
              }
              AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("请选择当前的时间")
                  .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                      remarkSelectIndex = which;
                    }
                  })
                  .setNegativeButton("取消", null)
                  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                      Blood blood = new Blood();
                      if (mUser != null && mUser.getMobilePhoneNumber() != null) {
                        blood.setPhoneNumber(mUser.getMobilePhoneNumber());
                      }
                      blood.setDate(BmobManager.getToday());
                      switch (remarkSelectIndex) {
                        case 0:
                          blood.setPreBreakfastValue(Float.valueOf(value));
                          pre(Float.valueOf(value));
                          break;
                        case 1:
                          blood.setAfterBreakfastValue(Float.valueOf(value));
                          after(Float.valueOf(value));
                          break;
                        case 2:
                          blood.setPreLunchValue(Float.valueOf(value));
                          pre(Float.valueOf(value));
                          break;
                        case 3:
                          blood.setAfterLunchValue(Float.valueOf(value));
                          after(Float.valueOf(value));
                          break;
                        case 4:
                          blood.setPreDinner(Float.valueOf(value));
                          pre(Float.valueOf(value));
                          break;
                        case 5:
                          blood.setAfterDinner(Float.valueOf(value));
                          after(Float.valueOf(value));
                          break;
                        case 6:
                          blood.setSleep(Float.valueOf(value));
                          after(Float.valueOf(value));
                          break;
                      }
                      BmobManager.updateBloodSugarData(blood, remarkSelectIndex, mHandler);
                    }
                  })
                  .create();
              alertDialog.show();
            } else {
              ToastUtils.show("血糖值不能为空");
            }
          }
        })
        .create();
    ad.show();
    return super.onOptionsItemSelected(item);
  }

  private void pre(float value) {
    if (value >= 3.3 && value <= 5.8) {
      mFit.setImageResource(R.drawable.nice);
      MyApplication.getInstance().saveBloodState(0,mUser);
    } else if (value < 3.3) {
      mFit.setImageResource(R.drawable.low_blood);
      MyApplication.getInstance().saveBloodState(-1,mUser);
    } else if (value > 5.8) {
      MyApplication.getInstance().saveBloodState(1,mUser);
      mFit.setImageResource(R.drawable.high_blood);
    }
  }

  private void after(float value) {
    if (value >= 4.4 && value <= 7.8) {
      mFit.setImageResource(R.drawable.nice);
      MyApplication.getInstance().saveBloodState(0,mUser);
    } else if (value < 4.4) {
      mFit.setImageResource(R.drawable.low_blood);
      MyApplication.getInstance().saveBloodState(-1,mUser);
    } else if (value > 7.8) {
      mFit.setImageResource(R.drawable.high_blood);
      MyApplication.getInstance().saveBloodState(1,mUser);
    }
  }

  /**
   * 首次登陆弹出显示
   */
  private void openDueDate() {
    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit, null);
    final TextInputEditText te = (TextInputEditText) view.findViewById(R.id.edit_avoid);
    te.setHint("末次月经时间");
    te.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
    te.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog =
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                te.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
              }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
      }
    });
    AlertDialog ad = new AlertDialog.Builder(getActivity()).setView(view)
        .setNegativeButton("取消", null)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            String date = te.getText().toString();
            if (!TextUtils.isEmpty(date)) {
              try {
                mUser.setDuedate(
                    DateUtils.getDateDelay(DateUtils.stringToDate(date, "yyyy-MM-dd")));
                BmobManager.updata(mUser);
              } catch (ParseException e) {
                e.printStackTrace();
              }
            }
          }
        })
        .create();
    ad.show();
  }



  public Bitmap myShot(Activity activity) {
    // 获取windows中最顶层的view
    View view = activity.getWindow().getDecorView();
    view.buildDrawingCache();
    // 获取状态栏高度
    Rect rect = new Rect();
    view.getWindowVisibleDisplayFrame(rect);
    int statusBarHeights = rect.top;
    Display display = activity.getWindowManager().getDefaultDisplay();
    // 获取屏幕宽和高
    int widths = display.getWidth();
    int heights = display.getHeight();

    // 允许当前窗口保存缓存信息
    view.setDrawingCacheEnabled(true);
    //起始位置 截图大小的设定
    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
        statusBarHeights, widths, heights - statusBarHeights);
    // 销毁缓存信息
    view.destroyDrawingCache();
    return bmp;
  }

  private void regTowx(){
    api = WXAPIFactory.createWXAPI(getActivity(),WXAPP_ID,true);
    api.registerApp(WXAPP_ID);
  }

}
