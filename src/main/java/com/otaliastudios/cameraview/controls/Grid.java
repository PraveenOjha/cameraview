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

package com.otaliastudios.cameraview.controls;


import com.otaliastudios.cameraview.CameraView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Grid values can be used to draw grid lines over the camera preview.
 *
 * @see CameraView#setGrid(Grid)
 */
public enum Grid implements Control {


    /**
     * No grid is drawn.
     */
    OFF(0),

    /**
     * Draws a regular, 3x3 grid.
     */
    DRAW_3X3(1),

    /**
     * Draws a regular, 4x4 grid.
     */
    DRAW_4X4(2),

    /**
     * Draws a grid respecting the 'phi' constant proportions,
     * often referred as to the golden ratio.
     */
    DRAW_PHI(3);

    static final Grid DEFAULT = OFF;

    private int value;

    Grid(int value) {
        this.value = value;
    }

    int value() {
        return value;
    }

    @NonNull
    static Grid fromValue(int value) {
        Grid[] list = Grid.values();
        for (Grid action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return DEFAULT;
    }
}