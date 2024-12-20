package com.dzaza.posts.repo;

import com.dzaza.posts.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>
{
}
