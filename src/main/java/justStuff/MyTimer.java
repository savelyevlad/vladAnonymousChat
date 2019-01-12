package justStuff;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Timer;

public class MyTimer extends Timer {

    public  MyTimer(User user) {
        super();
        this.user = user;
    }

    public MyTimer(User user, boolean isDaemon) {
        super(isDaemon);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
}
