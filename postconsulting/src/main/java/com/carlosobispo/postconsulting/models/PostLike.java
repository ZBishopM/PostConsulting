package com.carlosobispo.postconsulting.models;

import com.carlosobispo.postconsulting.models.enums.Type;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "post_likes")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class PostLike extends BaseModel {
    @NotNull
    private Type type;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    @Nullable
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
