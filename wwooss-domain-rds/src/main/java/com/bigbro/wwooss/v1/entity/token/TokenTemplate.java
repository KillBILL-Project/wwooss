package com.bigbro.wwooss.v1.entity.token;

import com.bigbro.wwooss.v1.enumType.IssueType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "token_template")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenTemplate {

    @Id
    @Column(name = "token_temolate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenTemplateId;

    @Comment("발급유형")
    @Column(name = "issue_type")
    @Enumerated(value = EnumType.STRING)
    private IssueType issueType;

    @Comment("토큰 값")
    @Column(name = "value")
    private String value;

}
