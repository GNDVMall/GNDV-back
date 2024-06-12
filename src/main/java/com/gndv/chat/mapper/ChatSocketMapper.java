package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ChatSocketMapper {
    @Insert("INSERT INTO Chat_Message (chat_content,member_id ,chatroom_id, message_user_type) " +
            "VALUES (#{content},(SELECT member_id FROM Member WHERE email = #{email}),#{chatroom_id}, #{message_user_type})")
    @Options(useGeneratedKeys = true, keyProperty = "message_id", keyColumn = "message_id")
    int insertMessage(ChatMessageRequest chatMessageRequest);
}
