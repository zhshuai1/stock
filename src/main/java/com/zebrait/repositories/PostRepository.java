package com.zebrait.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zebrait.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
