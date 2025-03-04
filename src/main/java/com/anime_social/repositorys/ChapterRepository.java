package com.anime_social.repositorys;

import com.anime_social.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Chapter findByChapterNumber(Integer chapterNumber);
}
