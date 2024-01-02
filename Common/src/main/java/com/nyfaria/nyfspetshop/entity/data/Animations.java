package com.nyfaria.nyfspetshop.entity.data;

import software.bernie.geckolib.core.animation.RawAnimation;

public class Animations {
    public static RawAnimation IDLE = RawAnimation.begin().thenLoop("sit");
    public static RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    public static RawAnimation SLEEP = RawAnimation.begin().thenLoop("sleep");
    public static RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    public static RawAnimation TAIL_WAG_SIT = RawAnimation.begin().thenLoop("tail_wag_sit");
    public static RawAnimation TAIL_SIT_IDLE = RawAnimation.begin().thenLoop("tail_sit_idle");
    public static RawAnimation TAIL_WAG_STAND = RawAnimation.begin().thenLoop("tail_wag_stand");
    public static RawAnimation EAR_WIGGLE = RawAnimation.begin().thenPlay("ear_wiggle");
    public static RawAnimation EAR_WIGGLE3 = RawAnimation.begin().thenPlayXTimes("ear_wiggle",3);
    public static RawAnimation EAR_IDLE = RawAnimation.begin().thenPlay("ear_idle");

}
