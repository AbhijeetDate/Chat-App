package com.example.Project1.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project1.dto.MessageResponse;
import com.example.Project1.dto.SendMessageRequest;
import com.example.Project1.entity.Connections;
import com.example.Project1.entity.Message;
import com.example.Project1.repository.ConnectionRepository;
import com.example.Project1.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

        @Autowired
        private ConnectionRepository connectionRepository;


    private boolean areUsersConnected(
                Integer userId1,
                Integer userId2)
        {
                Optional<Connections> connection1 =
                        connectionRepository.findByUserId1AndUserId2(
                                userId1,
                                userId2
                                );

                Optional<Connections> connection2 =
                        connectionRepository.findByUserId1AndUserId2(
                                userId2,
                                userId1
                        );

                return connection1.isPresent() || connection2.isPresent();
        }

    public List<MessageResponse> getChatHistory(Integer userId, Integer otherUserId) {

        List<Message> messages = messageRepository
                        .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
                                userId,
                                otherUserId,
                                otherUserId,
                                userId
                        );

        return messages.stream()
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getSenderId(),
                        message.getReceiverId(),
                        message.getMessage(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());

    }

    public MessageResponse saveMessage(
        SendMessageRequest request)
{
    if (!areUsersConnected(
            request.getSenderId(),
            request.getReceiverId()))
    {
        throw new RuntimeException(
                "Users are not connected."
        );
    }

    Message message = new Message();

    message.setSenderId(
            request.getSenderId()
    );

    message.setReceiverId(
            request.getReceiverId()
    );

    message.setMessage(
            request.getMessage()
    );

    // Set current timestamp
    message.setTimestamp(
            new Timestamp(System.currentTimeMillis())
    );

    // Save message
    Message savedMessage =
            messageRepository.save(message);

    return new MessageResponse(
            savedMessage.getId(),
            savedMessage.getSenderId(),
            savedMessage.getReceiverId(),
            savedMessage.getMessage(),
            savedMessage.getTimestamp()
    );
}
}

