package com.fg.tltmod.api.animation;

public class ControlledAnimation {
    /**
     * 用于动画的计时器
     */
    private int timer;
    private int prevtimer;

    /**
     * 动画的最大时间，即计时器能达到的最大值。
     * 它表示动画的持续时间
     */
    private int duration;

    private int timerChange;

    public ControlledAnimation(int d) {
        timer = 0;
        prevtimer = 0;
        duration = d;
    }

    /**
     * 设置动画的持续时间（以 tick 为单位）。推荐值大约在 50。
     *
     * @param d 计时器能达到的最大 tick 数。
     */
    public void setDuration(int d) {
        timer = 0;
        prevtimer = 0;
        duration = d;
    }

    /**
     * 返回当前动画的计时器值。可用于保存动画的进度。
     */
    public int getTimer() {
        return timer;
    }

    public int getPrevTimer() {
        return prevtimer;
    }

    /**
     * 将计时器设置为一个指定的值。
     *
     * @param time 要设置的 tick 数。
     */
    public void setTimer(int time) {
        timer = time;
        prevtimer = time;

        if (timer > duration) {
            timer = duration;
        } else if (timer < 0) {
            timer = 0;
        }
    }

    /**
     * 将计时器重置为 0。
     */
    public void resetTimer() {
        timer = 0;
        prevtimer = 0;
    }

    /**
     * 将计时器增加 1。
     */
    public void increaseTimer() {
        if (timer < duration) {
            timer++;
            timerChange = 1;
        }
    }

    /**
     * 检查计时器是否还能增加
     */
    public boolean canIncreaseTimer() {
        return timer < duration;
    }

    /**
     * 将计时器增加指定的值。
     *
     * @param time 要增加的 tick 数
     */
    public void increaseTimer(int time) {
        int newTime = timer + time;
        if (newTime <= duration && newTime >= 0) {
            timer = newTime;
        } else {
            timer = newTime < 0 ? 0 : duration;
        }
    }

    /**
     * 将计时器减少 1。
     */
    public void decreaseTimer() {
        if (timer > 0.0D) {
            timer--;
            timerChange = -1;
        }
    }

    /**
     * 检查计时器是否还能减少
     */
    public boolean canDecreaseTimer() {
        return timer > 0.0D;
    }

    /**
     * 将计时器减少指定的值。
     *
     * @param time 要减少的 tick 数
     */
    public void decreaseTimer(int time) {
        if (timer - time > 0.0D) {
            timer -= time;
        } else {
            timer = 0;
        }
    }

}
