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

package com.otaliastudios.cameraview.metering;

import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public interface MeteringTransform<T> {
    @NonNull
    PointF transformMeteringPoint(@NonNull PointF point);

    @NonNull
    T transformMeteringRegion(@NonNull RectF region, int weight);
}
