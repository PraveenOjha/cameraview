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

package com.otaliastudios.cameraview.filter;

/**
 * A special {@link Filter} that accepts a float parameter.
 *
 * The parameters will always be between 0F and 1F, so subclasses should
 * map this range to their internal range if needed.
 *
 * A standardized range is useful for different applications. For example:
 * - Filter parameters can be easily mapped to gestures since the range is fixed
 * - {@link BaseFilter} can use this setters and getters to make a filter copy
 */
public interface OneParameterFilter extends Filter {

    /**
     * Sets the parameter.
     * The value should always be between 0 and 1.
     *
     * @param value parameter
     */
    void setParameter1(float value);

    /**
     * Returns the parameter.
     * The returned value should always be between 0 and 1.
     *
     * @return parameter
     */
    float getParameter1();
}
