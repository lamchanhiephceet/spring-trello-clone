package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.Attachment;
import com.hn.springtrelloclone.model.GCard;
import com.hn.springtrelloclone.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final GCardService gCardService;

    public Attachment saveAttachment(Attachment attachment){
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAttachmentList(Long id){
        GCard card = gCardService.findById(id);
        return attachmentRepository.findAllByCard(card);
    }

    public Attachment findAttachmentById(Long id){
        return attachmentRepository.findById(id)
                .orElseThrow(()-> new SpringTrelloException("Khong tim thay Attachment:" +id));
    }

    public void deleteAttachment(Long id){ attachmentRepository.deleteById(id);
    }

}
