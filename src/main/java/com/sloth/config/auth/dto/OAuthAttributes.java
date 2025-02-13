package com.sloth.config.auth.dto;

import com.sloth.domain.member.constant.SocialType;
import lombok.Builder;
import lombok.Getter;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private SocialType socialType;
    private String password;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, SocialType socialType, String password) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.socialType = socialType;
        this.password = password;
    }

    /**
     * OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해줘야한다.
     * @param registrationId
     * @param userNameAttributeName
     * @param attributes
     * @return
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String password) {
        return ofGoogle(userNameAttributeName, attributes, password);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String password) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .socialType(SocialType.GOOGLE)
                .password(password)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .memberName(name)
                .email(email)
                .role(Role.ADMIN)
                .password(password)
                .socialType(SocialType.GOOGLE)
                .build();
    }

}
