package com.anime_social.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anime_social.dto.request.PostComment;
import com.anime_social.dto.request.UpdateComment;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/create")
    public AppResponse createComment(@RequestBody @Valid PostComment request) {
        return commentService.createComment(request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public AppResponse deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/create/{id}")
    public AppResponse updateComment(@PathVariable String id, @RequestBody UpdateComment request) {
        return commentService.updateComment(id, request);
    }
}