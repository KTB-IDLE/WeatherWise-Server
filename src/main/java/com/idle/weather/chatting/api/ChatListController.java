package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomService;
import com.idle.weather.chatting.api.port.WeatherAlertService;
import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.chatting.api.response.WeatherAlertResponse;
import com.idle.weather.chatting.repository.WeatherAlertEntity;
import com.idle.weather.chatting.service.LocationParsingService;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat-list")
@RequiredArgsConstructor @Slf4j
public class ChatListController {
    private final WeatherAlertService weatherAlertService;
    private final LocationParsingService locationParsingService;
    private final ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatRoomResponse> getAllWeatherAlert(@UserId Long userId ,
                                                     @RequestParam("latitude") double latitude,
                                                     @RequestParam("longitude") double longitude) {
        // 위도 경도에 따라 가장 큰 단위의 지역을 가져오기
        String result = locationParsingService.parsing(latitude, longitude);
        List<WeatherAlertEntity> weatherAlertEntities = weatherAlertService.getRegionList(result);
        return chatRoomService.getAllActivatedChatRooms(weatherAlertEntities);
    }
}
