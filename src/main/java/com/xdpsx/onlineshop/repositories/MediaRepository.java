package com.xdpsx.onlineshop.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.xdpsx.onlineshop.entities.Media;

public interface MediaRepository extends CrudRepository<Media, String> {
    @Query("SELECT m FROM Media m WHERE m.deleteFlg = true")
    List<Media> findDeletedMedia();

    @Query("""
		SELECT m FROM Media m
		WHERE m.tempFlg = true AND m.createdAt < :expiryTime
		""")
    List<Media> findExpiredMedia(@Param("expiryTime") LocalDateTime expiryTime);
}
