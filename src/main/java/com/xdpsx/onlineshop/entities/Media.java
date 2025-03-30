package com.xdpsx.onlineshop.entities;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.xdpsx.onlineshop.entities.enums.MediaResourceType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "media")
@EntityListeners(AuditingEntityListener.class)
public class Media extends AuditEntity {
    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 50, nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private String url;

    @Column(length = 100)
    private String caption;

    @Column(length = 30, nullable = false)
    private String contentType;

    @Enumerated(EnumType.ORDINAL)
    private MediaResourceType resourceType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tempFlg;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteFlg;
}
