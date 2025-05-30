package com.anime_social.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.HistoryReadRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.HistoryListMangaResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Manga;
import com.anime_social.models.MangaInteraction;
import com.anime_social.models.User;
import com.anime_social.models.UserReadManga;
import com.anime_social.repositories.MangaInteractionRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserReadMangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.HistoryReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryReadServiceImpl implements HistoryReadService {
        private final UserReadMangaRepository userReadMangaRepository;
        private final UserRepository userRepository;
        private final MangaRepository mangaRepository;
        private final MangaInteractionRepository mangaInteractionRepository;

        @Override
        public AppResponse read(String userId, String mangaId, HistoryReadRequest historyReadRequest) {
                Manga manga = mangaRepository.findById(mangaId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                manga.setView(manga.getView() + 1);
                Manga savedManga = mangaRepository.save(manga);
                Optional<MangaInteraction> mangaInteraction = mangaInteractionRepository.findById(mangaId);

                if (mangaInteraction.isEmpty()) {
                        MangaInteraction newMangaInteraction = MangaInteraction.builder()
                                        .manga(manga)
                                        .build();
                        mangaInteractionRepository.save(newMangaInteraction);
                }
                if (mangaInteraction.isPresent()) {
                        MangaInteraction mangaInteraction2 = mangaInteraction.get();
                        mangaInteraction2.setTime(mangaInteraction2.getTime() + 1);
                        mangaInteractionRepository.save(mangaInteraction2);
                }

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

                if (userReadManga.isEmpty()) {
                        UserReadManga saveUserReadManga = UserReadManga.builder()
                                        .user(user)
                                        .manga(savedManga)
                                        .lastReadAtChapter(historyReadRequest.getReadChapter())
                                        .lastReadAtDate(historyReadRequest.getReadDate())
                                        .build();

                        UserReadManga history = userReadMangaRepository.save(saveUserReadManga);

                        return AppResponse.builder()
                                        .status(HttpStatus.CREATED)
                                        .data(HistoryListMangaResponse.toHistoryListResponse(history))
                                        .message("Đã thêm vào lịch sử theo dõi")
                                        .build();
                }
                if (mangaInteraction.isPresent()) {
                        UserReadManga userReadManga2 = userReadManga.get();
                        userReadManga2.setLastReadAtChapter(historyReadRequest.getReadChapter());
                        userReadManga2.setLastReadAtDate(historyReadRequest.getReadDate());

                        UserReadManga savedHistory = userReadMangaRepository.save(userReadManga2);

                        return AppResponse.builder()
                                        .status(HttpStatus.OK)
                                        .data(HistoryListMangaResponse.toHistoryListResponse(savedHistory))
                                        .message("Đã cập nhật lịch sử theo dõi")
                                        .build();
                }
                throw new CusRunTimeException(ErrorCode.ERROR_IS_UNCATEGORIZED);
        }

        @Override
        public AppResponse unread(String userId, String mangaId) {
                Optional<UserReadManga> userReadManga = userReadMangaRepository.findByUserIdAndMangaId(userId, mangaId);

                if (userReadManga.isEmpty()) {
                        throw new CusRunTimeException(ErrorCode.HISTORY_NOT_FOUND);
                }

                userReadMangaRepository.delete(userReadManga.get());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã xóa truyện khỏi lịch sử theo dõi")
                                .build();
        }

        @Override
        public AppResponse getHistoryListMangaPaging(String userId, int page, int size) {
                int staterPage = page - 1;
                Pageable pageable = PageRequest.of(staterPage, size).withSort(Sort.by("lastReadAtDate").descending());

                Integer total = userReadMangaRepository.countByUserIdAndMangaId(userId).orElse(0);
                List<UserReadManga> userReadMangas = userReadMangaRepository.findAllByUserIdPaging(userId, pageable);

                List<HistoryListMangaResponse> historyListMangaResponses = userReadMangas.stream()
                                .map(HistoryListMangaResponse::toHistoryListResponse)
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(historyListMangaResponses)
                                .totalItem(total)
                                .build();
        }
}
