package com.who.daola.data;

/**
 * Created by dave on 9/14/2014.
 */
public class Trigger {
    private long mTarget;
    private long mFence;
    private boolean mEnabled;
    private long mDuration;
    private int mTransition;

    public long getTarget() {
        return mTarget;
    }

    public void setTarget(long target) {
        this.mTarget = target;
    }

    public long getFence() {
        return mFence;
    }

    public void setFence(long fence) {
        this.mFence = fence;
    }

    public boolean enabled() {
        return mEnabled;
    }

    public void enable(boolean enabled) {
        this.mEnabled = enabled;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public int getTransitionType() {
        return mTransition;
    }

    public void setTransitionType(int transition) {
        this.mTransition = transition;
    }

    public boolean isTranstionTypeEnabled(int transitionType) {
        if ((mTransition & transitionType) > 0) {
            return true;
        }
        return false;
    }
}
