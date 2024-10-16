package io.rednotice.attachment.controller;

import io.rednotice.attachment.entity.Attachment;
import io.rednotice.attachment.service.AttachmentService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachments")

public class AttachmentController {

    @Autowired
    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ApiResponse<Attachment> uploadFile(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam("file") MultipartFile file,
            @RequestParam Long cardId) {

        return ApiResponse.ok(attachmentService.uploadFile(file,cardId));
    }

    @GetMapping("/cards/{cardId}")
    public ApiResponse<List<Attachment>> getAttachments(@PathVariable Long cardId) {
        return ApiResponse.ok(attachmentService.getAttachment(cardId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAttachment(@PathVariable Long id) {
        return ApiResponse.ok(attachmentService.deleteAttachment(id));

    }
}
