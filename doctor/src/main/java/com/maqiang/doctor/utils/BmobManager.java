package com.maqiang.doctor.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.activity.FoundPwdAty;
import com.maqiang.doctor.activity.LoginAty;
import com.maqiang.doctor.activity.RegisterAty;
import com.maqiang.doctor.bean.Advice;
import com.maqiang.doctor.bean.Avoid;
import com.maqiang.doctor.bean.Baby;
import com.maqiang.doctor.bean.Blood;
import com.maqiang.doctor.bean.InfoItem;
import com.maqiang.doctor.bean.Recipe;
import com.maqiang.doctor.bean.User;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.maqiang.doctor.activity.FoundPwdAty.NEXT;

/**
 * Created by maqiang on 2017/3/30.
 */

public class BmobManager {

  public static final int USER_WHAT = 1;
  public static final int RECIPE_WHAT = 2;
  public static final int ADVICE_WHAT = 3;
  public static final int FEEDBACK_WHAT = 4;
  public static final int COLLECT = 5;
  public static final int AVOID = 6;
  public static final int INFO = 7;
  public static final int BLOOD = 8;
  public static final int BABYSAY = 9;
  public static final int AVOID_DELETE = 10;
  public static final int AVOID_ADD = 11;
  public static final int BLOOD_UPDATE = 12;

