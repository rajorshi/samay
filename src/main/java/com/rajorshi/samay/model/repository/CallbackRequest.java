package com.rajorshi.samay.model.repository;

import lombok.*;

import javax.persistence.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Entity
@Getter
@ToString(exclude = {"target"})
@NoArgsConstructor
@Table(name="timed_callbacks")
@NamedNativeQueries({
        @NamedNativeQuery(
        name = CallbackRequest.QUERY_GET_PENDING_FOR_UPDATE
        , query = "select * from timed_callbacks " +
                "where namespace = ?1 and status = \'Pending\' or status = \'Retry\' and call_at < ?2 limit ?3 for update"
        , resultClass = CallbackRequest.class
        , hints={@QueryHint(name="javax.persistence.query.timeout", value="1")}
        ),
        @NamedNativeQuery(
                name = CallbackRequest.QUERY_PURGE_BY_DATE
                , query = "delete from timed_callbacks " +
                "where not (status = \'Pending\' or status = \'Retry\') and call_at < ?1 limit ?2"
                , resultClass = CallbackRequest.class
                , hints={@QueryHint(name="javax.persistence.query.timeout", value="1")}
        )
})
//@NamedQueries({
//        @NamedQuery(
//                name = CallbackRequest.QUERY_GET_PENDING_FOR_UPDATE
//                , query = "select * from timed_callbacks " +
//                "where called_at = 0 and expired = 0 and call_at < :until limit :limit"
//        , hints={@QueryHint(name="javax.persistence.query.timeout", value="1")}
//        )
//})
public class CallbackRequest extends BasicEntity {

    public static final String QUERY_GET_PENDING_FOR_UPDATE = "CallbackRequest.getPendingCallbacksForUpdate";
    public static final String QUERY_PURGE_BY_DATE = "CallbackRequest.purgeCallbacksByDate";

    @Column(name = "namespace", length = 64, nullable = false)
    private String namespace;

    @Column(name = "ext_ref_id", length = 32, nullable = false)
    private String extRefId;

    @Column(name = "src", length = 64, nullable = false)
    private String source;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Column(name = "target_url", length = 1024, nullable = false)
    private String targetUrl;

    @Column(name = "data", length = 512, nullable = false)
    private String data;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_at", nullable = false)
    private Date callAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "called_at")
    private Date calledAt;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Transient
    private URI target;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, columnDefinition = "varchar(32) default 'Pending'")
    private RequestStatus status;

    @Column(name = "status_reason", length = 512)
    private String statusReason;

    @Builder
    private CallbackRequest(
            String namespace
            , String extRefId
            , String source
            , String data
            , Date callAt
            , Date calledAt
            , URI target
    ) {
        this.namespace = namespace;
        this.extRefId = extRefId;
        this.source = source;
        this.data = data;
        this.callAt = callAt;
        this.calledAt = calledAt;
        this.status = RequestStatus.Pending;
        setTarget(target);
    }

    private void setTarget(URI url) {
        this.target = url;
        this.targetUrl = url.toString();
    }

    public URI getTarget() throws URISyntaxException {
        if (target == null) {
            this.target = new URI(this.targetUrl);
        }
        return target;
    }

    public CallbackRequest status(RequestStatus status, String reason) {
        this.status = status;
        this.statusReason = reason;
        return this;
    }

    public CallbackRequest calledAt(Date date) {
        calledAt = date;
        return this;
    }

}
