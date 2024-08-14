package com.bigbro.wwooss.v1.entity.token;

import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import com.bigbro.wwooss.v1.enumType.IssueType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "token")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Comment("발급유형")
    @Column(name = "issue_type")
    @Enumerated(value = EnumType.STRING)
    private IssueType issueType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Comment("토큰 값")
    @Column(name = "value")
    private Double value;

}
