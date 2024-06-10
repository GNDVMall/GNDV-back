package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.request.ChatRoomMessageRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatMessage;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("{ CALL CreateChatRoomAndUsers(#{product_id, mode=IN, jdbcType=BIGINT}, #{item_id, mode=IN,jdbcType=BIGINT }, #{seller, mode=IN,jdbcType=VARCHAR}, #{email, mode=IN,jdbcType=VARCHAR }, #{chatRoomId, mode=OUT,jdbcType=BIGINT }) }")
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
            "SELECT cr.*, cu.*, m.nickname, rm.chat_content, m.profile_url\n" +
            "FROM Chat_Room cr\n" +
            "JOIN Chat_User cu ON cr.chatroom_id = cu.chatroom_id\n" +
            "JOIN `Member_With_Profile` m ON cu.member_id = m.member_id\n" +
            "LEFT JOIN Recent_Messages rm ON cr.chatroom_id = rm.chatroom_id AND rm.rn = 1\n" +
            "WHERE m.email = #{name}")
    List<ChatRoomResponse> findAllbyName(String name);

    @Select("SELECT cr.*, cu.*, m.nickname , m.rating , m.email, m.profile_url, p.title, p.price, p.product_status\n" +
            "FROM Chat_Room cr \n" +
            "JOIN Chat_User cu ON cr.chatroom_id = cu.chatroom_id \n" +
            "JOIN Member_With_Profile m ON cu.member_id = m.member_id\n" +
            "JOIN Product_With_Image p ON cr.product_id = p.product_id \n" +
            "WHERE cr.chatroom_id = #{chatroom_id} AND m.email != #{name}")
    ChatRoomDetail findByIdWithName(Long chatroom_id, String name);

    @Select("SELECT cr.* from Chat_Room cr  \n" +
            "JOIN Chat_User cu ON cr.chatroom_id = cu.chatroom_id \n" +
            "WHERE product_id = #{product_id} AND cu.chat_user_type = 'BUYER' AND (SELECT m.email FROM `Member` m WHERE cu.member_id = m.member_id) = #{buyer_email}")
    ChatRoom getChatRoomId(ChatRoomCheckRequest request);

    @Select("SELECT cm.*, \n" +
            "       mwp.nickname, \n" +
            "       mwp.profile_url,\n" +
            "       CASE \n" +
            "           WHEN mwp.email = #{email} THEN 'SENT'\n" +
            "           ELSE 'RECEIVE'\n" +
            "       END AS message_type\n" +
            "FROM Chat_Message cm\n" +
            "JOIN Member_With_Profile mwp ON cm.member_id = mwp.member_id\n" +
            "WHERE chatroom_id = #{chatroom_id}\n" +
            "ORDER BY sent_at ASC;")
    List<ChatMessage> getChatMessages(ChatRoomMessageRequest request);
}
