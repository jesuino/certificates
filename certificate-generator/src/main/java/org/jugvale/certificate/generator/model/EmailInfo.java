package org.jugvale.certificate.generator.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * 
 * Tracks sent emails 
 *
 */
@Entity
public class EmailInfo extends PanacheEntity {

    public Date sentDate;

    public String subject;

    @Lob
    public String body;

    // soft key
    public long certificateId;
}
