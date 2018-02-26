package basketball;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class Landing implements Comparable<Landing> {
    @Parent Key<Guestbook> guestbookName;
    @Id Long id;
    @Index User user;
    @Index String content;
    @Index String title;
    @Index Date date;
    private Landing() {}
    public Landing(User user, String content, String guestbookName, String title) {
        this.user = user;
        this.content = content;
        this.guestbookName = Key.create(Guestbook.class, guestbookName);
        date = new Date();
        this.title = title;
    }
    public User getUser() {
        return user;
    }
    public String getContent() {
        return content;
    }
    
    public Date getDate() {
    		return date;
    }
    
    public String getTitle() {
    		return title;
    }

    @Override
    public int compareTo(Landing other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
     }
}
