package uz.tsue.ricoin.service.interfaces;

import uz.tsue.ricoin.dto.EventDto;

import java.util.List;

public interface EventService {

    EventDto get(Long id);

    List<EventDto> getAll();

    EventDto create(EventDto eventDto);

    void update(Long id, EventDto eventDto);

    void updatePromoCode(Long id);

    void remove(Long id);


}
