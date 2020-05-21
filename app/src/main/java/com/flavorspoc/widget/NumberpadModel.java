package com.flavorspoc.widget;

public class NumberpadModel {
    private float value;
    private float min;
    private float max;
    private int precision;

    public NumberpadModel(float value, float min, float max, int precision) {
        this.value = value;
        this.min = min;
        this.max = max;
        this.precision = precision;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public int getPrecision() {
        return precision;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
