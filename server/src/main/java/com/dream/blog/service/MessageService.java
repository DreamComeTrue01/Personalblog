package com.dream.blog.service;

import com.dream.blog.model.Message;
import com.dream.blog.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }
    
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
    
    public Message updateMessage(Integer id, Message message) {
        message.setId(id);
        return messageRepository.save(message);
    }
    
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }
}