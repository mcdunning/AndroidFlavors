package com.flavorspoc.widget;

public class NumberPadModel {
    private float value;
    private float min;
    private float max;
    private int precision;

    private boolean startWithOne;
    private boolean useImageForBackspace;
    private boolean useImageForSubmit;

    public NumberPadModel(float value, float min, float max, int precision, boolean startWithOne, boolean useImageForBackspace, boolean useImageForSubmit) {
        this.value = value;
        this.min = min;
        this.max = max;
        this.precision = precision;
        this.startWithOne = startWithOne;
        this.useImageForBackspace = useImageForBackspace;
        this.useImageForSubmit = useImageForSubmit;
    }

    public float getValue() {
        return value;
    }

    // The getters and setValue methods are kept package private since they should only be accessed by the number pad
    void setValue(final float value) {
        this.value = value;
    }

    float getMin() {
        return min;
    }
    float getMax() {
        return max;
    }
    int getPrecision() {
        return precision;
    }
    boolean isStartWithOne() {return startWithOne; }
    boolean useImageForBackspace() { return useImageForBackspace; }
    boolean useImageForSubmit() {return useImageForSubmit; }

}
