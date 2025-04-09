package com.example.wegobe.gathering.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGatheringMember is a Querydsl query type for GatheringMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGatheringMember extends EntityPathBase<GatheringMember> {

    private static final long serialVersionUID = 1118213986L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGatheringMember gatheringMember = new QGatheringMember("gatheringMember");

    public final QGathering gathering;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.wegobe.gathering.domain.enums.GatheringStatus> status = createEnum("status", com.example.wegobe.gathering.domain.enums.GatheringStatus.class);

    public final com.example.wegobe.auth.entity.QUser user;

    public QGatheringMember(String variable) {
        this(GatheringMember.class, forVariable(variable), INITS);
    }

    public QGatheringMember(Path<? extends GatheringMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGatheringMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGatheringMember(PathMetadata metadata, PathInits inits) {
        this(GatheringMember.class, metadata, inits);
    }

    public QGatheringMember(Class<? extends GatheringMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gathering = inits.isInitialized("gathering") ? new QGathering(forProperty("gathering"), inits.get("gathering")) : null;
        this.user = inits.isInitialized("user") ? new com.example.wegobe.auth.entity.QUser(forProperty("user")) : null;
    }

}

