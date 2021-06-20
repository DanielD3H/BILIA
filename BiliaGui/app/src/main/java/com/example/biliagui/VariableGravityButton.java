package com.example.biliagui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

public class VariableGravityButton extends androidx.appcompat.widget.AppCompatButton {
    public VariableGravityButton(Context context) {
        super(context);
    }

    public VariableGravityButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VariableGravityButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed != isPressed()) {
            //setGravity(pressed ? Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM : Gravity.CENTER);
            if(pressed)
                setPadding(0,10,0,0);
            else
                setPadding(0,0,0,0);
        }
        super.setPressed(pressed);
    }
}