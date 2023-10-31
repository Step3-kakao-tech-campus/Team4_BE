package com.ktc.matgpt.store;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreJPARepository extends JpaRepository<Store,Long> {

    @Query(value = "SELECT s FROM Store s JOIN FETCH s.subCategory sc JOIN FETCH sc.category c WHERE s.id = :id")
    Optional<Store> findById(@Param("id")Long id);

    //거리안의 음식점 marker 가져오기
    @Query(value ="SELECT * FROM store_tb WHERE latitude < :maxLat AND latitude > :minLat AND longitude < :maxLon AND longitude > :minLon", nativeQuery = true)
    List<Store> findAllWithinLatLonBoundaries(@Param("maxLat")double maxLat, @Param("maxLon")double maxLon, @Param("minLat")double minLat, @Param("minLon")double minLon);

    // mysql에서는 st_distance_sphere 함수를 사용가능하지만, 배포환경인 mariadb와 h2에서는 사용 불가하므로, 공식을 통해 거리 가져오기
    //사용자의 위치와 음식점 거리가 가까운 순으로 음식점 불러오기
    @Query(value = "WITH DistanceCTE AS (" +
            "SELECT s.*, " +
            "6371 * ACOS(" +
            "COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) * " +
            "COS(RADIANS(longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude))" +
            ") AS distance FROM store_tb s)" +
            "SELECT * FROM DistanceCTE " +
            "WHERE (distance > :cursor OR (distance = :cursor AND id < :lastid)) " +
            "ORDER BY distance ASC, id DESC",
            nativeQuery = true)
    List<Store> findAllByNearestAndDistanceLessThanCursor(@Param("latitude")double latitude, @Param("longitude")double longitude, @Param("cursor")Double cursor, @Param("lastid")Long lastId, Pageable pageable);

    //사용자의 위치와 음식점 거리가 가까운 순으로 음식점 불러오기
    @Query(value = "WITH DistanceCTE AS (" +
            "SELECT s.*, " +
            "6371 * ACOS(" +
            "COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) * " +
            "COS(RADIANS(longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude))" +
            ") AS distance FROM store_tb s)" +
            "SELECT * FROM DistanceCTE " +
            "ORDER BY distance ASC, id DESC",
            nativeQuery = true)
    List<Store> findAllByNearest(@Param("latitude")double latitude, @Param("longitude")double longitude, Pageable pageable);

    // 별점 높은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER BY s.ratingAvg DESC")
    List<Store> findAllBySearchOrderByRatingDesc(@Param("search")String search, Pageable pageable);

    // 별점 높은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s " +
            "WHERE s.name LIKE %:search% AND (s.ratingAvg < :cursor OR (s.ratingAvg = :cursor AND s.id < :lastid))" +
            "ORDER by s.ratingAvg DESC, s.id DESC")
    List<Store> findAllBySearchAndRatingLessThanCursor(@Param("search")String search, @Param("cursor")Long cursor, @Param("lastid")Long lastId, Pageable pageable);

    // id 내림차순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER by s.id DESC")
    List<Store> findAllBySearchOrderByIdDesc(@Param("search")String search, Pageable pageable);

    // id 내림차순으로 가게 불러오기 with cursor id
    @Query("SELECT s FROM Store s WHERE s.id < :cursor AND s.name LIKE %:search% ORDER by s.id DESC")
    List<Store> findAllBySearchAndIdLessThanCursor(@Param("search")String search, @Param("cursor")Long cursor, Pageable pageable);

    // 리뷰 많은 순으로 가게 불러오기
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:search% ORDER BY s.numsOfReview DESC, s.id DESC")
    List<Store> findAllBySearchOrderByNumsOfReviewDesc(@Param("search")String search, Pageable pageable);

    // 리뷰 많은 순으로 가게 불러오기 with cursor id
    @Query("SELECT s " +
            "FROM Store s " +
            "WHERE s.name LIKE %:search% AND (s.numsOfReview < :cursor OR (s.numsOfReview = :cursor AND s.id < :lastId))" +
            "ORDER BY s.numsOfReview DESC, s.id DESC")
    List<Store> findAllBySearchAndNumsOfReviewLessThanCursor(@Param("search")String search, @Param("cursor")int cursor, @Param("lastId")Long lastId, Pageable pageable);
}