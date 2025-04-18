package com.anime_social.dto.response;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.anime_social.models.Manga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MangaResponse {
        String id;
        String name;
        String slug;
        String description;
        String coverImage;
        Integer view;
        Integer follow;
        Boolean isDone;
        Boolean isActive;
        String authorName;
        List<CategoryOption> categories;
        List<SimpleChapterResponse> chapters;
        Date createAt;
        Date updateAt;

        public static MangaResponse toMangaResponse(Manga manga) {
                List<CategoryOption> categories = Optional.ofNullable(manga.getCategoryMangas())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(categoryManga -> {
                                        String categoryName = categoryManga.getCategory().getName();
                                        String categoryId = categoryManga.getCategory().getId();
                                        return CategoryOption.builder()
                                                        .value(categoryId)
                                                        .label(categoryName)
                                                        .build();

                                })
                                .collect(Collectors.toList());
                List<SimpleChapterResponse> chapters = Optional.ofNullable(manga.getChapters())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(SimpleChapterResponse::toSimpleChapterResponse)
                                .sorted(Comparator.comparing(
                                                (SimpleChapterResponse chapter) -> chapter.getChapterNumber())
                                                .reversed())
                                .collect(Collectors.toList());

                return MangaResponse.builder()
                                .id(manga.getId())
                                .name(manga.getName())
                                .slug(manga.getSlug())
                                .description(manga.getDescription())
                                .coverImage(manga.getCoverImage())
                                .view(manga.getView())
                                .follow(manga.getFollow())
                                .isDone(manga.getIsDone())
                                .isActive(manga.getIsActive())
                                .authorName(manga.getAuthor().getFullName())
                                .categories(categories)
                                .chapters(chapters)
                                .createAt(manga.getCreateAt())
                                .updateAt(manga.getUpdateAt())
                                .build();
        }
}
