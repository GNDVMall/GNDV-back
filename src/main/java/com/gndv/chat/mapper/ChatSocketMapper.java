package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ChatSocketMapper {
    @Insert("INSERT INTO Chat_Message (chat_content,member_id ,chatroom_id) " +
            "VALUES (#{content},(SELECT member_id FROM Member WHERE email = #{email}),#{chatroom_id})")
    @Options(useGeneratedKeys = true, keyProperty = "message_id", keyColumn = "message_id")
    int insertMessage(ChatMessageRequest chatMessageRequest);
}
