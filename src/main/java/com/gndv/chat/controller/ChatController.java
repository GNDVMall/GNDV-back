package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.request.ChatRoomMessageRequest;
import com.gndv.chat.domain.dto.response.ChatRoomDetailResponse;
import com.gndv.chat.domain.dto.response.ChatRoomListResponse;
import com.gndv.chat.domain.dto.response.ChatRoomMessageResponse;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatMessage;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import com.gndv.chat.service.ChatService;
import com.gndv.common.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v2/chat")
@Tag(name = "Chat API", description = "채팅 관련 API")
public class ChatController {
    private final ChatService chatService;
    private final ModelMapper modelMapper;

    @Operation(summary = "채팅방 존재 여부 확인", description = "이미 생성된 채팅방이 있는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "확인 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check")
    public CustomResponse<Object> checkIsRoom(@Parameter(description = "상품 ID") @RequestParam Long product_id) {
        log.info("이미 생성된 채팅방이 있는지 확인");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ChatRoomCheckRequest cr = ChatRoomCheckRequest.builder()
                .buyer_email(auth.getName()).product_id(product_id).build();

        ChatRoom chatroom = chatService.checkIsRoom(cr);
        if (chatroom == null) return CustomResponse.ok("채팅방이 존재하지 않습니다.", null);
        return CustomResponse.ok("채팅방이 이미 존재합니다.", chatroom.getChatroom_id());
    }

    @Operation(summary = "채팅방 목록 조회", description = "사용자가 참여한 채팅방 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @Content(schema = @Schema(implementation = ChatRoomListResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public CustomResponse<ChatRoomListResponse> getChatRooms() {
        log.info("Get ChatRoom list");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<ChatRoomResponse> chatrooms = chatService.getChatRooms(auth.getName());

        return CustomResponse.ok("Get ChatRoom list",
                ChatRoomListResponse.builder()
                        .total(chatrooms.size())
                        .list(chatrooms).build());
    }

    @Operation(summary = "채팅방 상세 조회", description = "특정 채팅방의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = ChatRoomDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{chatrooom_id}")
    public CustomResponse<ChatRoomDetailResponse> getChatRoom(@Parameter(description = "채팅방 ID") @PathVariable Long chatrooom_id) {
        log.info("Get a ChatRoom");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ChatRoomDetail chatroom = chatService.getChatRoom(chatrooom_id, auth.getName());
        ChatRoomDetailResponse chatRoomDetailResponse = modelMapper.map(chatroom, ChatRoomDetailResponse.class);

        return CustomResponse.ok("Get ChatRoom", chatRoomDetailResponse);
    }

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = ChatRoomCreateRequest.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("")
    public CustomResponse<ChatRoomCreateRequest> createRoom(@RequestBody @Parameter(description = "채팅방 생성 요청 정보") ChatRoomCreateRequest chatRoomCreateRequest) throws Exception {
        log.info("Create ChatRoom {} ", chatRoomCreateRequest);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        chatRoomCreateRequest.setEmail(auth.getName());

        chatService.createChatRoom(chatRoomCreateRequest);
        return CustomResponse.ok("Create new ChatRoom", chatRoomCreateRequest);
    }

    @Operation(summary = "채팅방 나가기", description = "사용자가 특정 채팅방에서 나갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 나가기 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{chatroom_id}")
    public CustomResponse<Object> deleteUserFromChatroom(@Parameter(description = "채팅방 ID") @PathVariable Long chatroom_id) throws Exception {
        log.info("Leave ChatRoom {}", chatroom_id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int updated = chatService.deleteUserFromChatroom(chatroom_id, auth.getName());
        if (updated != 1) {
            throw new Exception("채팅방 떠나기 실패");
        }
        return CustomResponse.ok("Leave ChatRoom ok");
    }

    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방의 메시지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공", content = @Content(schema = @Schema(implementation = ChatRoomMessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{chatroom_id}/messages")
    public CustomResponse<ChatRoomMessageResponse> getChatMessages(@Parameter(description = "채팅방 ID") @PathVariable Long chatroom_id) {
        log.info("채팅방 메시지들 가져오기 - {}", chatroom_id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ChatRoomMessageRequest request = ChatRoomMessageRequest.builder()
                .chatroom_id(chatroom_id)
                .email(auth.getName())
                .build();

        List<ChatMessage> list = chatService.getChatMessages(request);

        ChatRoomMessageResponse cmr = ChatRoomMessageResponse.builder()
                .list(list).build();
        return CustomResponse.ok("채팅방 메시지들 반환", cmr);
    }

    @Operation(summary = "채팅 메시지 읽음 처리", description = "특정 채팅 메시지를 읽음 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽음 처리 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/messages/{message_id}")
    public CustomResponse<Object> readChatMessage(@Parameter(description = "메시지 ID") @PathVariable Long message_id) throws Exception {
        log.info("채팅 읽음 처리 : {}", message_id);
        int updated = chatService.readChatMessage(message_id);

        if (updated != 1) throw new Exception("채팅 메시지 읽음 처리 실패");
        return CustomResponse.ok("채팅 메시지 읽음 처리");
    }
}
