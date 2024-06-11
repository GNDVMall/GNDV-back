package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.request.ChatRoomMessageRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatMessage;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatMessage;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("{ CALL CreateChatRoomAndUsers(#{product_id, mode=IN, jdbcType=BIGINT}, #{item_id, mode=IN,jdbcType=BIGINT }, #{seller, mode=IN,jdbcType=VARCHAR}, #{email, mode=IN,jdbcType=VARCHAR }, #{chatRoomId, mode=OUT,jdbcType=BIGINT }) }")
    @Options(statementType = StatementType.CALLABLE)
    void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest);

    @Update("UPDATE Chat_User cu SET cu.`leave` = 'Y' WHERE cu.chatroom_id = #{chatroom_id} " +
            "AND (SELECT m.member_id FROM `Member` m WHERE m.email = #{email}) = cu.member_id ")
    int deleteUserFromChatroom(Long chatroom_id, String email);

    @Select("WITH Recent_Messages AS (\n" +
            "SELECT chatroom_id, chat_content, ROW_NUMBER() OVER (PARTITION BY chatroom_id ORDER BY sent_at DESC) as rn\n" +
            "FROM Chat_Message),\n" +
            "Unread_Message_Count AS ( SELECT c.chatroom_id, COUNT(*) AS unread_count\n" +
            "FROM Chat_Message c JOIN `Member` m ON m.member_id = c.member_id\n" +
            "WHERE is_read = 'N' AND m.email != #{email}\n" +
            "GROUP BY chatroom_id )\n" +
            "SELECT cr.*, cu.*, m.nickname, rm.chat_content, m.profile_url, uc.unread_count\n" +
            "FROM Chat_Room cr JOIN Chat_User cu ON cr.chatroom_id = cu.chatroom_id\n" +
            "JOIN `Member_With_Profile` m ON cu.member_id = m.member_id\n" +
            "LEFT JOIN Recent_Messages rm ON cr.chatroom_id = rm.chatroom_id AND rm.rn = 1\n" +
            "LEFT JOIN Unread_Message_Count uc ON uc.chatroom_id = cr.chatroom_id \n" +
            "WHERE m.email = #{email}\n" +
            "GROUP BY cr.chatroom_id")
    List<ChatRoomResponse> findAllbyName(String email);

    @Select("SELECT cr.*, cu.*, m.nickname , m.rating , m.email, m.profile_url, p.product_sales_status, p.images, p.title, p.price, p.product_status\n" +
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

    @Select("{ CALL UpdateAndSelectChatMessages(#{chatroom_id, mode=IN, jdbcType=BIGINT},#{email, mode=IN,jdbcType=VARCHAR }) }")
    List<ChatMessage> findAllMessagesByIdAndUpdateIsRead(ChatRoomMessageRequest request);

    @Update("UPDATE Chat_Message\n" +
            "    SET read_at = CURRENT_TIMESTAMP(), is_read = 'Y'\n" +
            "    WHERE message_id = #{message_id}")
    int updateMessageReadStatus(Long message_id);
}
