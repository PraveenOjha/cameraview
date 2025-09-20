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

package com.otaliastudios.cameraview.gesture;


/**
 * Gestures and gesture actions can both have a type. For a gesture to be able to be mapped to
 * a certain {@link GestureAction}, both of them might be of the same type.
 */
public enum GestureType {

    /**
     * Defines gestures or gesture actions that consist of a single operation.
     * Gesture example: a tap.
     * Gesture action example: taking a picture.
     */
    ONE_SHOT,

    /**
     * Defines gestures or gesture actions that consist of a continuous operation.
     * Gesture example: pinching.
     * Gesture action example: controlling zoom.
     */
    CONTINUOUS
}
