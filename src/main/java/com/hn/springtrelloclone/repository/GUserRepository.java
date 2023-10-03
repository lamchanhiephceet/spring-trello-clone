package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GUserRepository extends JpaRepository<GUser, Long> {
    Optional<GUser> findByUsername(String username);
    Optional<GUser> findByEmail(String email);
//    @Query("SELECT user FROM GUser user WHERE user.username like %:search%")
//    Optional<List<GUser>> findMemberByNamedParams(@Param("search") String search);
}
