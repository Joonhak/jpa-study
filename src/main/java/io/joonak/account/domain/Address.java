package io.joonak.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @NotEmpty
    @Column(name = "address", nullable = false)
    private String address;

    @NotEmpty
    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @NotEmpty
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Builder
    public Address(@NotEmpty String address, @NotEmpty String detailAddres, @NotEmpty String zipCode) {
        this.address = address;
        this.detailAddress = detailAddres;
        this.zipCode = zipCode;
    }
}