  /**
   * 保存数据
   */
  public static <T extends BmobObject> void saveData(T object) {
    object.save(new SaveListener<String>() {
      @Override public void done(String s, BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.save_data_succeed);
        } else {
          ToastUtils.show(R.string.save_data_failed);
        }
      }
    });
  }

  public static <T extends BmobObject> void saveData(T object, final Handler handler) {
    object.save(new SaveListener<String>() {
      @Override public void done(String s, BmobException e) {
        if (e == null) {
          ToastUtils.show("收藏成功");
          Message message = handler.obtainMessage();
          message.what = INFO;
          message.arg1 = 1;
          handler.sendMessage(message);
        } else {
          ToastUtils.show("收藏失败");
        }
      }
    });
  }

  /**
   * 登录账号
   */
  public static <T extends BmobUser> void login(T object, final Handler handler) {
    object.login(new SaveListener<Object>() {
      @Override public void done(Object o, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.arg1 = 1;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.arg1 = 0;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 注册账号
   */
  public static <T extends BmobUser> void register(T object, final Context context) {
    object.signUp(new SaveListener<Object>() {
      @Override public void done(Object o, BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.register_succeed);
          Intent intent = new Intent(context, LoginAty.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        } else {
          if (e.getErrorCode() == 202) {
            ToastUtils.show("该手机号已经被注册，请尝试找回密码");
            Intent intent = new Intent(context, FoundPwdAty.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
          } else {
            ToastUtils.show(R.string.register_failed);
            Intent intent = new Intent(context, RegisterAty.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
          }
        }
      }
    });
  }

  /**
   * 更新密码
   */
  public static void updatePwd(final String smsCode, final String newPwd, final Context context) {
    BmobUser.resetPasswordBySMSCode(smsCode, newPwd, new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          ToastUtils.show("密码重置成功，请登录");
          Intent intent = new Intent(context, LoginAty.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        } else {
          ToastUtils.show("密码找回失败,请重新尝试");
          Intent intent = new Intent(context, FoundPwdAty.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        }
      }
    });
  }

  /**
   * 更新信息
   */
  public static <T extends BmobObject> void updata(T object) {
    object.update(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.data_succeed);
        } else {
          ToastUtils.show(R.string.data_failed);
        }
      }
    });
  }

  /**
   * 更新信息
   */
  public static <T extends BmobObject> void updata(T object, final Handler handler) {
    object.update(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.data_succeed);
          Message message = handler.obtainMessage();
          message.what = BmobManager.BLOOD_UPDATE;
          message.arg1 = 1;
          handler.sendMessage(message);
        } else {
          ToastUtils.show(R.string.data_failed);
        }
      }
    });
  }


  /**
   * 更新信息
   */
  public static <E extends BmobUser> void updata(E object) {
    object.update(new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.data_succeed);
        } else {
          ToastUtils.show(R.string.data_failed);
        }
      }
    });
  }




  /**
   * 保存忌口并回调
   */
  public static void saveAvoid(final Avoid avoid, final Handler handler) {
    avoid.save(new SaveListener<String>() {
      @Override public void done(String s, BmobException e) {
        if (e == null) {
          ToastUtils.show("忌口保存成功");
          Message message = handler.obtainMessage();
          message.obj = avoid;
          message.arg1 = 1;
          message.what = AVOID_ADD;
          handler.sendMessage(message);
        } else {
          ToastUtils.show("忌口保存失败");
        }
      }
    });
  }

  public static void collect(final InfoItem object, final Handler handler) {
    BmobQuery<InfoItem> bmobQuery = new BmobQuery<InfoItem>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER,
        BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
    bmobQuery.addWhereEqualTo(ConstantUtil.ID, object.getId());
    bmobQuery.findObjects(new FindListener<InfoItem>() {
      @Override public void done(List<InfoItem> list, BmobException e) {
        if (e == null && list.size() > 0) {
          Message message = handler.obtainMessage();
          message.what = INFO;
          message.arg1 = 1;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = INFO;
          message.arg1 = 0;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 移除收藏
   */
  public static void updateCollect(final InfoItem object, final Handler handler) {
    BmobQuery<InfoItem> bmobQuery = new BmobQuery<InfoItem>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, object.getPhoneNumber());
    bmobQuery.addWhereEqualTo(ConstantUtil.ID, object.getId());
    bmobQuery.findObjects(new FindListener<InfoItem>() {
      @Override public void done(List<InfoItem> list, BmobException e) {
        if (e != null) {
          object.setPhoneNumber(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
          saveData(object, handler);
        } else {
          InfoItem item = list.get(0);
          item.delete(item.getObjectId(), new UpdateListener() {
            @Override public void done(BmobException e) {
              if (e == null) {
                ToastUtils.show("取消收藏成功");
                Message message = handler.obtainMessage();
                message.what = INFO;
                message.arg1 = 0;
                handler.sendMessage(message);
              } else {
                ToastUtils.show("取消收藏失败");
              }
            }
          });
        }
      }
    });
  }

  /**
   * 移除忌口
   */
  public static void removeAvoid(Avoid avoid, final int pos, final Handler handler) {
    BmobQuery<Avoid> bmobQuery = new BmobQuery<Avoid>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, avoid.getPhoneNumber());
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, avoid.getDate());
    bmobQuery.addWhereEqualTo("item", avoid.getItem());
    bmobQuery.findObjects(new FindListener<Avoid>() {
      @Override public void done(List<Avoid> list, BmobException e) {
        if (e != null) {
          ToastUtils.show("连接服务器失败");
        } else {
          Avoid item = list.get(0);
          item.setTableName(ConstantUtil.AVOID);
          item.delete(item.getObjectId(), new UpdateListener() {
            @Override public void done(BmobException e) {
              if (e == null) {
                ToastUtils.show("移除忌口成功");
                Message message = handler.obtainMessage();
                message.what = AVOID_DELETE;
                message.arg1 = 1;
                message.arg2 = pos;
                handler.sendMessage(message);
              } else {
                ToastUtils.show("移除忌口失败");
              }
            }
          });
        }
      }
    });
  }

  /**
   * 上传头像
   */
  public static void uploadIcon(String imageUrl) {
    final BmobFile icon = new BmobFile(new File(imageUrl));
    icon.upload(new UploadFileListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          User user = BmobUser.getCurrentUser(User.class);
          if (user != null) {
            user.setIcon(icon);
            user.update(user.getObjectId(), new UpdateListener() {
              @Override public void done(BmobException e) {
                if (e == null){
                  ToastUtils.show("头像上传成功");
                }else {
                  ToastUtils.show("头像上传失败");
                }
              }
            });

          } else {
            ToastUtils.show("登录信息失效，请重新登录");
          }
        }
      }
    });
  }

  /**
   * 请求验证码
   */
  public static void requestCode(String phoneNumber) {
    BmobSMS.requestSMSCode(phoneNumber, "验证码", new QueryListener<Integer>() {
      @Override public void done(Integer integer, BmobException e) {
        if (e == null) {
          ToastUtils.show(R.string.code_sending);
        } else {
          ToastUtils.show(R.string.code_sent_failed);
        }
      }
    });
  }

  /**
   * 验证短信验证吗的正确与否
   */
  public static void verifyCode(final Context context, final String phoneNumber, String code,
      final int flag) {
    BmobSMS.verifySmsCode(phoneNumber, code, new UpdateListener() {
      @Override public void done(BmobException e) {
        if (e == null) {
          ToastUtils.show("验证成功");

          if (flag == 0) {
            //注册流程
            User user = new User();
            user.setMobilePhoneNumberVerified(true);
            user.setMobilePhoneNumber(phoneNumber);
            user.setUsername(phoneNumber);
            Intent intent = new Intent(context, RegisterAty.class);
            intent.putExtra(RegisterAty.CLICK, true);
            intent.putExtra(RegisterAty.USER, user);
            context.startActivity(intent);
          } else {
            //找回密码流程
            User user = new User();
            user.setMobilePhoneNumberVerified(true);
            user.setMobilePhoneNumber(phoneNumber);
            user.setUsername(phoneNumber);
            Intent intent = new Intent(context, FoundPwdAty.class);
            intent.putExtra(NEXT, true);
            intent.putExtra(RegisterAty.USER, user);
            context.startActivity(intent);
          }
        } else {
          ToastUtils.show("验证失败");
          if (flag == 0) {
            //注册流程
            Intent intent = new Intent(context, RegisterAty.class);
            intent.putExtra(RegisterAty.CLICK, false);
            intent.putExtra(RegisterAty.USER, new User());
            context.startActivity(intent);
          } else {

          }
        }
      }
    });
  }

  /**
   * 存储食谱
   */
  public static void updateRecipeData(final Recipe newRecipe, final int pos) {
    BmobQuery<Recipe> bmobQuery = new BmobQuery<Recipe>();
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, getToday());
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, newRecipe.getPhoneNumber());
    bmobQuery.findObjects(new FindListener<Recipe>() {
      @Override public void done(List<Recipe> list, BmobException e) {
        if (e == null) {
          Recipe recipe = list.get(0);
          switch (pos) {
            case 0:
              recipe.setBreakfast(newRecipe.getBreakfast());
              break;
            case 1:
              recipe.setAfternoon(newRecipe.getAfternoon());
              break;
            case 2:
              recipe.setDinner(newRecipe.getDinner());
              break;
            case 3:
              recipe.setAdd(newRecipe.getAdd());
              break;
          }
          recipe.setTableName(ConstantUtil.RECIPE);
          updata(recipe);
        } else {
          saveData(newRecipe);
        }
      }
    });
  }

  /**
   * 存储血糖数据
   */
  public static void updateBloodSugarData(final Blood newBlood, final int pos, final Handler handler) {
    BmobQuery<Blood> bmobQuery = new BmobQuery<Blood>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, newBlood.getPhoneNumber());
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, newBlood.getDate());
    bmobQuery.findObjects(new FindListener<Blood>() {
      @Override public void done(List<Blood> list, BmobException e) {
        if (e == null) {
          Blood blood = list.get(0);
          switch (pos) {
            case 0:
              blood.setPreBreakfastValue(newBlood.getPreBreakfastValue());
              break;
            case 1:
              blood.setAfterBreakfastValue(newBlood.getAfterBreakfastValue());
              break;
            case 2:
              blood.setPreLunchValue(newBlood.getPreLunchValue());
              break;
            case 3:
              blood.setAfterLunchValue(newBlood.getAfterLunchValue());
              break;
            case 4:
              blood.setPreDinner(newBlood.getPreDinner());
              break;
            case 5:
              blood.setAfterDinner(newBlood.getAfterDinner());
              break;
            case 6:
              blood.setSleep(newBlood.getSleep());
              break;
          }
          updata(blood,handler);
        } else {
          saveData(newBlood);
        }
      }
    });
  }

  /**
   * 获取血糖数据
   */
  public static void getBloodSugar(String phoneNumber, final Handler handler) {
    BmobQuery<Blood> bmobQuery = new BmobQuery<Blood>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.findObjects(new FindListener<Blood>() {
      @Override public void done(List<Blood> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BmobManager.BLOOD;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BmobManager.BLOOD;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }

  public static void updateRemark(final Advice advice) {
    BmobQuery<Advice> bmobQuery = new BmobQuery<Advice>();
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, getToday());
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, advice.getPhoneNumber());
    bmobQuery.findObjects(new FindListener<Advice>() {
      @Override public void done(List<Advice> list, BmobException e) {
        if (e != null) {
          saveData(advice);
        } else {
          Advice newAdvice = list.get(0);
          newAdvice.setTableName(ConstantUtil.DOCTOR_ADVICE);
          newAdvice.setContent(advice.getContent());
          newAdvice.setCheck(advice.getCheck());
          updata(newAdvice);
        }
      }
    });
  }

  /**
   * 获取食谱数据
   */
  public static void getRecipe(String phoneNumber, final Handler handler) {
    BmobQuery<Recipe> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, getToday());
    bmobQuery.findObjects(new FindListener<Recipe>() {
      @Override public void done(List<Recipe> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BmobManager.RECIPE_WHAT;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BmobManager.RECIPE_WHAT;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 获取食谱数据
   */
  public static void getRecipe(String phoneNumber, String date,final Handler handler) {
    BmobQuery<Recipe> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, date);
    bmobQuery.findObjects(new FindListener<Recipe>() {
      @Override public void done(List<Recipe> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BmobManager.RECIPE_WHAT;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BmobManager.RECIPE_WHAT;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }



  /**
   * 获取医嘱
   */
  public static void getAdvice(String phoneNumber, final Handler handler) {
    BmobQuery<Advice> bmobQuery = new BmobQuery<Advice>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, getToday());
    bmobQuery.findObjects(new FindListener<Advice>() {
      @Override public void done(List<Advice> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BmobManager.ADVICE_WHAT;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BmobManager.ADVICE_WHAT;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 获取医嘱
   */
  public static void getAdvice(String phoneNumber,String date,final Handler handler) {
    BmobQuery<Advice> bmobQuery = new BmobQuery<Advice>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.addWhereEqualTo(ConstantUtil.CREATE_DATA, date);
    bmobQuery.findObjects(new FindListener<Advice>() {
      @Override public void done(List<Advice> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BmobManager.ADVICE_WHAT;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BmobManager.ADVICE_WHAT;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }


  /**
   * 获取收藏
   */
  public static void getCollect(String phoneNumber, final Handler handler) {
    BmobQuery<InfoItem> bmobQuery = new BmobQuery<InfoItem>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.findObjects(new FindListener<InfoItem>() {
      @Override public void done(List<InfoItem> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = COLLECT;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = COLLECT;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 获取今天的日期
   */
  public static String getToday() {
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String date = sDateFormat.format(new java.util.Date());
    return date;
  }

  /**
   * 获取登录用户的信息
   */
  public static void getUserInfo(final Handler handler) {
    User user = BmobUser.getCurrentUser(User.class);
    BmobQuery<User> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo(ConstantUtil.MOBILEPHONENUMBER, user.getMobilePhoneNumber());
    bmobQuery.findObjects(new FindListener<User>() {
      @Override public void done(List<User> list, BmobException e) {
        if (e == null && list.size() > 0) {
          Message message = handler.obtainMessage();
          message.what = USER_WHAT;
          message.obj = list.get(0);
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 宝宝说
   * @param week
   * @param handler
   */
  public static void getBaby(int week, final Handler handler) {
    if (week>160){
      week = 160;
    }
    BmobQuery<Baby> babyBmobQuery = new BmobQuery<Baby>();
    babyBmobQuery.addWhereEqualTo(ConstantUtil.WEEK, week);
    babyBmobQuery.findObjects(new FindListener<Baby>() {
      @Override public void done(List<Baby> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = BABYSAY;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = BABYSAY;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }

  /**
   * 获取忌口清单
   */
  public static void getAvoid(String phoneNumber, final Handler handler) {
    BmobQuery<Avoid> bmobQuery = new BmobQuery<Avoid>();
    bmobQuery.addWhereEqualTo(ConstantUtil.PHONENMBER, phoneNumber);
    bmobQuery.findObjects(new FindListener<Avoid>() {
      @Override public void done(List<Avoid> list, BmobException e) {
        if (e == null) {
          Message message = handler.obtainMessage();
          message.what = AVOID;
          message.obj = list;
          handler.sendMessage(message);
        } else {
          Message message = handler.obtainMessage();
          message.what = AVOID;
          message.obj = null;
          handler.sendMessage(message);
        }
      }
    });
  }
}
