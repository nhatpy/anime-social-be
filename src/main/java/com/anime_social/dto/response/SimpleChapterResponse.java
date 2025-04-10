package com.anime_social.dto.response;

import java.util.Date;

import com.anime_social.models.Chapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SimpleChapterResponse {
    Integer chapterNumber;
    Date createAt;
    Date updateAt;

    public static SimpleChapterResponse toSimpleChapterResponse(Chapter chapter) {
        return SimpleChapterResponse.builder()
                .chapterNumber(chapter.getChapterNumber())
                .createAt(chapter.getCreateAt())
                .updateAt(chapter.getUpdateAt())
                .build();
    }
}
