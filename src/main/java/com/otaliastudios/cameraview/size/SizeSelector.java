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

package com.otaliastudios.cameraview.size;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * A size selector receives a list of {@link Size}s and returns another list with
 * sizes that are considered acceptable.
 */
public interface SizeSelector {

    /**
     * Returns a list of acceptable sizes from the given input.
     * The final size will be the first element in the output list.
     *
     * @param source input list
     * @return output list
     */
    @NonNull
    List<Size> select(@NonNull List<Size> source);
}
