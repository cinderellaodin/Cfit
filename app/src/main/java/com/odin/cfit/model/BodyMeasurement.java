package com.odin.cfit.model;

public class BodyMeasurement {

    double userchest;
    double usershoulder;
    double userarm;
    double userneck;
    double userwaist;
    double userhip;
    double usercalf;
    double userwrist;
    double userthighs;
    double userdsize;

    public BodyMeasurement() {
    }

    public BodyMeasurement(double userchest, double usershoulder, double userarm, double userneck, double userwaist, double userhip, double usercalf, double userwrist, double userthighs, double userdsize) {
        this.userchest = userchest;
        this.usershoulder = usershoulder;
        this.userarm = userarm;
        this.userneck = userneck;
        this.userwaist = userwaist;
        this.userhip = userhip;
        this.usercalf = usercalf;
        this.userwrist = userwrist;
        this.userthighs = userthighs;
        this.userdsize = userdsize;
    }

    public double getUserchest() {
        return userchest;
    }

    public void setUserchest(double userchest) {
        this.userchest = userchest;
    }

    public double getUsershoulder() {
        return usershoulder;
    }

    public void setUsershoulder(double usershoulder) {
        this.usershoulder = usershoulder;
    }

    public double getUserarm() {
        return userarm;
    }

    public void setUserarm(double userarm) {
        this.userarm = userarm;
    }

    public double getUserneck() {
        return userneck;
    }

    public void setUserneck(double userneck) {
        this.userneck = userneck;
    }

    public double getUserwaist() {
        return userwaist;
    }

    public void setUserwaist(double userwaist) {
        this.userwaist = userwaist;
    }

    public double getUserhip() {
        return userhip;
    }

    public void setUserhip(double userhip) {
        this.userhip = userhip;
    }

    public double getUsercalf() {
        return usercalf;
    }

    public void setUsercalf(double usercalf) {
        this.usercalf = usercalf;
    }

    public double getUserwrist() {
        return userwrist;
    }

    public void setUserwrist(double userwrist) {
        this.userwrist = userwrist;
    }

    public double getUserthighs() {
        return userthighs;
    }

    public void setUserthighs(double userthighs) {
        this.userthighs = userthighs;
    }

    public double getUserdsize() {
        return userdsize;
    }

    public void setUserdsize(double userdsize) {
        this.userdsize = userdsize;
    }
}
