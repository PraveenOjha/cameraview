/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 17/09/25, 7:58 pm
 *  * Use only if permission provided by ILabs (inductiongames.com)
 *   *  * Use or edit only if permission provided by Ilbas
 *   *  * email : inductionlabs@gmail.com
 *   *  * A github invite link will be considered permission for using this code
 *   *
 *   *
 *  *
 *
 *
 */

package com.otaliastudios.cameraview.preview;


import android.content.Context;
import androidx.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.preview.CameraPreview;

public class MockCameraPreview extends CameraPreview<View, Void> implements FilterCameraPreview {

    public MockCameraPreview(Context context, ViewGroup parent) {
        super(context, parent);
    }

    private View rootView;
    private Filter filter;

    @Override
    public boolean supportsCropping() {
        return true;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull Context context, @NonNull ViewGroup parent) {
        rootView = new View(context);
        return rootView;
    }

    @NonNull
    @Override
    public Class<Void> getOutputClass() {
        return null;
    }

    @NonNull
    @Override
    public Void getOutput() {
        return null;
    }


    @NonNull
    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void setFilter(@NonNull Filter filter) {
        this.filter = filter;
    }

    @NonNull
    @Override
    public Filter getCurrentFilter() {
        return filter;
    }
}
