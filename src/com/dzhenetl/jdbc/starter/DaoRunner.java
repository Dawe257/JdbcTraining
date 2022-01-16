package com.dzhenetl.jdbc.starter;

import com.dzhenetl.jdbc.starter.dao.TicketDao;
import com.dzhenetl.jdbc.starter.dto.TicketFilter;
import com.dzhenetl.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        TicketFilter filter = new TicketFilter(3, 2, null, "B1");
        TicketDao ticketDao = TicketDao.getInstance();
        List<Ticket> tickets = ticketDao.findAll(filter);
        tickets.forEach(System.out::println);
    }

    private static void findAllTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        List<Ticket> tickets = ticketDao.findAll();
        System.out.println(tickets);
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Optional<Ticket> maybeTicket = ticketDao.findById(55L);
        System.out.println(maybeTicket);

        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(1888.88));
            ticketDao.update(ticket);
        });
    }

    private static void deleteTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        boolean deleteResult = ticketDao.delete(56L);
        System.out.println(deleteResult);
    }

    private static void savedTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("1234566");
        ticket.setPassengerName("Test");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B6");
        ticket.setCost(BigDecimal.TEN);

        Ticket savedTicket = ticketDao.save(ticket);
        System.out.println(savedTicket);
    }
}
