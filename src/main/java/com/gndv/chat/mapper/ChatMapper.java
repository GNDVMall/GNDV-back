package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("{ CALL CreateChatRoomAndUsers(#{product_id, mode=IN, jdbcType=BIGINT}, #{item_id, mode=IN,jdbcType=BIGINT }, #{seller_id, mode=IN,jdbcType=BIGINT}, #{member_id, mode=IN,jdbcType=BIGINT }, #{chatRoomId, mode=OUT,jdbcType=BIGINT }) }")
    @Options(statementType = StatementType.CALLABLE)
    void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest);

    @Delete("DELETE cu FROM Chat_User cu\n" +
            "JOIN `Member` m ON cu.member_id = m.member_id\n" +
            "WHERE cu.chatroom_id = #{chatroom_id} AND m.email = #{name}")
    int deleteUserFromChatroom(Long chatroom_id, String name);

    @Select("WITH Recent_Messages AS (\n" +
            "    SELECT chatroom_id, chat_content, ROW_NUMBER() OVER (PARTITION BY chatroom_id ORDER BY sent_at DESC) as rn\n" +
            "    FROM Chat_Message\n" +
            ")\n" +
            "SELECT cr.*, cu.*, m.email, rm.chat_content\n" +
            "FROM Chat_Room cr\n" +
            "JOIN Chat_User cu ON cr.chatroom_id = cu.chatroom_id\n" +
            "JOIN `Member` m ON cu.member_id = m.member_id\n" +
            "JOIN Recent_Messages rm ON cr.chatroom_id = rm.chatroom_id AND rm.rn = 1\n" +
            "WHERE m.email = #{name}")
    List<ChatRoomResponse> findAllbyName(String name);
}
