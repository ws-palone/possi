package fr.istic.iodeman.Audit;

import org.apache.commons.lang.Validate;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class TimeBoxAudit {

    private Date from;
    private Date to;

    public TimeBoxAudit() {

    }

    public TimeBoxAudit(Date from, Date to) {
        setFrom(from);
        setTo(to);
    }

    public Date getFrom() {
        return from;
    }
    public void setFrom(Date from) {
        this.from = from;
    }
    public Date getTo() {
        return to;
    }
    public void setTo(Date to) {
        this.to = to;
    }

    public void validate() {

        Validate.notNull(from);
        Validate.notNull(to);
        Validate.isTrue(from.before(to) || from.equals(to));
    }
}
