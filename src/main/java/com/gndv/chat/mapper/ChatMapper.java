package com.gndv.chat.mapper;

import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

@Mapper
public interface ChatMapper {
    @Select("{ CALL CreateChatRoomAndUsers(#{product_id, mode=IN, jdbcType=BIGINT}, #{item_id, mode=IN,jdbcType=BIGINT }, #{seller_id, mode=IN,jdbcType=BIGINT}, #{member_id, mode=IN,jdbcType=BIGINT }, #{chatRoomId, mode=OUT,jdbcType=BIGINT }) }")
    @Options(statementType = StatementType.CALLABLE)
    void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest);

    @Delete("DELETE cu FROM Chat_User cu\n" +
            "JOIN `Member` m ON cu.member_id = m.member_id\n" +
            "WHERE cu.chatroom_id = #{chatroom_id} AND m.email = #{name}")
    int deleteUserFromChatroom(Long chatroom_id, String name);
}
