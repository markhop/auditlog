package play.modules.auditlog;

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import play.db.jpa.Model;

public class AuditLogListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    public void onPostInsert(PostInsertEvent event) {
    	Model entity = (Model) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] values = event.getState();
            for (int i=0; i<properties.length; i++) {
            	String value = values[i] == null ? "NULL" : values[i].toString();
            	// if we have a password field then ensure we don't log the actual value
            	try {
					if(entity.getClass().getField(properties[i]).isAnnotationPresent(PasswordField.class)) {
						value = "******";
					}
				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {
				}
                AuditLog.invoke(
                        "onCreate",
                        model,
                        modelId,
                        properties[i],
                        value
                );
            }
        }
    }

    public void onPostUpdate(PostUpdateEvent event) {
    	Model entity = (Model) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] oldValues = event.getOldState();
            Object[] values = event.getState();
            for (int i=0; i<properties.length; i++) {
                boolean updated = false;
                if (oldValues[i] == null) {
                    if (values[i] != null) {
                        updated = true;
                    }
                } else if (!oldValues[i].equals(values[i])) {
                    updated = true;
                }
                if (updated) {
                	String oldValue = oldValues[i] == null ? "NULL" : oldValues[i].toString();
                	String newValue = values[i] == null ? "NULL" : values[i].toString();
                	// if we have a password field then ensure we don't log the actual value
                	try {
    					if(entity.getClass().getField(properties[i]).isAnnotationPresent(PasswordField.class)) {
    						oldValue = "******";
    						newValue = "******";
    					}
    				} catch (SecurityException e) {
    				} catch (NoSuchFieldException e) {
    				}
                    AuditLog.invoke(
                            "onUpdate",
                            model,
                            modelId,
                            properties[i],
                            oldValue,
                            newValue
                    );
                }
            }
        }
    }

    public void onPostDelete(PostDeleteEvent event) {
    	Model entity = (Model) event.getEntity();
        if (entity.getClass().isAnnotationPresent(Auditable.class)) {
            String model = entity.getClass().getName().replaceFirst("models.", "");
            String modelId = entity.getId().toString();
            String[] properties = event.getPersister().getPropertyNames();
            Object[] values = event.getDeletedState();
            for (int i=0; i<properties.length; i++) {
            	String value = values[i] == null ? "NULL" : values[i].toString();
            	// if we have a password field then ensure we don't log the actual value
            	try {
					if(entity.getClass().getField(properties[i]).isAnnotationPresent(PasswordField.class)) {
						value = "******";
					}
				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {
				}
                AuditLog.invoke(
                        "onDelete",
                        model,
                        modelId,
                        properties[i],
                        value
                );
            }
        }
    }

}
