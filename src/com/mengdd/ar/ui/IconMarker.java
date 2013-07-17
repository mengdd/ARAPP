package com.mengdd.ar.ui;

import com.mengdd.paintable.PaintableIcon;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This class extends Marker and draws an icon instead of a circle for it's
 * visual representation.
 * 
 * The source of the codes:
 * 1."android-augment-reality-framework"
 * project link: http://code.google.com/p/android-augment-reality-framework/
 * 
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451
 * Official repository for Pro Android Augmented Reality:
 * https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class IconMarker extends Marker
{

	private Bitmap bitmap = null;

	public IconMarker(String name, double latitude, double longitude,
			double altitude, int color, Bitmap bitmap)
	{
		super(name, latitude, longitude, altitude, color);
		this.bitmap = bitmap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawIcon(Canvas canvas)
	{
		if (canvas == null || bitmap == null)
			throw new NullPointerException();

		if (gpsSymbol == null)
			gpsSymbol = new PaintableIcon(bitmap, 96, 96);
		super.drawIcon(canvas);
	}
}
