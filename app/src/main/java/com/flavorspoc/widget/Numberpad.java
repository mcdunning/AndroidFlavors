package com.flavorspoc.widget;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flavorspoc.R;

public class Numberpad extends ConstraintLayout {
    private final String TAG = getClass().getCanonicalName();

    private View anchor;

    private float paddingTop = 0.0f;
    private float paddingStart = 0.0f;

    private NumberpadModel numberpadModel;
    private View.OnClickListener onConfirm;

    private ViewGroup mainView;
    private TextView value;
    private ImageButton one;
    private ImageButton two;
    private ImageButton three;
    private ImageButton four;
    private ImageButton five;
    private ImageButton six;
    private ImageButton seven;
    private ImageButton eight;
    private ImageButton nine;
    private ImageButton zero;
    private ImageButton decimal_point;
    private ImageButton submit;
    private ImageButton backspace;

    // boolean flag to replace the current value with the value of the first button clicked
    private boolean handleFirstButtonClick = true;
    private View.OnClickListener numberpadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nine:
                    handleNumberPress(R.string.nine);
                    break;
                case R.id.eight:
                    handleNumberPress(R.string.eight);
                    break;
                case R.id.seven:
                    handleNumberPress(R.string.seven);
                    break;
                case R.id.six:
                    handleNumberPress(R.string.six);
                    break;
                case R.id.five:
                    handleNumberPress(R.string.five);
                    break;
                case R.id.four:
                    handleNumberPress(R.string.four);
                    break;
                case R.id.three:
                    handleNumberPress(R.string.three);
                    break;
                case R.id.two:
                    handleNumberPress(R.string.two);
                    break;
                case R.id.one:
                    handleNumberPress(R.string.one);
                    break;
                case R.id.zero:
                    handleNumberPress(R.string.zero);
                    break;
                case R.id.decimal_point:
                    handleNumberPress(R.string.decimal_point);
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

    public Numberpad(Context context) {
        this(context, null, 0);
    }

    public Numberpad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Numberpad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.activity_numerpad, this, true);

        mainView = findViewById(R.id.main_view);
        value = findViewById(R.id.value);
        backspace = findViewById(R.id.backspace);
        nine = findViewById(R.id.nine);
        eight = findViewById(R.id.eight);
        seven = findViewById(R.id.seven);
        six = findViewById(R.id.six);
        five = findViewById(R.id.five);
        four = findViewById(R.id.four);
        three = findViewById(R.id.three);
        two = findViewById(R.id.two);
        one = findViewById(R.id.one);
        zero = findViewById(R.id.zero);
        decimal_point = findViewById(R.id.decimal_point);
        submit = findViewById(R.id.submit);
    }

    public void init (final View anchor, final NumberpadModel numberpadModel, final View.OnClickListener onConfirm) {
        this.anchor = anchor;
        this.numberpadModel = numberpadModel;
        this.onConfirm = onConfirm;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void show() {

        // Assign the listeners to the numberpad keys
        backspace.setOnClickListener(numberpadButtonClickListener);
        nine.setOnClickListener(numberpadButtonClickListener);
        eight.setOnClickListener(numberpadButtonClickListener);
        seven.setOnClickListener(numberpadButtonClickListener);
        six.setOnClickListener(numberpadButtonClickListener);
        five.setOnClickListener(numberpadButtonClickListener);
        four.setOnClickListener(numberpadButtonClickListener);
        three.setOnClickListener(numberpadButtonClickListener);
        two.setOnClickListener(numberpadButtonClickListener);
        one.setOnClickListener(numberpadButtonClickListener);
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

            // Move the numpadcontainer to the middle of the anchor.
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
                    ViewGroup rootView = (ViewGroup) Numberpad.this.getParent();
                    if (rootView != null) {
                        rootView.removeView(Numberpad.this);
                    }

                    mainView.setScaleX(1);
                    mainView.setScaleY(1);
                    mainView.setTranslationX(paddingStart);
                    mainView.setTranslationY(paddingTop);
                    Numberpad.this.setAlpha(1);

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

    private void cleanup() {
        mainView.setOnTouchListener(null);

        backspace.setOnClickListener(null);
        nine.setOnClickListener(null);
        eight.setOnClickListener(null);
        seven.setOnClickListener(null);
        six.setOnClickListener(null);
        five.setOnClickListener(null);
        four.setOnClickListener(null);
        three.setOnClickListener(null);
        two.setOnClickListener(null);
        one.setOnClickListener(null);
        zero.setOnClickListener(null);
        decimal_point.setOnClickListener(null);
        submit.setOnClickListener(null);

        backspace = null;
        nine = null;
        eight = null;
        seven = null;
        six = null;
        five = null;
        four = null;
        three = null;
        two = null;
        one = null;
        zero = null;
        decimal_point = null;
        submit = null;

        anchor = null;
        numberpadModel = null;
        onConfirm = null;
    }
}
