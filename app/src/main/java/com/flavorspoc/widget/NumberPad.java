package com.flavorspoc.widget;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.flavorspoc.R;

public class NumberPad extends ConstraintLayout {
    private final String TAG = getClass().getCanonicalName();

    private View anchor;

    private float paddingTop = 0.0f;
    private float paddingStart = 0.0f;

    private NumberpadModel numberpadModel;
    private View.OnClickListener onConfirm;

    private ConstraintLayout mainView;
    private ConstraintLayout container;
    private TextView value;
    private Button r41;
    private Button r42;
    private Button r43;
    private Button r31;
    private Button r32;
    private Button r33;
    private Button r21;
    private Button r22;
    private Button r23;
    private Button zero;
    private Button decimal_point;
    private Button backspace;

    // boolean flag to replace the current value with the value of the first button clicked
    private Button submit;
    private boolean handleFirstButtonClick = true;
    private View.OnClickListener numberpadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.r23:
                case R.id.r22:
                case R.id.r21:
                case R.id.r33:
                case R.id.r32:
                case R.id.r31:
                case R.id.r43:
                case R.id.r42:
                case R.id.r41:
                case R.id.zero:
                case R.id.decimal_point:
                    // Fall through let the number press handler take care of this input
                    handleNumberPress(getNumKeyStringId(v.getId()));
                    break;
                case R.id.submit:
                    if (value.getText().length() != 0) {
                        numberpadModel.setValue(Float.parseFloat(value.getText().toString()));
                    }
                    onConfirm.onClick(v);
                    break;
                case R.id.backspace:
                    if (handleFirstButtonClick) {
                        handleFirstButtonClick = false;
                    }
                    CharSequence currentValue = value.getText();
                    if (currentValue.length() != 0) {
                        if (currentValue.length() == 1) {
                            value.setText("");
                        } else {
                            value.setText(currentValue.subSequence(0, currentValue.length() - 1));
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "The view id," + v.getId() + ", is not supported by the keypad");
            }
        }
    };

    public NumberPad(Context context) {
        this(context, null, 0);
    }

    public NumberPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.activity_numerpad, this, true);

        mainView = findViewById(R.id.main_view);
        container = findViewById(R.id.numberpad_container);
        value = findViewById(R.id.value);
        backspace = findViewById(R.id.backspace);
        r23 = findViewById(R.id.r23);
        r22 = findViewById(R.id.r22);
        r21 = findViewById(R.id.r21);
        r33 = findViewById(R.id.r33);
        r32 = findViewById(R.id.r32);
        r31 = findViewById(R.id.r31);
        r43 = findViewById(R.id.r43);
        r42 = findViewById(R.id.r42);
        r41 = findViewById(R.id.r41);
        zero = findViewById(R.id.zero);
        decimal_point = findViewById(R.id.decimal_point);
        submit = findViewById(R.id.submit);
    }

    public void init (final View anchor, final NumberpadModel numberpadModel, final View.OnClickListener onConfirm) {
        this.anchor = anchor;
        this.numberpadModel = numberpadModel;
        this.onConfirm = onConfirm;

        applyStyleToNumberpad();
    }

    /**
     * Shows the number pad
     */
    @SuppressLint("ClickableViewAccessibility")
    public void show() {
        // Assign the listeners to the number pad keys
        backspace.setOnClickListener(numberpadButtonClickListener);
        r23.setOnClickListener(numberpadButtonClickListener);
        r22.setOnClickListener(numberpadButtonClickListener);
        r21.setOnClickListener(numberpadButtonClickListener);
        r33.setOnClickListener(numberpadButtonClickListener);
        r32.setOnClickListener(numberpadButtonClickListener);
        r31.setOnClickListener(numberpadButtonClickListener);
        r43.setOnClickListener(numberpadButtonClickListener);
        r42.setOnClickListener(numberpadButtonClickListener);
        r41.setOnClickListener(numberpadButtonClickListener);
        zero.setOnClickListener(numberpadButtonClickListener);
        decimal_point.setOnClickListener(numberpadButtonClickListener);
        submit.setOnClickListener(numberpadButtonClickListener);

        mainView.setOnTouchListener((v1, event) -> {
            hide();
            return true;
        });

        value.setText(Float.toString(numberpadModel.getValue()));

        // Initialize the dialog box preparing for the show animation.
        mainView.setAlpha(0);
        this.setAlpha(1);

        // Add the dialog to the root view.
        Activity activity = ContextUtil.getActivity(anchor.getContext());
        View rootView;
        if(activity != null) {
            rootView = activity.findViewById(android.R.id.content);
        } else {
            rootView = anchor;
            while (rootView.getParent() != null && rootView.getParent() instanceof View && rootView.getParent() instanceof ViewGroup) {
                rootView = (View) rootView.getParent();
            }
        }

        if(rootView instanceof ViewGroup) {
            ((ViewGroup)rootView).addView(this);
        }

        // We must post the animation because Android will not measure the container until after
        // this call.  We just added it to the layout so we must do the show later.
        anchor.post(() -> {
            // Get the xy coordinates to explode from.
            int[] translationXY = getTranslationXY(anchor);

            // Move the number pad container to the middle of the anchor.
            mainView.setTranslationX(translationXY[0]);
            mainView.setTranslationY(translationXY[1]);
            mainView.setScaleX(0);
            mainView.setScaleY(0);

            // Explode the popup moving it to the padding offset and scaling it from 0 to 1.
            mainView.animate().setDuration(400).alpha(1).translationY(paddingTop).translationX(paddingStart).scaleX(1).scaleY(1).setListener(new Animator.AnimatorListener() {
                /**
                 * Make sure the container is correctly aligned at the end of the animation.
                 */
                private void onAnimationComplete() {
                    mainView.setAlpha(1);
                    mainView.setScaleX(1);
                    mainView.setScaleY(1);
                    mainView.setTranslationX(paddingStart);
                    mainView.setTranslationY(paddingTop);

                }

                @Override
                public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimationComplete();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationComplete();
                }

                @Override
                public void onAnimationRepeat(Animator animation) { }
            }).start();
        });
    }

    /**
     * Hides the number pad when the submit is hit or if a touch event outside the
     * number pad is detected
     */
    public void hide() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        Context context = getContext();
        View view = null;
        if(context instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                view = ((Activity) getContext()).getCurrentFocus();
            }
        }

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getContext());
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if(anchor != null) {
            // Get the point to implode to.
            int[] translationXY = getTranslationXY(anchor);
            anchor = null;

            // Collapse the popup back to the anchor.
            mainView.animate().setDuration(400).translationX(translationXY[0] + paddingStart).translationY(translationXY[1] + paddingTop).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
                private void onAnimationComplete() {
                    ViewGroup rootView = (ViewGroup) NumberPad.this.getParent();
                    if (rootView != null) {
                        rootView.removeView(NumberPad.this);
                    }

                    mainView.setScaleX(1);
                    mainView.setScaleY(1);
                    mainView.setTranslationX(paddingStart);
                    mainView.setTranslationY(paddingTop);
                    NumberPad.this.setAlpha(1);

                    cleanup();
                }

                @Override
                public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimationComplete();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimationComplete();
                }

                @Override
                public void onAnimationRepeat(Animator animation) { }
            }).start();

            // Fade everything as well.
            animate().setDuration(400).alpha(0).start();
        }
    }

    private void cleanup() {
        mainView.setOnTouchListener(null);

        backspace.setOnClickListener(null);
        r23.setOnClickListener(null);
        r22.setOnClickListener(null);
        r21.setOnClickListener(null);
        r33.setOnClickListener(null);
        r32.setOnClickListener(null);
        r31.setOnClickListener(null);
        r43.setOnClickListener(null);
        r42.setOnClickListener(null);
        r41.setOnClickListener(null);
        zero.setOnClickListener(null);
        decimal_point.setOnClickListener(null);
        submit.setOnClickListener(null);

        backspace = null;
        r23 = null;
        r22 = null;
        r21 = null;
        r33 = null;
        r32 = null;
        r31 = null;
        r43 = null;
        r42 = null;
        r41 = null;
        zero = null;
        decimal_point = null;
        submit = null;

        anchor = null;
        numberpadModel = null;
        onConfirm = null;
    }

    private void applyStyleToNumberpad() {
        GradientDrawable currentDrawable;

        // Style the numberpad container
        container.setBackgroundResource(R.drawable.rounded_container);
        currentDrawable = (GradientDrawable) container.getBackground();
        currentDrawable.mutate();
        currentDrawable.setColor(getResources().getColor(resolveAttributeId(R.attr.numberpad_container_color)));
        currentDrawable.setCornerRadius(getResources().getDimension(resolveAttributeId(R.attr.numberpad_container_corner_radius)));

        // style the value text view
        value.setBackgroundResource(R.drawable.rounded_container);
        currentDrawable = (GradientDrawable) value.getBackground();
        currentDrawable.mutate();
        currentDrawable.setColor(getResources().getColor(resolveAttributeId(R.attr.numberpad_value_background)));
        currentDrawable.setCornerRadius(getResources().getDimension(resolveAttributeId(R.attr.numberpad_value_corner_radius)));

        // Set the text of the number pad keys
        if (!numberpadModel.useImageForBackspace()) {
            backspace.setBackground(getResources().getDrawable(R.drawable.numberpad_button_background));
            backspace.setText(getResources().getString(R.string.backspace));
        }

        if (!numberpadModel.useImageForSubmit()) {
            submit.setBackground(getResources().getDrawable(R.drawable.numberpad_button_background));
            submit.setText(getResources().getString(R.string.submit));
        }

        r23.setText(getResources().getString(getNumKeyStringId(R.id.r23)));
        r22.setText(getResources().getString(getNumKeyStringId(R.id.r22)));
        r21.setText(getResources().getString(getNumKeyStringId(R.id.r21)));
        r33.setText(getResources().getString(getNumKeyStringId(R.id.r33)));
        r32.setText(getResources().getString(getNumKeyStringId(R.id.r32)));
        r31.setText(getResources().getString(getNumKeyStringId(R.id.r31)));
        r43.setText(getResources().getString(getNumKeyStringId(R.id.r43)));
        r42.setText(getResources().getString(getNumKeyStringId(R.id.r42)));
        r41.setText(getResources().getString(getNumKeyStringId(R.id.r41)));
        zero.setText(getResources().getString(R.string.zero));
        decimal_point.setText(getResources().getString(R.string.decimal_point));
    }

    private int resolveAttributeId(int attributeId) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(attributeId, typedValue, true);

        return typedValue.resourceId;
    }

    /**
     * This computes the point from which to explode from.
     * @param anchor    The view to explode from.  We will explode from the middle of the view.
     * @return          The computed translation XY
     */
    private int[] getTranslationXY(View anchor) {

        int[] anchorXY = new int[2];
        int[] containerXY = new int[2];
        int[] translationXY = new int[2];

        // Get the coordinates for the anchor and the container.
        mainView.getLocationOnScreen(containerXY);
        anchor.getLocationOnScreen(anchorXY);

        // Compute the translation point.
        translationXY[0] = (anchorXY[0] + anchor.getWidth() / 2) - (containerXY[0] + mainView.getWidth() / 2);
        translationXY[1] = (anchorXY[1] + anchor.getHeight() / 2) - (containerXY[1] + mainView.getHeight() / 2);

        return translationXY;
    }

    /**
     * Checks to see if the new value generated by the key press is valid based on the passed in model
     * @param numberpadKeyId the string id of the key that was pressed
     */
    private void handleNumberPress(int numberpadKeyId) {
        boolean updateValue = false;
        StringBuilder newValue;
        if (handleFirstButtonClick) {
            newValue = new StringBuilder();
            handleFirstButtonClick = false;
        } else {
            newValue = new StringBuilder(value.getText().toString());
        }
        
        String passedValue = getResources().getString(numberpadKeyId);
        if (R.string.decimal_point == numberpadKeyId) {
            if (!newValue.toString().contains(passedValue)) {
                newValue.append(passedValue);
                updateValue = true;
            }
        } else {
            int decimalPlaceIndex = newValue.toString().indexOf(getResources().getString(R.string.decimal_point));
            if (decimalPlaceIndex == -1 || (newValue.toString().length() - decimalPlaceIndex -1) < numberpadModel.getPrecision()) {
                newValue.append(passedValue);
                float newFloatValue = Float.parseFloat(newValue.toString());
                updateValue = newFloatValue <= numberpadModel.getMax() && newFloatValue >= numberpadModel.getMin();
            }
        }

        if (updateValue) {
            value.setText(newValue.toString());
        }
    }

    /**
     * Utility method to return the correct value of a number pad key press based on
     * the specified layout of the keyboard
     * @param keyId - the id of the number pad key that was pressed
     * @return the string id of the value
     */
    private int getNumKeyStringId(int keyId) {
        int numberKeyValue;
        switch(keyId) {
            case R.id.r23:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.three : R.string.nine;
                break;
            case R.id.r22:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.two : R.string.eight;
                break;
            case R.id.r21:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.one : R.string.seven;
                break;
            case R.id.r33:
                numberKeyValue = R.string.six;
                break;
            case R.id.r31:
                numberKeyValue = R.string.four;
                break;
            case R.id.r32:
                numberKeyValue = R.string.five;
                break;
            case R.id.r43:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.nine : R.string.three;
                break;
            case R.id.r42:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.eight : R.string.two;
                break;
            case R.id.r41:
                numberKeyValue = numberpadModel.isStartWithOne() ? R.string.seven : R.string.one;
                break;
            case R.id.zero:
                numberKeyValue = R.string.zero;
                break;
            case R.id.decimal_point:
                numberKeyValue = R.string.decimal_point;
                break;
            default:
                Log.e(TAG, "An invalid number key was entered");
                numberKeyValue = -1;
        }

        return numberKeyValue;
    }
}
