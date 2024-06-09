package com.gndv.wish.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.wish.domain.dto.WishDTO;
import com.gndv.wish.domain.entity.Wish;
import com.gndv.wish.mapper.WishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
    private final WishMapper wishMapper;
    private final MemberMapper memberMapper;

    @Override
    public Long getMemberIdByEmail(String email) {
        return memberMapper.findByEmail(email).get().getMember_id();
    }

    @Override
    public String toggleWish(WishDTO wishDTO) {
        if (wishMapper.existsByMemberIdAndItemId(wishDTO.getMemberId(), wishDTO.getItemId())) {
            wishMapper.deleteByMemberIdAndItemId(wishDTO.getMemberId(), wishDTO.getItemId());
            return "Wish removed successfully";
        } else {
            wishMapper.insertWish(wishDTO);
            return "Wish added successfully";
        }
    }

    @Override
    public boolean isWishListed(Long memberId, Long itemId) {
        return wishMapper.existsByMemberIdAndItemId(memberId, itemId);
    }

    @Override
    public List<WishDTO> getWishlist(Long memberId) {
        List<Wish> wishes = wishMapper.findByMemberId(memberId);
        return wishes.stream()
                .map(wish -> new WishDTO(wish.getWishId(), wish.getMemberId(), wish.getItemId(), wish.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeWish(WishDTO wishDTO) {
        wishMapper.deleteByMemberIdAndItemId(wishDTO.getMemberId(), wishDTO.getItemId());
    }
}
