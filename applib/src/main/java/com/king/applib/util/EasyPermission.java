/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.king.applib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo:弹窗解耦<br/>
 * Utility to request and check System permissions for apps targeting Android M (API >= 23).
 * see also:https://github.com/baiiu/easypermissions,https://github.com/googlesamples/easypermissions
 * <p>
 * https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/java/com/blankj/utilcode/util/PermissionUtils.java
 * https://github.com/Blankj/AndroidUtilCode/blob/master/feature/utilcode/pkg/src/main/java/com/blankj/utilcode/pkg/feature/permission/PermissionActivity.kt
 * <p>
 * https://github.com/yanzhenjie/AndPermission/blob/master/support/src/main/java/com/yanzhenjie/permission/Rationale.java
 */
public class EasyPermission {

    public static final int SETTINGS_REQ_CODE = 16061;

    public interface PermissionCallback extends ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionGranted(int requestCode, List<String> grantedPerms);

        void onPermissionDenied(int requestCode, List<String> deniedPerms);

    }

    public interface OnPermissionRationaleCallback {
        void onShouldShowRationale();

        void onNeverShowRationale();
    }

    private Object object;
    private String[] mPermissions;
    private String mRationale;
    private int mRequestCode;
    @StringRes
    private int mPositiveButtonText = android.R.string.ok;
    @StringRes
    private int mNegativeButtonText = android.R.string.cancel;


    private EasyPermission(Object object) {
        this.object = object;
    }

    public static EasyPermission with(Activity activity) {
        return new EasyPermission(activity);
    }

    public static EasyPermission with(Fragment fragment) {
        return new EasyPermission(fragment);
    }

    public EasyPermission permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public EasyPermission rationale(String rationale) {
        this.mRationale = rationale;
        return this;
    }

    public EasyPermission addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public EasyPermission positveButtonText(@StringRes int positiveButtonText) {
        this.mPositiveButtonText = positiveButtonText;
        return this;
    }

    public EasyPermission nagativeButtonText(@StringRes int negativeButtonText) {
        this.mNegativeButtonText = negativeButtonText;
        return this;
    }

    public void request() {
        requestPermissions(object, mRationale, mPositiveButtonText, mNegativeButtonText, mRequestCode, mPermissions);
    }

    /**
     * Check if the calling context has a set of permissions.
     */
    public static boolean hasPermissions(Context context, String... perms) {
        if (!isOverMarshmallow()) {
            return true;
        }

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     */
    public static void requestPermissions(final Object object, String rationale, final int requestCode, final String... perms) {
        requestPermissions(object, rationale, android.R.string.ok, android.R.string.cancel, requestCode, perms);
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     */
    public static void requestPermissions(final Object object, String rationale, @StringRes int positiveButton, @StringRes int negativeButton,
                                          final int requestCode, final String... permissions) {

        checkCallingObjectSuitability(object);

        PermissionCallback mCallBack = (PermissionCallback) object;

        if (!isOverMarshmallow()) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
            return;
        }

        final List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);

        boolean shouldShowRationale = false;
        for (String perm : deniedPermissions) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
        }

        if (deniedPermissions.isEmpty()) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
        } else {

            final String[] deniedPermissionArray = deniedPermissions.toArray(new String[deniedPermissions.size()]);

            if (shouldShowRationale) {
                showRationaleDialog(object, rationale, requestCode, deniedPermissions, deniedPermissionArray);
            } else {
                executePermissionsRequest(object, deniedPermissionArray, requestCode);
            }
        }
    }

    private static void showRationaleDialog(final Object object, String rationale, final int requestCode, final List<String> deniedPermissions, final String[] deniedPermissionArray) {
        final Activity activity = getActivity(object);
        new AlertDialog.Builder(activity).setMessage(rationale)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest(activity, deniedPermissionArray, requestCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PermissionCallback) object).onPermissionDenied(requestCode, deniedPermissions);
                    }
                }).create().show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        checkCallingObjectSuitability(object);

        PermissionCallback mCallBack = (PermissionCallback) object;

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.isEmpty()) {
            mCallBack.onPermissionGranted(requestCode, Arrays.asList(permissions));
        } else {
            mCallBack.onPermissionDenied(requestCode, deniedPermissions);
        }
    }

    /**
     * If user denied permissions with the flag NEVER ASK AGAIN, open a dialog explaining the
     * permissions rationale again and directing the user to the app settings. After the user
     * returned to the app, onActivityResult() will be called with
     * {@value #SETTINGS_REQ_CODE} as requestCode
     * <p>
     * NOTE: use of this method is optional, should be called from
     * {@link PermissionCallback#onPermissionDenied(int, List)}
     *
     * @return {@code true} if user denied at least one permission with the flag NEVER ASK AGAIN.
     */
    public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, List<String> deniedPerms) {
        for (String perm : deniedPerms) {
            if (!shouldShowRequestPermissionRationale(object, perm)) {
                return true;
            }
        }

        return false;
    }

    public static void showSettingsDialog(final Object object, String rationale) {
        showSettingsDialog(object, rationale, android.R.string.ok, android.R.string.cancel);
    }

    public static void showSettingsDialog(final Object object, String rationale, @StringRes int positiveIds, @StringRes int negativeIds) {
        final Activity activity = getActivity(object);
        new AlertDialog.Builder(activity).setMessage(rationale)
                .setPositiveButton(positiveIds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettingsScreen(object);
                    }
                })
                .setNegativeButton(negativeIds, null)
                .create().show();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void startAppSettingsScreen(Object object) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getActivity(object).getPackageName(), null));
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        }
    }

    private static void checkCallingObjectSuitability(Object object) {
        checkContext(object);
        if (!(object instanceof PermissionCallback)) {
            throw new IllegalArgumentException("Caller must implement PermissionCallback.");
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            throw new IllegalArgumentException("Caller must not be null.");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();

        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }

        return denyPermissions;
    }

    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static void checkContext(Object object) {
        if (!((object instanceof Fragment) || (object instanceof Activity) || (object instanceof android.app.Fragment))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }
    }
}
