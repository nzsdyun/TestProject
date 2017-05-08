package com.example.sky.autoaddwxnearcontacts;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.sky.autoaddwxnearcontacts.util.Constant;
import com.example.sky.autoaddwxnearcontacts.util.SharedPreferecesUtil;
import com.example.sky.autoaddwxnearcontacts.util.StringUtils;

import java.util.List;

public class WxAddNearContactService extends AccessibilityService {
    public static final String TAG = WxAddNearContactService.class.getSimpleName();

    private static final String WX_MAIN_ACTIVITY = "com.tencent.mm.ui.LauncherUI";
    private static final String WX_FIND_INTERFACE = "android.widget.RelativeLayout";
    private static final String WX_FIND_NEAR_INTERFACE = "com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI";
    private static final String WX_FIND_NEAR_LIST = "android.widget.ListView";
    private static final String WX_FIND_NEAR_DETAIL = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";
    private static final String WX_FIND_NEAR_HELLO = "com.tencent.mm.ui.contact.SayHiEditUI";

    private long mCurrentInstance = 0;
    private int mCurrentIndex = 0;
    private String mDefaultHello = "hello";
    private boolean mIsSend = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "event = " + event.toString());
        if (SharedPreferecesUtil.getBoolean(this, Constant.AutoAddContactsConstant.SHARED_PREFERENCE_NAME,
                Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG,
                Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG_DEFAULT)) {
            int type = event.getEventType();
            switch (type) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    String claName = event.getClassName().toString();
                    String claContent = event.getText().toString();
                    Log.d(TAG, "class name:" + claName + ", claContent:" + claContent);
                    if (WX_MAIN_ACTIVITY.equals(claName)) {
                        mCurrentIndex = 0;
                        mCurrentInstance = 0;
                        // click find button
                        handleClick("com.tencent.mm:id/bq0", R.string.wx_find);
                    } else if (claContent.contains(getStr(R.string.wx_near_dialog_tips))) {
                        //find near contacts dialog tips， click confirm button
                        handleClick("com.tencent.mm:id/abg", R.string.wx_near_dialog_tips_confirm);
                    } else if (WX_FIND_NEAR_INTERFACE.equals(claName)) {
                        mIsSend = false;
                        // scroll next page
                        AccessibilityNodeInfo nearList = getList(getRootInActiveWindow());
                        if (nearList != null) {
                            nearList.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        }
                        //current interface is near contacts interface
//                        AccessibilityNodeInfo nearList = getList(getRootInActiveWindow());
//                        Log.d(TAG, "contacts count:" + nearList.getChildCount());
//                        AccessibilityNodeInfo nearInstanceNode = obtainNodeInfo("com.tencent.mm:id/bv_", R.string.wx_near_instance_tips, true);
//                        String instanceText = nearInstanceNode.getText().toString();
//                        mCurrentInstance = StringUtils.obtainNumbers(instanceText);
//                        if (instanceText.contains(getStr(R.string.wx_near_instance_km))) {
//                            mCurrentInstance = mCurrentInstance * 1000;
//                        }
//                        Log.d(TAG, "mCurrentInstance :" + mCurrentInstance);
                    } else if (WX_FIND_NEAR_DETAIL.equals(claName)) {
                        if (mIsSend) {
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            mIsSend = false;
                            return;
                        }
                        //near contacts details interface
                        AccessibilityNodeInfo helloNodeInfo = obtainNodeInfo("com.tencent.mm:id/acw",
                                R.string.wx_near_contacts_detail_hello, false);
                        if (helloNodeInfo != null) {
                            helloNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else {
                            //FIXME: is already a good friend
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        }
                    } else if (WX_FIND_NEAR_HELLO.equals(claName)) {
                        // say hello interface
                        inputHello(mDefaultHello);
                        AccessibilityNodeInfo sendNodeInfo = obtainNodeInfo("com.tencent.mm:id/gd",
                                R.string.wx_near_contacts_say_hello_send, false);
                        if (sendNodeInfo != null) {
                            sendNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            mIsSend = true;
                        }
                        //test
//                        handleClick("com.tencent.mm:id/gw", null);
//                        mIsSend = true;
                    }
                    break;
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    String findName = event.getClassName().toString();
                    String text = event.getText().toString();
                    Log.d(TAG, "findName:" + findName + ", text:" + text);
                    if (WX_FIND_INTERFACE.equals(findName)
                            && text.contains(getStr(R.string.wx_find)))
                        handleClick("android:id/title", R.string.wx_near_contacts);
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    String listName = event.getClassName().toString();
                    if (WX_FIND_NEAR_LIST.equals(listName)) {
                        int totalCount = event.getItemCount();
                        int formIndex = event.getFromIndex();
                        int toIndex = event.getToIndex();
                        if (mCurrentIndex >= totalCount) {
                            //all have been added, come back
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            return;
                        }
                        Log.d(TAG, "totalCount:" + totalCount + ", formIndex:" + formIndex + ", toIndex:" + toIndex + " mCurrentIndex:" + mCurrentIndex);
                        if (mCurrentIndex < totalCount && mCurrentIndex < toIndex) {
                            mCurrentIndex = formIndex;
                            handleClick("com.tencent.mm:id/alh", null);
                            mCurrentIndex += (toIndex - formIndex);
                        }
                    }
                    break;
            }
        }
    }

    private AccessibilityNodeInfo getList(AccessibilityNodeInfo nearRoot) {
        if (nearRoot == null) {
            return null;
        }
        List<AccessibilityNodeInfo> items = nearRoot.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bv3");
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++)
                if (WX_FIND_NEAR_LIST.equals(items.get(i).getClassName())) {
                    return items.get(i);
                }
        }
        return null;
    }

    private long getInstance(String instanceContent) {
        long instance = StringUtils.obtainNumbers(instanceContent);
        if (instanceContent.contains(getStr(R.string.wx_near_instance_km))) {
            instance = instance * 1000;
        }
        return instance;
    }

    private void inputHello(String hello) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            Log.d(TAG, "inputHello: null");
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", hello);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }


    private String getStr(int resId) {
        return getResources().getString(resId);
    }

    /**
     * handle click
     */
    private void handleClick(String viewId, int strId) {
        handleClick(viewId, getStr(strId));
    }

    /**
     * handle click
     */
    private void handleClick(String viewId, String str) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        String findStr = str;
        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId(viewId);
            rootNode.recycle();
            if (nodeInfos != null && nodeInfos.size() > 0) {
                for (int i = 0; i < nodeInfos.size(); i++) {
                    AccessibilityNodeInfo nodeInfo = nodeInfos.get(i);
                    if (nodeInfo != null) {
                        Log.d(TAG, "content :" + nodeInfo.getText());
                        if (findStr == nodeInfo.getText()
                                || findStr.equals(nodeInfo.getText())) {
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            AccessibilityNodeInfo parent = nodeInfo.getParent();
                            while (parent != null) {
                                if (parent.isClickable()) {
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    break;
                                }
                                parent = parent.getParent();
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * obtain AccessibilityNodeInfo
     */
    private AccessibilityNodeInfo obtainNodeInfo(String viewId, int strId, boolean isInclusive) {
        return obtainNodeInfo(viewId, getStr(strId), isInclusive);
    }

    /**
     * obtain AccessibilityNodeInfo
     */
    private AccessibilityNodeInfo obtainNodeInfo(String viewId, String str, boolean isInclusive) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        String findStr = str;
        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId(viewId);
            rootNode.recycle();
            if (nodeInfos != null && nodeInfos.size() > 0) {
                for (int i = 0; i < nodeInfos.size(); i++) {
                    AccessibilityNodeInfo nodeInfo = nodeInfos.get(i);
                    if (nodeInfo != null) {
                        if (findStr == nodeInfo.getText()) {
                            return nodeInfo;
                        }
                        String nodeText = nodeInfo.getText().toString();
                        if (isInclusive) {
                            if (nodeText.contains(findStr)) {
                                return nodeInfo;
                            }
                        } else {
                            if (findStr.equals(nodeInfo.getText())) {
                                return nodeInfo;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        if (current != null) {
            if (WX_FIND_NEAR_LIST.equals(current.getClassName())) {
                return current;
            } else {
                for (int i = 0; i < current.getChildCount(); i++) {
                    getListItemNodeInfo(current = current.getChild(i));
                }
            }
        }
        return null;
//        AccessibilityNodeInfo current = source;
//        while (true) {
//            AccessibilityNodeInfo parent = current.getParent();
//            if (parent == null) {
//                return null;
//            }
//            if (WX_FIND_NEAR_LIST.equals(parent.getClassName())) {
//                return current;
//            }
//            // NOTE: Recycle the infos.
//            AccessibilityNodeInfo oldCurrent = current;
//            current = parent;
//            oldCurrent.recycle();
//        }
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * accessible weather open
     */
    public static boolean isOpenAccessible(Context context) {
        int accessibleEnabled = 0;
        final String service = context.getPackageName() + "/" + WxAddNearContactService.class.getCanonicalName();
        try {
            accessibleEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }

        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (accessibleEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }

                }
            }

        }
        return false;
    }
}
