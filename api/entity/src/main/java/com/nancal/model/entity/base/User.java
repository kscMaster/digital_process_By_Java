package com.nancal.model.entity.base;


import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Column(name = "creation_user_id", length = 64,updatable = false )
    private String creationUserId;

    @Column(name = "creation_username", length = 128,updatable = false)
    private String creationUsername;

    @Column(name = "last_update_user_id", length = 64)
    private String lastUpdateUserId;

    @Column(name = "last_update_username", length = 128)
    private String lastUpdateUsername;

}
