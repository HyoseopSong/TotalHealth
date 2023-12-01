package com.medichain.totalhealth.util

import android.content.Context
import android.graphics.Bitmap

import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BlurBuilder {
    val BITMAP_SCALE = 0.3f
    val BLUR_RADIUS = 20f

    fun blur(context : Context, image:Bitmap) : Bitmap{
        val width = Math.round(image.getWidth() * BITMAP_SCALE);
        val height = Math.round(image.getHeight() * BITMAP_SCALE);
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        val outputBitmap = Bitmap.createBitmap(inputBitmap);
        val rs = RenderScript.create(context);
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }


}