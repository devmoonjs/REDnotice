package io.rednotice.attachment.service;

import io.rednotice.attachment.entity.Attachment;
import io.rednotice.attachment.repository.AttachmentRepository;
import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.common.AttachmentValidation;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.config.S3ServiceUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final S3ServiceUtility s3ServiceUtility;
    private final String BUCKET_NAME = "rednotice-sample";
    private final CardRepository cardRepository;


    @Transactional
    public Attachment uploadFile(MultipartFile file, Long cardId) throws ApiException {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_));

        AttachmentValidation.validationFile(file);

        String fileUrl = s3ServiceUtility.uploadFile(file, BUCKET_NAME);

        Attachment attachment = new Attachment();
        attachment.setFileUrl(fileUrl);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setCard(card);

        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAttachment(Long cardId) {
        return attachmentRepository.findByCardId(cardId);
    }

    @Transactional
    public Void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._ATTACHMENT_NOT_FOUND));

        s3ServiceUtility.deleteFile(attachment.getFileUrl(), BUCKET_NAME);
        attachmentRepository.delete(attachment);
        return null;
    }
}
