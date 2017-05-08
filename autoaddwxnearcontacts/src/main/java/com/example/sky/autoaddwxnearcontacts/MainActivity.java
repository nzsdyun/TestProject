package com.example.sky.autoaddwxnearcontacts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sky.autoaddwxnearcontacts.util.Constant;
import com.example.sky.autoaddwxnearcontacts.util.SharedPreferecesUtil;

public class MainActivity extends Activity {
    private ImageButton accessibleButton;
    private TextView showStep;
    private Button autoAdd;
    private boolean mIsChecked = Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG_DEFAULT;
    private final SettingsContentObserver mSettingsContentObserver =
            new SettingsContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    updateView(WxAddNearContactService.isOpenAccessible(MainActivity.this));
                }
            };

    private void updateView(boolean openAccessible) {
        if (openAccessible) {
//            accessibleButton.setVisibility(View.GONE);
            showStep.setVisibility(View.GONE);
        } else {
//            accessibleButton.setVisibility(View.VISIBLE);
            showStep.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingsContentObserver.register(getContentResolver());
        accessibleButton = (ImageButton) findViewById(R.id.open_accessible_settings);
        showStep = (TextView) findViewById(R.id.open_accessible_settings_tips);
        mIsChecked = SharedPreferecesUtil.getBoolean(this, Constant.AutoAddContactsConstant.SHARED_PREFERENCE_NAME,
                Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG,
                Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG_DEFAULT);
        final CheckBox autoAddNearContacts = (CheckBox) findViewById(R.id.auto_add_near_contacts);
        autoAddNearContacts.setChecked(mIsChecked);
        if (mIsChecked) {
            autoAddNearContacts.setText(R.string.auto_add_near_contacts_stop);
        } else {
            autoAddNearContacts.setText(R.string.auto_add_near_contacts_start);
        }
        autoAddNearContacts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
                if (mIsChecked) {
                    autoAddNearContacts.setText(R.string.auto_add_near_contacts_stop);
                } else {
                    autoAddNearContacts.setText(R.string.auto_add_near_contacts_start);
                }
                SharedPreferecesUtil.setBoolean(MainActivity.this, Constant.AutoAddContactsConstant.SHARED_PREFERENCE_NAME,
                        Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG, isChecked);
            }
        });
        autoAdd = (Button) findViewById(R.id.start_auto_add);
        autoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsChecked && WxAddNearContactService.isOpenAccessible(MainActivity.this)
                        == false) {
                    Toast.makeText(MainActivity.this, R.string.wx_accessible_no_open, Toast.LENGTH_LONG).show();
                    return;
                }
                openWx();
            }
        });
        if (!WxAddNearContactService.isOpenAccessible(this)) {
            SharedPreferecesUtil.setBoolean(MainActivity.this, Constant.AutoAddContactsConstant.SHARED_PREFERENCE_NAME,
                    Constant.AutoAddContactsConstant.SHARED_PREFERENCE_FLAG, false);
            updateView(false);
            accessibleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAccessibleSettings();
                }
            });
        }
    }

    private void openWx() {
        Intent wxIntent = new Intent();
        ComponentName wxComponentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        wxIntent.setComponent(wxComponentName);
        wxIntent.setAction(Intent.ACTION_MAIN);
        wxIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        wxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (wxIntent.resolveActivity(getPackageManager()) != null)  {
            startActivity(wxIntent);
        } else {
            Toast.makeText(this, R.string.wx_no_installed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        mSettingsContentObserver.unregister(getContentResolver());
        super.onDestroy();
    }

    private void startAccessibleSettings() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    abstract class SettingsContentObserver extends ContentObserver {

        public SettingsContentObserver(Handler handler) {
            super(handler);
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(Settings.Secure.getUriFor(
                    Settings.Secure.ACCESSIBILITY_ENABLED), false, this);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor(
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES), false, this);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }
    }
}
