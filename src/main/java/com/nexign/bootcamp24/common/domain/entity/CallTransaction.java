package com.nexign.bootcamp24.common.domain.entity;

import com.nexign.bootcamp24.common.domain.enums.CallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "call_transaction")
public class CallTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    @Column(nullable = false)
    private CallType callType;

    @Column(nullable = false)
    private LocalDateTime dateStart;

    @Column(nullable = false)
    private LocalDateTime dateEnd;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(nullable = false)
    private Customer customer;
}
