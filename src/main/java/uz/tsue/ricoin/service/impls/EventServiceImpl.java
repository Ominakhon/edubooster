package uz.tsue.ricoin.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.tsue.ricoin.dto.EventDto;
import uz.tsue.ricoin.entity.Event;
import uz.tsue.ricoin.repository.EventRepository;
import uz.tsue.ricoin.service.PromoCodeGeneratorService;
import uz.tsue.ricoin.service.interfaces.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final PromoCodeGeneratorService promoCodeGeneratorService;

    @Override
    public EventDto get(Long id) {
        Event event = findById(id);
        return getEventDtoFromEvent(event);
    }

    @Override
    public List<EventDto> getAll() {
        List<EventDto> events = new ArrayList<>();
        List<Event> list = eventRepository.findAll();
        for (Event event : list) {
            events.add(getEventDtoFromEvent(event));
        }
        return events;
    }

    private Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = getEventFromEventDto(eventDto);
        eventRepository.save(event);
        return getEventDtoFromEvent(event);
    }

    @Override
    public void update(Long id, EventDto eventDto) {
        Event event = findById(id);
        Optional.ofNullable(eventDto.name()).ifPresent(event::setName);
        Optional.ofNullable(eventDto.description()).ifPresent(event::setDescription);
        Optional.ofNullable(eventDto.place()).ifPresent(event::setPlace);
        Optional.ofNullable(eventDto.price()).ifPresent(event::setPrice);
        event.setPromoCode(promoCodeGeneratorService.generatePromoCode(10));
        if (eventDto.dateTime() != null)
            event.setDateTime(getFormattedDateTime(eventDto.dateTime()));

        eventRepository.save(event);
    }

    @Override
    public void updatePromoCode(Long id) {
        Event event = findById(id);
        event.setPromoCode(promoCodeGeneratorService.generatePromoCode(10));
        eventRepository.save(event);
    }

    @Override
    public void remove(Long id) {
        eventRepository.deleteById(id);
    }


    private LocalDateTime getFormattedDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    private Event getEventFromEventDto(EventDto eventDto){
        return Event.builder()
                .name(eventDto.name())
                .description(eventDto.description())
                .price(eventDto.price())
                .place(eventDto.place())
                .dateTime(getFormattedDateTime(eventDto.dateTime()))
                .isActive(true)
                .promoCode(promoCodeGeneratorService.generatePromoCode(10))
                .build();
    }

    private EventDto getEventDtoFromEvent(Event event){
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .place(event.getPlace())
                .price(event.getPrice())
                .dateTime(event.getDateTime().toString())
                .isActive(event.isActive())
                .promoCode(event.getPromoCode())
                .build();
    }

}
