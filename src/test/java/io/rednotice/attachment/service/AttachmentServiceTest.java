package io.rednotice.attachment.service;

import io.rednotice.attachment.entity.Attachment;
import io.rednotice.attachment.repository.AttachmentRepository;
import io.rednotice.card.entity.Card;
import io.rednotice.card.service.CardService;
import io.rednotice.common.AttachmentValidation;
import io.rednotice.common.utils.S3ServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private S3ServiceUtil s3ServiceUtil;

    @Mock
    private CardService cardService;

    @InjectMocks
    private AttachmentService attachmentService;

    @Mock
    private MultipartFile multipartFile;

    private Card card;
    private Attachment attachment;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setId(1L);

        attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFileUrl(("https://example.com/file.txt"));
        attachment.setFileName("file.txt");
        attachment.setFileType("text/plain");
        attachment.setCard(card);

    }


    @Test
    void uploadFile_Success() {
        // Given
        when(cardService.getCard(anyLong())).thenReturn(card);
        when(s3ServiceUtil.uploadFile(any(MultipartFile.class), anyString())).thenReturn("https://example.com/file.txt");
        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);

        try (MockedStatic<AttachmentValidation> mockedValidation = mockStatic(AttachmentValidation.class)) {
            mockedValidation.when(() -> AttachmentValidation.validationFile(any(MultipartFile.class))).then(invocation -> null);

            // When
            Attachment result = attachmentService.uploadFile(multipartFile, 1L);

            // Then
            assertNotNull(result);
            assertEquals("https://example.com/file.txt", result.getFileUrl());
            assertEquals("file.txt", result.getFileName());
            assertEquals("text/plain", result.getFileType());
            assertEquals(card, result.getCard());

            verify(cardService).getCard(1L);
            verify(s3ServiceUtil).uploadFile(multipartFile, "rednotice-sample");
            verify(attachmentRepository).save(any(Attachment.class));
        }
    }

    @Test
    void getAttachment_Success() {
        // Given
        List<Attachment> attachments = Arrays.asList(attachment);
        when(attachmentRepository.findByCardId(anyLong())).thenReturn(attachments);

        // When
        List<Attachment> result = attachmentService.getAttachment(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attachment, result.get(0));

        verify(attachmentRepository).findByCardId(1L);
    }

    @Test
    void deleteAttachment_Success() {
        // Given
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.of(attachment));

        // When
        assertDoesNotThrow(() -> attachmentService.deleteAttachment(1L));

        // Then
        verify(attachmentRepository).findById(1L);
        verify(s3ServiceUtil).deleteFile("https://example.com/file.txt", "rednotice-sample");
        verify(attachmentRepository).delete(attachment);
    }

}