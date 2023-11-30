package com.siseth.adapter.repository.source;

import com.siseth.adapter.entity.source.ImageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageSourceRepository extends JpaRepository<ImageSource, Long> {

    List<ImageSource> findAllByIsActiveIsTrueAndUserIdAndRealm(String userId, String realm);

    Integer countAllByIsActiveIsTrueAndUserIdAndRealm(String userId, String realm);

    List<ImageSource> findAllByUserIdAndRealm(String userId, String realm);

    boolean existsByUserIdAndNameAndRealmAndIsActiveIsTrue(String userId, String name, String realm);

    List<ImageSource> findAllByIdIn(List<Long> ids);

    @Query("SELECT CASE WHEN count(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ImageSource s " +
            "WHERE s.isActive = TRUE AND " +
            "      s.ip = :#{#source.ip} AND " +
            "      s.port = :#{#source.port} AND " +
            "      s.id <> :#{#source.id}")
    boolean checkConfigExists(@Param("source") ImageSource source);

    @Query("SELECT s " +
            "FROM ImageSource s " +
            "WHERE s.isActive = TRUE AND " +
            "      (:userId = '' OR s.userId = :userId) AND " +
            "      s.realm = :realm AND " +
            "      s.recorded IN :recordedType AND " +
            "      s.type IN :sourceType AND " +
            "      (0L IN :sourceIds OR s.id IN :sourceIds) AND " +
            "      (:filter = '' OR " +
            "               UPPER(s.place) LIKE CONCAT('%', UPPER(:filter), '%')  OR " +
            "               UPPER(s.name) LIKE CONCAT('%', UPPER(:filter), '%') OR " +
            "               UPPER(s.recorded) LIKE CONCAT('%', UPPER(:filter), '%') )")
    Page<ImageSource> findAllByUserIdAndRealmAndIsActiveIsTrue(@Param("userId") String userId,
                                                               @Param("realm") String realm,
                                                               @Param("filter") String filter,
                                                               @Param("sourceType") List<ImageSource.SourceType> sourceType,
                                                               @Param("recordedType") List<ImageSource.SourceRecorded> recordedType,
                                                               @Param("sourceIds") List<Long> sourceIds,
                                                               Pageable pageable);

    List<ImageSource> findAllByStationIdIsNotNull();


    List<ImageSource> findAllByRealm(String realm);

    Optional<ImageSource> findByIdAndRealm(Long id, String realm);
    List<ImageSource> findAllByTypeAndRealmAndIsActiveIsTrue(ImageSource.SourceType type, String realm);

    List<ImageSource> findAllByTypeAndIsActiveIsTrue(ImageSource.SourceType type);

    List<ImageSource> findAllByTypeInAndRealmAndIsActiveIsTrue(List<ImageSource.SourceType> types, String realm);
    List<ImageSource> findAllByTypeInAndIsActiveIsTrue(List<ImageSource.SourceType> types);

    @Query("SELECT s " +
            "FROM ImageSource s " +
            "INNER JOIN SourceConfig c ON c.source = s AND c.type = 'FTP' " +
            "WHERE s.synchronize = TRUE AND s.type IN :types AND c.enable = TRUE ")
    List<ImageSource> getAllToSynchronizeWithFTP(@Param("types") List<ImageSource.SourceType> types);

}
