package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.ChatRoomService;
import com.idle.weather.chatting.api.port.WeatherAlertService;
import com.idle.weather.chatting.api.response.ChatRoomResponse;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.chatting.repository.WeatherAlertEntity;
import com.idle.weather.chatting.service.LocationParsingService;
import com.idle.weather.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-list")
@RequiredArgsConstructor
public class ChatListController {

    private final WeatherAlertService weatherAlertService;
    private final LocationParsingService locationParsingService;
    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ChatRoomResponse>>> getAllWeatherAlert(
            @UserId Long userId,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        String result = locationParsingService.parsing(latitude, longitude);
        List<WeatherAlertEntity> weatherAlertEntities = weatherAlertService.getRegionList(result);
        List<ChatRoomResponse> response = chatRoomService.getAllActivatedChatRooms(weatherAlertEntities);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
