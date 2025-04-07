package com.example.wegobe.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1437304524L;

    public static final QUser user = new QUser("user");

    public final EnumPath<com.example.wegobe.gathering.domain.enums.AgeGroup> ageGroup = createEnum("ageGroup", com.example.wegobe.gathering.domain.enums.AgeGroup.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.example.wegobe.gathering.domain.enums.Gender> gender = createEnum("gender", com.example.wegobe.gathering.domain.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath statusMessage = createString("statusMessage");

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

