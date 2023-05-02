package com.example.domain.entity;

import javax.persistence.*;

import com.example.domain.dto.point.request.TicketCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Builder(builderMethodName = "TicketBuilder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticket_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(name = "car_number")
    private String car_number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sha_id")
    private ShareLot shareLot;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "parking_region")
    private String parkingRegion;

    private String address;

    private int type;

    private int cost;

    @Column(name = "in_timing")
    private int in_timing;

    @ColumnDefault("true")
    private boolean validation;

    @Column(name = "parking_date")
    private String parking_date;

    @Column(name = "buy_confirm", columnDefinition = "boolean default true")
    private boolean buy_confirm;

    @Column(name="sell_confirm", columnDefinition = "boolean default false")
    private boolean sell_confirm;

    public static TicketBuilder builder(ShareLot shareLot, User buyer, TicketCreateRequestDto ticketCreateRequestDto){
        int[] typeToHour = new int[]{2, 6, 10, 48};
        int cost = typeToHour[ticketCreateRequestDto.getType()]*shareLot.getSha_fee();

        return TicketBuilder()
                .shareLot(shareLot)
                .buyer(buyer)
                .car_number(ticketCreateRequestDto.getCarNumber())
                .sellerId(shareLot.getUser().getUser_id())
                .parkingRegion(shareLot.getSha_name())
                .address(shareLot.getSha_road())
                .type(ticketCreateRequestDto.getType())
                .cost(cost)
                .parking_date(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .in_timing(ticketCreateRequestDto.getInTiming());
    }

    public void setBuy_confirm(boolean buyConfirm) {
        this.buy_confirm = buyConfirm;
    }

    public void setSell_confirm(boolean sellConfirm) {
        this.sell_confirm = sellConfirm;
    }

}
