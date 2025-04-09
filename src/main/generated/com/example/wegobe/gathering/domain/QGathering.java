package com.example.wegobe.gathering.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGathering is a Querydsl query type for Gathering
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGathering extends EntityPathBase<Gathering> {

    private static final long serialVersionUID = 216972968L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGathering gathering = new QGathering("gathering");

    public final com.example.wegobe.global.QBaseTimeEntity _super = new com.example.wegobe.global.QBaseTimeEntity(this);

    public final EnumPath<com.example.wegobe.gathering.domain.enums.Category> category = createEnum("category", com.example.wegobe.gathering.domain.enums.Category.class);

    public final DateTimePath<java.time.LocalDateTime> closedAt = createDateTime("closedAt", java.time.LocalDateTime.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final com.example.wegobe.auth.entity.QUser creator;

    public final DatePath<java.time.LocalDate> endAt = createDate("endAt", java.time.LocalDate.class);

    public final ListPath<HashTag, QHashTag> hashtags = this.<HashTag, QHashTag>createList("hashtags", HashTag.class, QHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final StringPath location = createString("location");

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final EnumPath<com.example.wegobe.gathering.domain.enums.AgeGroup> preferredAge = createEnum("preferredAge", com.example.wegobe.gathering.domain.enums.AgeGroup.class);

    public final EnumPath<com.example.wegobe.gathering.domain.enums.Gender> preferredGender = createEnum("preferredGender", com.example.wegobe.gathering.domain.enums.Gender.class);

    public final DatePath<java.time.LocalDate> startAt = createDate("startAt", java.time.LocalDate.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    public QGathering(String variable) {
        this(Gathering.class, forVariable(variable), INITS);
    }

    public QGathering(Path<? extends Gathering> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGathering(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGathering(PathMetadata metadata, PathInits inits) {
        this(Gathering.class, metadata, inits);
    }

    public QGathering(Class<? extends Gathering> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new com.example.wegobe.auth.entity.QUser(forProperty("creator")) : null;
    }

}

