package eu.toqix.mlgrush.Utils;

public class TimeManager {
    public static StringBuilder getTimeString (int gameTime) {
        StringBuilder time = new StringBuilder();
        int hours;
        int minutes;
        int seconds;
        seconds = gameTime;
        minutes = seconds / 60;
        hours = minutes / 60;
        seconds = seconds - (minutes * 60) - (hours * 3600);
        minutes = minutes - (hours * 60);
        if(hours < 10) {
            time.append("0").append(hours);
        }else {
            time.append(hours);
        }
        if(minutes < 10) {
            time.append(":0").append(minutes);
        }else {
            time.append(":").append(minutes);
        }
        if(seconds < 10) {
            time.append(":0").append(seconds);
        }else {
            time.append(":").append(seconds);
        }
        return time;
    }
}
