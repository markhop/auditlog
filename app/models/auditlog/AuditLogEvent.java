package models.auditlog;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class AuditLogEvent extends Model {

    @Required
    public String model;

    @Required
    public String modelId;

    @Required
    @Enumerated(EnumType.STRING)
    public Operation operation;

    public enum Operation {
        CREATE, READ, UPDATE, DELETE
    }

    public String property;

    public String oldValue;

    public String newValue;

    public String actor;

    public Date createdAt;

    public String sessionId;
    
}
