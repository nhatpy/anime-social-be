package com.anime_social.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.ChatbotRequest;
import com.anime_social.models.ChatbotHistory;
import com.anime_social.models.User;
import com.anime_social.repositories.CategoryRepository;
import com.anime_social.repositories.ChatbotRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.ChatbotService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {
    private final ChatbotRepository chatbotRepository;
    private final MangaRepository mangaRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<String> getHistory(String userId) {
        Pageable pageable = PageRequest.of(0, 2).withSort(Sort.by(Sort.Direction.DESC, "createAt"));
        return chatbotRepository.findAllMessageByUserId(userId, pageable);
    }

    @Override
    public String sendMessage(ChatbotRequest chatbotRequest) {
        List<String> genres = this.extractGenres(chatbotRequest.getMessage());
        if (genres.isEmpty()) {
            return "Tôi không hiểu bạn nói gì.";
        }

        Pageable pageable = PageRequest.of(0, 4);
        List<String> mangaNames = mangaRepository.findTop4MangaTitlesByGenres(genres, pageable);
        if (mangaNames.isEmpty()) {
            return "Không tìm thấy manga nào phù hợp với thể loại bạn đã cung cấp.";
        }

        chatbotRequest.getUserId().ifPresent(userId -> {
            if (userRepository.existsById(userId) && !chatbotRepository.existsByMessage(chatbotRequest.getMessage())) {
                User user = userRepository.findById(userId).orElse(null);

                ChatbotHistory userChatbotHistory = ChatbotHistory.builder()
                        .user(user)
                        .message(chatbotRequest.getMessage())
                        .build();

                chatbotRepository.save(userChatbotHistory);
            }
        });
        StringBuilder response = new StringBuilder("Dưới đây là một số manga phù hợp với thể loại bạn đã cung cấp: ");
        response.append(String.join(", ", mangaNames));
        return response.toString();
    }

    private List<String> extractGenres(String message) {
        List<String> genres = new ArrayList<>();

        List<String> allGenres = categoryRepository.findAllCategoryName();
        for (String genre : allGenres) {
            if (message.toLowerCase().contains(genre.toLowerCase())) {
                genres.add(genre);
            }
        }

        return genres;
    }

}
