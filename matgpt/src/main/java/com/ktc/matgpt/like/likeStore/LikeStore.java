package com.ktc.matgpt.like.likeStore;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likestore_tb")
public class LikeStore {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    private LikeStore(User user, Store store){
        this.user = user;
        this.store = store;
    }
    @Builder
    public static LikeStore create(User user, Store store) {
        return new LikeStore(user,store);
    }

}
