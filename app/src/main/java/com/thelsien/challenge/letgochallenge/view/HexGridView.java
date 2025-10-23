package com.thelsien.challenge.letgochallenge.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view that draws a hexagonal grid filling the entire screen.
 * Hexagons have black stroke outlines and small gaps between them.
 */
public class HexGridView extends View {

    private Paint strokePaint;
    private float hexRadius;
    private float hexWidth;
    private float hexHeight;
    private float gap;

    public HexGridView(Context context) {
        super(context);
        init();
    }

    public HexGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paint for hexagon stroke
        strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3f);
        strokePaint.setAntiAlias(true);

        // Set hexagon size (radius from center to vertex)
        hexRadius = 80f;
        gap = 4f; // Small gap between hexagons

        // Calculate hexagon dimensions
        // For a flat-topped hexagon:
        // width = 2 * radius
        // height = sqrt(3) * radius
        hexWidth = 2f * hexRadius;
        hexHeight = (float) (Math.sqrt(3) * hexRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return;
        }

        // Calculate horizontal and vertical spacing between hexagon centers
        float horizontalSpacing = hexWidth * 0.75f + gap;
        float verticalSpacing = hexHeight + gap;

        // Calculate how many rows and columns we need to fill the screen
        // We add extra hexagons beyond the edges to ensure full coverage
        int numCols = (int) Math.ceil(width / horizontalSpacing) + 2;
        int numRows = (int) Math.ceil(height / verticalSpacing) + 2;

        // Start drawing from off-screen to ensure hexagons run over edges
        float startX = -hexWidth;
        float startY = -hexHeight;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Calculate center position of hexagon
                float x = startX + col * horizontalSpacing;
                float y = startY + row * verticalSpacing;

                // Offset every other row for hexagonal pattern
                if (row % 2 == 1) {
                    x += horizontalSpacing / 2f;
                }

                // Draw hexagon at this position
                drawHexagon(canvas, x, y);
            }
        }
    }

    /**
     * Draws a flat-topped hexagon at the specified center position
     */
    private void drawHexagon(Canvas canvas, float centerX, float centerY) {
        Path hexPath = new Path();

        // Calculate the 6 vertices of a flat-topped hexagon
        // Starting from the top-left vertex and going clockwise
        for (int i = 0; i < 6; i++) {
            float angleDeg = 60f * i;
            float angleRad = (float) Math.toRadians(angleDeg);

            float x = centerX + (hexRadius - gap / 2f) * (float) Math.cos(angleRad);
            float y = centerY + (hexRadius - gap / 2f) * (float) Math.sin(angleRad);

            if (i == 0) {
                hexPath.moveTo(x, y);
            } else {
                hexPath.lineTo(x, y);
            }
        }

        hexPath.close();
        canvas.drawPath(hexPath, strokePaint);
    }

    /**
     * Set the size of hexagons
     * @param radius The radius from center to vertex in pixels
     */
    public void setHexRadius(float radius) {
        this.hexRadius = radius;
        this.hexWidth = 2f * hexRadius;
        this.hexHeight = (float) (Math.sqrt(3) * hexRadius);
        invalidate();
    }

    /**
     * Set the gap between hexagons
     * @param gap The gap size in pixels
     */
    public void setGap(float gap) {
        this.gap = gap;
        invalidate();
    }

    /**
     * Set the stroke color
     * @param color The color for hexagon outlines
     */
    public void setStrokeColor(int color) {
        strokePaint.setColor(color);
        invalidate();
    }

    /**
     * Set the stroke width
     * @param width The width of hexagon outlines in pixels
     */
    public void setStrokeWidth(float width) {
        strokePaint.setStrokeWidth(width);
        invalidate();
    }
}
