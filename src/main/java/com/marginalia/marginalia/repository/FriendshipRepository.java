package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.Friendship;
import com.marginalia.marginalia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // Get all friends of a user
    List<Friendship> findByUserId(Long userId);

    // Check if friendship exists
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    // Find specific friendship
    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);

    // Get friend IDs (useful for queries)
    @Query("SELECT f.friend.id FROM Friendship f WHERE f.user.id = :userId")
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);

    // Count friends
    long countByUserId(Long userId);
}